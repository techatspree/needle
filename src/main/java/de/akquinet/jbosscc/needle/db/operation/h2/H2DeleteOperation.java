package de.akquinet.jbosscc.needle.db.operation.h2;

import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
import de.akquinet.jbosscc.needle.db.operation.hsql.HSQLDeleteOperation;

/**
 * Delete everything from the DB: This cannot be done with the JPA, because the
 * order of deletion matters. Instead we directly use a JDBC connection.
 */
public class H2DeleteOperation extends HSQLDeleteOperation {

    public H2DeleteOperation(JdbcConfiguration configuration) {
        super(configuration);
    }

}
