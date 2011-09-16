package de.akquinet.jbosscc.needle.db.dialect;

import java.sql.SQLException;
import java.util.List;

public interface DBDialect {

	/**
	 * Disables the referential constraints of the database, e.g foreign keys.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	void disableReferentialIntegrity() throws SQLException;

	/**
	 * Enabled the referential constraints of the database, e.g foreign keys.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	void enableReferentialIntegrity() throws SQLException;

	/**
	 * Returns the names of all tables in the database.
	 *
	 * @return a {@link List} with all table names
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	List<String> getTableNames() throws SQLException;

	/**
	 * Establish a connection to the given database.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	void openConnection() throws SQLException;

	/**
	 * Close the connection to the database.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	void closeConnection() throws SQLException;

	/**
	 * Deletes all contents from the given tables.
	 *
	 * @param tables
	 *            - a {@link List} of table names who are to be deleted.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	void deleteContent(List<String> tables) throws SQLException;

	/**
	 * Commits the current transaction.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	void commit() throws SQLException;

	/**
	 * Revoke the current transaction.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	void rollback() throws SQLException;

}
