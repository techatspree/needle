package de.akquinet.jbosscc.needle.db.operation;

/**
 * JDBC configuration properties.
 *
 */
public class JdbcConfiguration {

	private final String jdbcUrl;
	private final String jdbcDriver;
	private final String jdbcUser;
	private final String jdbcPassword;

	/**
	 * Create an instance of {@link JdbcConfiguration}
	 *
	 * @param jdbcUrl
	 *            the JDBC connection url to use to connect to the database
	 * @param jdbcDriver
	 *            the name of a JDBC driver to use to connect to the database
	 * @param jdbcUser
	 *            the JDBC connection user name
	 * @param jdbcPassword
	 *            the JDBC connection password
	 */
	public JdbcConfiguration(final String jdbcUrl, final String jdbcDriver, final String jdbcUser,
	        final String jdbcPassword) {
		super();
		this.jdbcUrl = jdbcUrl;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUser = jdbcUser;
		this.jdbcPassword = jdbcPassword;
	}

	/**
	 * Returns the JDBC connection url to use to connect to the database
	 *
	 * @return the JDBC connection url
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * Return the name of a JDBC driver to use to connect to the database.
	 *
	 * @return the jdbc driver name
	 */
	public String getJdbcDriver() {
		return jdbcDriver;
	}

	/**
	 * Returns the user name to use to connect to the database.
	 *
	 * @return the jdbc user name
	 *
	 */
	public String getJdbcUser() {
		return jdbcUser;
	}

	/**
	 * Returns the password to use to connect to the database.
	 *
	 * @return the JDBC connection password
	 */
	public String getJdbcPassword() {
		return jdbcPassword;
	}

}
