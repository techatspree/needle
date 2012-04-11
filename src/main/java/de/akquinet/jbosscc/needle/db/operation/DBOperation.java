package de.akquinet.jbosscc.needle.db.operation;

import java.sql.SQLException;

/**
 * Database operations before and after test execution.
 */
public interface DBOperation {

	/**
	 * Execute the database operation in test setup.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	void setUpOperation() throws SQLException;

	/**
	 * Execute the database operation in test tear down.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	void tearDownOperation() throws SQLException;

}
