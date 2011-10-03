package de.akquinet.jbosscc.needle.db.operation;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ExecuteScriptOperationTest {

	private static final JdbcConfiguration HSQL_DB_CONFIGURATION = new JdbcConfiguration(
	        "jdbc:hsqldb:mem:ExecuteScriptOperationTestDB", "org.hsqldb.jdbcDriver", "sa", "");

	private ExecuteScriptOperation executeScriptOperation = new ExecuteScriptOperation(HSQL_DB_CONFIGURATION);

	@Test
	public void testSetUpOperation() throws Exception {
		executeScriptOperation.setUpOperation();

		List<String> tableNames = executeScriptOperation.getTableNames(executeScriptOperation.getConnection());

		Assert.assertFalse(tableNames.isEmpty());

		executeScriptOperation.closeConnection();
	}

	@Test
	public void testTearDownOperation() throws Exception {
		executeScriptOperation.tearDownOperation();

		List<String> tableNames = executeScriptOperation.getTableNames(executeScriptOperation.getConnection());
		Assert.assertTrue(tableNames.isEmpty());
		executeScriptOperation.closeConnection();
	}

}
