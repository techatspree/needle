package de.akquinet.jbosscc.needle.junit;

import java.sql.SQLException;

import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;
import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;

public class DBOperationImpl extends AbstractDBOperation {

    public DBOperationImpl(JdbcConfiguration jdbcConfiguration) {
        super(jdbcConfiguration);
    }

    @Override
    public void setUpOperation() throws SQLException {
        throw new RuntimeException("executed");

    }

    @Override
    public void tearDownOperation() throws SQLException {
        throw new RuntimeException("executed");

    }
}