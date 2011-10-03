package de.akquinet.jbosscc.needle.db.operation;

import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Execute before and after sql scripts in test setup and tear down.
 *
 */
public class ExecuteScriptOperation extends AbstractDBOperation {

	private static final Logger LOG = LoggerFactory.getLogger(ExecuteScriptOperation.class);

	public ExecuteScriptOperation(JdbcConfiguration jdbcConfiguration) {
		super(jdbcConfiguration);
	}

	private static final String BEFORE_SCRIPT_NAME = "before.sql";
	private static final String AFTER_SCRIPT_NAME = "after.sql";

	/**
	 * Execute before.sql script in test setup.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	@Override
	public void setUpOperation() throws SQLException {
		execute(BEFORE_SCRIPT_NAME);
	}

	/**
	 * Execute after.sql script in test tear down.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	@Override
	public void tearDownOperation() throws SQLException {
		execute(AFTER_SCRIPT_NAME);
	}

	private void execute(final String filename) {
		Statement statement = null;

		try {
			statement = getConnection().createStatement();

			executeScript(filename, statement);

			commit();
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			try {
				rollback();
			} catch (SQLException e1) {
				LOG.error(e1.getMessage(), e1);
			}
		} finally {
			try {

				if (statement != null) {
					statement.close();
				}
				commit();

				closeConnection();
			} catch (SQLException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

}
