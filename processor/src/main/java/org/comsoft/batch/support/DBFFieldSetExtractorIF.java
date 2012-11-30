package org.comsoft.batch.support;

import org.springframework.batch.item.file.transform.FieldSet;
import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;

public interface DBFFieldSetExtractorIF  {
	public FieldSet extract(final DBF dbf) throws xBaseJException ;
}
