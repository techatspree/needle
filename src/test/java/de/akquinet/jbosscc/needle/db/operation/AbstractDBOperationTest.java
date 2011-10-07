package de.akquinet.jbosscc.needle.db.operation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractDBOperationTest {

	private static final JdbcConfiguration HSQL_DB_CONFIGURATION = new JdbcConfiguration(
	        "jdbc:hsqldb:mem:AbstractDBOperationTestDB", "org.hsqldb.jdbcDriver", "sa", "");

	private AbstractDBOperation dbOperation = new AbstractDBOperation(HSQL_DB_CONFIGURATION) {

		@Override
		public void tearDownOperation() throws SQLException {

		}

		@Override
		public void setUpOperation() throws SQLException {

		}
	};

	@Before
	public void setUp() throws Exception {
		dbOperation.openConnection();
	}

	@Test
	public void testGetTableNames() throws Exception {
		Connection connection = dbOperation.getConnection();
		List<String> tableNames = dbOperation.getTableNames(connection);

		Assert.assertTrue(tableNames.isEmpty());

		Statement statement = connection.createStatement();
		dbOperation.executeScript("before.sql", statement);

		tableNames = dbOperation.getTableNames(connection);

		Assert.assertTrue(tableNames.contains("USER_TABLE"));
		Assert.assertTrue(tableNames.contains("ADDRESS_TABLE"));

		dbOperation.executeScript("after.sql", statement);

		statement.close();
	}

	@Test(expected = SQLException.class)
	public void testExecuteScript() throws Exception {
		Statement statement = null;
		try {
			Connection connection = dbOperation.getConnection();
			statement = connection.createStatement();
			dbOperation.executeScript("exception.sql", statement);
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Test
	public void testExecuteScript_UnknownFileName() throws Exception {
		// expect logging and not an NullPointerException
		dbOperation.executeScript("unknown.sql", null);
	}

	@After
	public void tearDown() throws Exception {
		dbOperation.closeConnection();
	}

}
