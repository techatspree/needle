package de.akquinet.jbosscc.needle.db.dialect;

import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
import de.akquinet.jbosscc.needle.db.operation.hsql.HSQLDeleteOperation;

/**
 * @deprecated use {@link HSQLDeleteOperation}
 *
 */
public class HSQLDialect extends HSQLDeleteOperation {

	public HSQLDialect(JdbcConfiguration configuration) {
	    super(configuration);
    }



}
