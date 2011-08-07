package de.akquinet.jbosscc.needle.db.dialect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;

public abstract class AbstractDBDialect implements DBDialect {

	private Connection connection;

	@Override
	public void openConnection() throws SQLException {
		connection = DriverManager.getConnection(NeedleConfiguration
				.getJdbcUrl());
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
