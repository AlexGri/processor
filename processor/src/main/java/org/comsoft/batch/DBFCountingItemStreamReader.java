package org.comsoft.batch;

import java.util.List;

import org.comsoft.dbf.DBFFieldMapper;
import org.comsoft.dbf.FixDBF;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.xBaseJ.DBF;

public class DBFCountingItemStreamReader<T> extends
		AbstractItemCountingItemStreamItemReader<T> {
	
	private FixDBF dbf;
	
	private Resource resource;
	
	private DBFFieldMapper<T> dbfFieldMapper;
	
	private List<String> selectingFields;
	
	private String encode = DBF.encodedType;
	
	private boolean noInput = false;
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}	

	public void setEncode(String encode) {
		this.encode = encode;
	}	

	public void setDbfFieldMapper(DBFFieldMapper<T> dbfFieldMapper) {
		this.dbfFieldMapper = dbfFieldMapper;
	}
	
	public void setSelectingFields(List<String> selectingFields) {
		this.selectingFields = selectingFields;
	}

	@Override
	protected T doRead() throws Exception {
		if (noInput) {
			return null;
		}
		dbf.read();
		return dbfFieldMapper.mapFields(dbf.getFieldSet(selectingFields));
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

		dbf = new FixDBF(resource.getFile().getAbsolutePath(), DBF.READ_ONLY, encode);
		noInput = false;
	}

	@Override
	protected void doClose() throws Exception {
		dbf.close();
		dbf = null;
	}
}
