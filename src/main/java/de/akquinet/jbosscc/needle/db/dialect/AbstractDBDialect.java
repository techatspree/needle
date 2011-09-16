package de.akquinet.jbosscc.needle.db.dialect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDBDialect implements DBDialect {

	private final DBDialectConfiguration configuration;

	public AbstractDBDialect(final DBDialectConfiguration configuration) {
		super();
		this.configuration = configuration;

	}

	private Connection connection;

	@Override
	public void openConnection() throws SQLException {
		try {
			Class.forName(configuration.getJdbcDriver());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("jdbc driver not found", e);
		}

		connection = DriverManager.getConnection(configuration.getJdbcUrl(), configuration.getJdbcUser(),
		        configuration.getJdbcPassword());
		connection.setAutoCommit(false);

	}

	@Override
	public void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			connection = null;
		}

	}

	@Override
	public void commit() throws SQLException {
		if (connection != null) {
			connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException {
		if (connection != null) {
			connection.rollback();
		}
	}

	protected Connection getConnection() throws SQLException {
		if (connection == null) {
			openConnection();
		}

		return connection;
	}

}
