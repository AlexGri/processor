package org.comsoft.dbf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.Field;

public class FixDBF extends DBF {

	public FixDBF(String DBFname, char readOnly, String inEncodeType)
			throws xBaseJException, IOException {
		super(DBFname, readOnly, inEncodeType);
	}
	
	public Collection<Field> getFieldSet() throws ArrayIndexOutOfBoundsException, xBaseJException {
		Collection<Field> result = new ArrayList<Field>(getFieldCount());
		for (int i = 1; i <= getFieldCount(); i++) {
			result.add(getField(i));			
		}
		return result;
	}
	
	public Collection<Field> getFieldSet(Collection<String> selectingFields) throws ArrayIndexOutOfBoundsException, xBaseJException {
		if (selectingFields == null || selectingFields.isEmpty())
			return getFieldSet();
		
		Collection<Field> result = new ArrayList<Field>(selectingFields.size());
		for (String fieldName : selectingFields) {
			result.add(getField(fieldName));
		}
		return result;
	}

}
