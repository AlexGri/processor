package org.comsoft.batch;

import org.comsoft.batch.support.DBFFieldMapperIF;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.xBaseJ.DBF;

public class DBFCountingItemStreamReader<T> extends
		AbstractItemCountingItemStreamItemReader<T> implements InitializingBean{
	
	private DBF dbf;
	
	private Resource resource;
	
	private DBFFieldMapperIF<T> dbfFieldMapper;
	
	private String encode = DBF.encodedType;
	
	private boolean noInput = false;
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}	

	public void setEncode(String encode) {
		this.encode = encode;
	}	

	public void setDbfFieldMapper(DBFFieldMapperIF<T> dbfFieldMapper) {
		this.dbfFieldMapper = dbfFieldMapper;
	}
	
	public DBFCountingItemStreamReader() {
		setName(ClassUtils.getShortName(DBFCountingItemStreamReader.class));
	}

	@Override
	protected T doRead() throws Exception {
		if (noInput) {
			return null;
		}
		dbf.read();
		return dbfFieldMapper.mapDBF(dbf);
	}
	
	@Override
	protected void jumpToItem(int itemIndex) throws Exception {
		dbf.gotoRecord(itemIndex);
		setCurrentItemCount(itemIndex);
	}

	@Override
	protected void doOpen() throws Exception {
		Assert.notNull(resource, "Input resource must be set");

		noInput = true;
		if (!resource.exists()) {
			throw new IllegalStateException("Input resource must exist: " + resource);
		}

		if (!resource.isReadable()) {
			throw new IllegalStateException("Input resource must be readable: " + resource);
		}

		dbf = new DBF(resource.getFile().getAbsolutePath(), DBF.READ_ONLY, encode);
		setMaxItemCount(dbf.getRecordCount());
		noInput = false;
	}

	@Override
	protected void doClose() throws Exception {
		dbf.close();
		dbf = null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dbfFieldMapper, "mapper must be set");		
	}
}
