package de.akquinet.jbosscc.needle.db.operation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.configuration.ConfigurationLoader;

/**
 * A abstract implementation of {@link DBOperation} with common jdbc operations.
 *
 */
public abstract class AbstractDBOperation implements DBOperation {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDBOperation.class);

	private final JdbcConfiguration configuration;

	private Connection connection;

	public AbstractDBOperation(final JdbcConfiguration jdbcConfiguration) {
		super();
		this.configuration = jdbcConfiguration;
	}

	/**
	 * Establish a connection to the given database.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	protected void openConnection() throws SQLException {
		if (connection == null) {
			try {
				Class.forName(configuration.getJdbcDriver());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("jdbc driver not found", e);
			}

			connection = DriverManager.getConnection(configuration.getJdbcUrl(), configuration.getJdbcUser(),
			        configuration.getJdbcPassword());
			connection.setAutoCommit(false);
		}
	}

	/**
	 * Close the connection to the database.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	protected void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			connection = null;
		}

	}

	/**
	 * Commits the current transaction.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	protected void commit() throws SQLException {
		if (connection != null) {
			connection.commit();
		}
	}

	/**
	 * Revoke the current transaction.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	protected void rollback() throws SQLException {
		if (connection != null) {
			connection.rollback();
		}
	}

	/**
	 * Returns the names of all tables in the database.
	 *
	 * @param connection
	 *            the jdbc connection object
	 *
	 * @return a {@link List} of all table names
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	protected List<String> getTableNames(final Connection connection) throws SQLException {
		ResultSet resultSet = null;

		try {
			final List<String> tables = new ArrayList<String>();

			resultSet = connection.getMetaData().getTables(null, null, "%", new String[] { "TABLE" });

			while (resultSet.next()) {
				tables.add(resultSet.getString("TABLE_NAME"));
			}
			return tables;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}

	}

	/**
	 * Execute the given sql script.
	 *
	 * @param filename
	 *            the filename of the sql script
	 * @param statement
	 *            the {@link Statement} to used for executing a SQL statement.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 * @throws NullPointerException
	 *             if the filename is null
	 */
	protected void executeScript(final String filename, final Statement statement) throws SQLException {
		LOG.info("Executing sql script: " + filename);

		final InputStream fileInputStream = ConfigurationLoader.loadResource(filename);

		final BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
		long lineNo = 0;

		StringBuffer sql = new StringBuffer();
		String line;
		try {
			while ((line = reader.readLine()) != null) {

				lineNo++;
				String trimmedLine = line.trim();

				if (trimmedLine.length() == 0 || trimmedLine.startsWith("--") || trimmedLine.startsWith("//")) {
					continue;
				} else if (trimmedLine.startsWith("/*")) {
					while ((line = reader.readLine()) != null) {
						if (line.endsWith("*/")) {
							LOG.debug("ignore " + line);
							break;
						}
					}

				} else {
					sql.append(trimmedLine);
					if (trimmedLine.endsWith(";")) {
						String sqlStatement = sql.toString();
						sqlStatement = sqlStatement.substring(0, sqlStatement.length() - 1);
						LOG.info(sqlStatement);

						statement.execute(sqlStatement);
						sql = new StringBuffer();
					}

				}
			}
		} catch (Exception e) {
			throw new SQLException("Error during import script execution at line " + lineNo, e);
		}

	}

	/**
	 * Returns the sql connection object. If there is no connection a new
	 * connection is established.
	 *
	 * @return the sql connection object
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	protected Connection getConnection() throws SQLException {
		if (connection == null) {
			openConnection();
		}

		return connection;
	}

}
