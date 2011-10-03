package de.akquinet.jbosscc.needle.db.operation.hsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;
import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;

/**
 * Delete everything from the DB: This cannot be done with the JPA, because the
 * order of deletion matters. Instead we directly use a JDBC connection.
 */
public class HSQLDeleteOperation extends AbstractDBOperation {

	private static final Logger LOG = LoggerFactory.getLogger(HSQLDeleteOperation.class);

	public HSQLDeleteOperation(final JdbcConfiguration configuration) {
		super(configuration);
	}

	/**
	 * {@inheritDoc} No operation implementation.
	 */
	@Override
	public void setUpOperation() throws SQLException {
	}

	/**
	 * {@inheritDoc}. Delete all data from all tables given by {@link DatabaseMetaData#getTables(String, String, String, String[]).
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	@Override
	public void tearDownOperation() throws SQLException {
		Connection connection = null;
		Statement statement = null;

		try {
			connection = getConnection();
			statement = connection.createStatement();

			disableReferentialIntegrity(statement);
			List<String> tableNames = getTableNames(connection);

			deleteContent(tableNames, statement);

		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			try {
				rollback();
			} catch (SQLException e1) {
				LOG.error(e1.getMessage(), e1);
			}
		} finally {
			try {
				enableReferentialIntegrity(statement);

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

	/**
	 * Disables the referential constraints of the database, e.g foreign keys.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	protected void disableReferentialIntegrity(final Statement statement) throws SQLException {
		setReferentialIntegrity(false, statement);
	}

	/**
	 * Enabled the referential constraints of the database, e.g foreign keys.
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	protected void enableReferentialIntegrity(final Statement statement) throws SQLException {
		setReferentialIntegrity(true, statement);
	}

	private void setReferentialIntegrity(final boolean enable, final Statement statement) throws SQLException {
		final int databaseMajorVersion = statement.getConnection().getMetaData().getDatabaseMajorVersion();
		final String referentialIntegrity = enable ? "TRUE" : "FALSE";

		final String command = databaseMajorVersion < 2 ? "SET REFERENTIAL_INTEGRITY "
		        : "SET DATABASE REFERENTIAL INTEGRITY ";
		getConnection().prepareStatement(command + referentialIntegrity).execute();
	}

	/**
	 * Deletes all contents from the given tables.
	 *
	 * @param tables
	 *            a {@link List} of table names who are to be deleted.
	 *
	 * @param statement
	 *            the {@link Statement} to used for executing a SQL statement.
	 *
	 *
	 * @throws SQLException
	 *             - if a database access error occurs
	 */
	protected void deleteContent(final List<String> tables, final Statement statement) throws SQLException {

		final ArrayList<String> tempTables = new ArrayList<String>(tables);

		// Loop until all data is deleted: we don't know the correct DROP
		// order, so we have to retry upon failure
		while (!tempTables.isEmpty()) {
			final int sizeBefore = tempTables.size();
			for (final ListIterator<String> iterator = tempTables.listIterator(); iterator.hasNext();) {
				final String table = iterator.next();

				try {
					statement.executeUpdate("DELETE FROM " + table);
					iterator.remove();
				} catch (final SQLException exc) {
					LOG.warn("Ignored exception: " + exc.getMessage() + ". WILL RETRY.");
				}
			}
			if (tempTables.size() == sizeBefore) {
				throw new AssertionError("unable to clean tables " + tempTables);
			}

		}

	}

}
