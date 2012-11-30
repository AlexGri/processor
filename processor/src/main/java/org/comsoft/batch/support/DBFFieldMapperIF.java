package org.comsoft.batch.support;

import org.xBaseJ.DBF;

public interface DBFFieldMapperIF<T> {
	public T mapDBF(DBF dbf) throws Exception;
}
