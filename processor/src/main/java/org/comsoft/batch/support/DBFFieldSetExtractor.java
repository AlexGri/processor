package org.comsoft.batch.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.Field;

public class DBFFieldSetExtractor implements DBFFieldSetExtractorIF, InitializingBean {
	
	private FieldSetFactory fieldSetFactory = new DefaultFieldSetFactory();
	
	private String[] extractingFieldNames;
	
	public void setFieldSetFactory(FieldSetFactory fieldSetFactory) {
		this.fieldSetFactory = fieldSetFactory;
	}

	public void setExtractingFieldNames(String[] names) {
		this.extractingFieldNames = Arrays.asList(names).toArray(new String[names.length]);
	}


	@Override
	public FieldSet extract(final DBF dbf) throws xBaseJException {
		Collection<Field> selectedFields = selectFields(dbf);
		Collection<String> selectedFieldValues = new ArrayList<String>(selectedFields.size());
		for (Field field : selectedFields) {
			selectedFieldValues.add(field.get());
		}
		return fieldSetFactory.create(selectedFieldValues.toArray(new String[selectedFields.size()]), extractingFieldNames);
	}

	private Collection<Field> selectAllFields(final DBF dbf) throws ArrayIndexOutOfBoundsException, xBaseJException {
		int cnt = dbf.getFieldCount();
		Collection<Field> result = new ArrayList<Field>(cnt);
		Collection<String> nameList = new ArrayList<String>(cnt);
		Field f;
		for (int i = 1; i <= cnt; i++) {
			f = dbf.getField(i);
			result.add(f);
			nameList.add(f.getName());
		}
		f = null;
		extractingFieldNames = nameList.toArray(new String[nameList.size()]);
		return result;
	}
	
	private Collection<Field> selectFields(final DBF dbf) throws ArrayIndexOutOfBoundsException, xBaseJException {		
		if (extractingFieldNames == null || extractingFieldNames.length == 0)
			return selectAllFields(dbf);
		
		Collection<Field> result = new ArrayList<Field>(extractingFieldNames.length);
		for (int i = 0; i < extractingFieldNames.length; i++) {
			result.add(dbf.getField(extractingFieldNames[i]));
		}
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(extractingFieldNames, "Field names must be non-null");
	}
}
