package de.akquinet.jbosscc.needle.configuration;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.db.dialect.DBDialect;
import de.akquinet.jbosscc.needle.mock.MockProvider;

public class NeedleConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(NeedleConfiguration.class);

	private static final Map<String, String> CONFIG_PROPERTIES = loadResourceAndDefault("needle");

	static final String JDBC_URL_KEY = "jdbc.url";

	static final String PERSISTENCEUNIT_NAME_KEY = "persistenceUnit.name";
	static final String DB_DIALECT_KEY = "db.dialect";

	static final String MOCK_PROVIDER_KEY = "mock.provider";

	static final String HIBERNATE_CFG_FILENAME_KEY = "hibernate.cfg.filename";

	private static final String JDBC_URL = CONFIG_PROPERTIES.get(JDBC_URL_KEY);

	private static final String DEFAULT_CONFIGURATION_FILENAME = "needle-defaults";

	private static final String PERSISTENCEUNIT_NAME = CONFIG_PROPERTIES.get(PERSISTENCEUNIT_NAME_KEY);

	private static final String HIBERNATE_CFG_FILENAME = CONFIG_PROPERTIES.get(HIBERNATE_CFG_FILENAME_KEY);

	private static final Class<? extends DBDialect> DB_DIALECT_CLASS = lookupDBDialectClass();

	private static final Class<? extends MockProvider> MOCK_PROVIDER_CLASS = lookupMockProviderClass();

	static {
		StringBuilder builder = new StringBuilder();
		builder.append("\nJDBC_URL=").append(getJdbcUrl());
		builder.append("\nPU_NAME=").append(getPersistenceunitName());
		builder.append("\nCFG_FILE=").append(getHibernateCfgFilename());
		builder.append("\nDB_DIALECT=").append(getDBDialectClass());
		builder.append("\nMOCK_PROVIDER=").append(getMockProviderClass());

		LOG.info("Needle Configuration: {}", builder.toString());
	}

	private NeedleConfiguration() {
		super();
	}

	static final Map<String, String> loadResourceAndDefault(String name) {
		final Map<String, String> result = new HashMap<String, String>();

		try {
			ResourceBundle customBundle = loadResourceBundle(name);

			for (Enumeration<String> keys = customBundle.getKeys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				result.put(key, customBundle.getString(key));
			}

			URL url = NeedleConfiguration.class.getResource("/" + name + ".properties");
			LOG.debug("loaded Needle config named {} from {}", name, url);
		} catch (Exception e) {
			LOG.debug("found no custom configuration");
		}

		try {

			ResourceBundle defaultResourceBundle = loadResourceBundle(DEFAULT_CONFIGURATION_FILENAME);

			for (Enumeration<String> keys = defaultResourceBundle.getKeys(); keys.hasMoreElements();) {
				String key = keys.nextElement();
				if (!result.containsKey(key)) {
					result.put(key, defaultResourceBundle.getString(key));
				}

			}

			URL url = NeedleConfiguration.class.getResource("/" + DEFAULT_CONFIGURATION_FILENAME + ".properties");
			LOG.debug("loaded default Needle config from: {}", url);
		} catch (Exception e1) {
			LOG.error("should never happen", e1);

			throw new AssertionError("should never happen");
		}

		return result;
	}

	private static ResourceBundle loadResourceBundle(String name) {
		return ResourceBundle.getBundle(name);
	}

	public static String getJdbcUrl() {
		return JDBC_URL;
	}

	public static String getPersistenceunitName() {
		return PERSISTENCEUNIT_NAME;
	}

	public static String getHibernateCfgFilename() {
		return HIBERNATE_CFG_FILENAME;
	}

	public static Class<? extends DBDialect> getDBDialectClass() {
		return DB_DIALECT_CLASS;
	}

	public static Class<? extends MockProvider> getMockProviderClass() {
		return MOCK_PROVIDER_CLASS;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends DBDialect> lookupDBDialectClass() {
		final String dbDialect = CONFIG_PROPERTIES.containsKey(DB_DIALECT_KEY) ? CONFIG_PROPERTIES.get(DB_DIALECT_KEY)
		        : null;

		try {
			if (dbDialect != null) {

				return (Class<? extends DBDialect>) Class.forName(dbDialect);
			}

		} catch (Exception e) {
			LOG.warn("could not create db dialect {} {}", dbDialect, e.getMessage());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends MockProvider> lookupMockProviderClass() {
		final String mockProvider = CONFIG_PROPERTIES.containsKey(MOCK_PROVIDER_KEY) ? CONFIG_PROPERTIES
		        .get(MOCK_PROVIDER_KEY) : null;

		try {
			if (mockProvider != null) {

				return (Class<? extends MockProvider>) Class.forName(mockProvider);
			}

		} catch (Exception e) {
			LOG.warn("could not create mock provider {} {}", mockProvider, e.getMessage());
		}

		return null;
	}
}
