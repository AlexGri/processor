package org.comsoft.batch.support;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.xBaseJ.DBF;

public class DBFFieldMapper<T> implements DBFFieldMapperIF<T>, InitializingBean{
	
	private DBFFieldSetExtractorIF extractor;

	private FieldSetMapper<T> fieldSetMapper;

	public T mapDBF(DBF dbf) throws Exception {
		FieldSet fs = extractor.extract(dbf);
		System.out.println("\n\n fs=" + (fs == null ? null : fs.getClass()));
		return fieldSetMapper.mapFieldSet(fs);
	}

	public void setExtractor(DBFFieldSetExtractorIF extractor) {
		this.extractor = extractor;
	}

	public void setFieldSetMapper(FieldSetMapper<T> fieldSetMapper) {
		this.fieldSetMapper = fieldSetMapper;
	}

	public void afterPropertiesSet() {
		Assert.notNull(extractor, "The DBFFieldSetExtractor must be set");
		Assert.notNull(fieldSetMapper, "The FieldSetMapper must be set");
	}
}
