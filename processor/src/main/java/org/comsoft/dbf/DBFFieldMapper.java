package org.comsoft.dbf;

import java.util.Collection;

import org.xBaseJ.fields.Field;

public interface DBFFieldMapper<T> {
	public T mapFields(Collection<Field> fields);
}
