package de.akquinet.jbosscc.needle.db.dialect;

/**
 * JDBC configuration properties.
 *
 */
public class DBDialectConfiguration {

	private final String jdbcUrl;
	private final String jdbcDriver;
	private final String jdbcUser;
	private final String jdbcPassword;

	public DBDialectConfiguration(final String jdbcUrl, final String jdbcDriver, final String jdbcUser,
	        final String jdbcPassword) {
		super();
		this.jdbcUrl = jdbcUrl;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUser = jdbcUser;
		this.jdbcPassword = jdbcPassword;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getJdbcUser() {
		return jdbcUser;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

}
