package de.akquinet.jbosscc.needle.configuration;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.db.dialect.AbstractDBDialect;
import de.akquinet.jbosscc.needle.db.dialect.DBDialect;
import de.akquinet.jbosscc.needle.db.dialect.DBDialectConfiguration;
import de.akquinet.jbosscc.needle.mock.MockProvider;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public final class NeedleConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(NeedleConfiguration.class);

	private static final ConfigurationLoader CONFIGURATION_LOADER = new ConfigurationLoader();

	static final String JDBC_URL_KEY = "jdbc.url";
	static final String JDBC_DRIVER_KEY = "jdbc.driver";
	static final String JDBC_USER_KEY = "jdbc.user";
	static final String JDBC_PASSWORD_KEY = "jdbc.password";

	static final String DB_DIALECT_KEY = "db.dialect";

	static final String PERSISTENCEUNIT_NAME_KEY = "persistenceUnit.name";

	static final String MOCK_PROVIDER_KEY = "mock.provider";

	static final String HIBERNATE_CFG_FILENAME_KEY = "hibernate.cfg.filename";

	static final String CUSTOM_INJECTION_ANNOTATIONS_KEY = "custom.injection.annotations";

	static final Set<Class<? extends Annotation>> CUSTOM_INJECTION_ANNOTATIONS = lookupCustomInjectionAnnotations();

	static final String CUSTOM_INJECTION_PROVIDERS_KEY = "custom.injection.providers";

	static final DBDialectConfiguration DB_DIALECT_CONFIGURATION = new DBDialectConfiguration(
	        CONFIGURATION_LOADER.getPropertie(JDBC_URL_KEY), CONFIGURATION_LOADER.getPropertie(JDBC_DRIVER_KEY),
	        CONFIGURATION_LOADER.getPropertie(JDBC_USER_KEY), CONFIGURATION_LOADER.getPropertie(JDBC_PASSWORD_KEY));

	static final Class<? extends AbstractDBDialect> DB_DIALECT_CLASS = lookupDBDialectClass(CONFIGURATION_LOADER
	        .getPropertie(DB_DIALECT_KEY));

	static final Class<? extends MockProvider> MOCK_PROVIDER_CLASS = lookupMockProviderClass();

	static {
		StringBuilder builder = new StringBuilder();
		builder.append("\nPU_NAME=").append(getPersistenceunitName());
		builder.append("\nCFG_FILE=").append(getHibernateCfgFilename());
		builder.append("\nDB_DIALECT=").append(getDBDialectClass());
		builder.append("\nMOCK_PROVIDER=").append(getMockProviderClass());
		builder.append("\nJDBC_URL=").append(DB_DIALECT_CONFIGURATION.getJdbcUrl());
		builder.append("\nJDBC_DRIVER=").append(DB_DIALECT_CONFIGURATION.getJdbcDriver());

		LOG.info("Needle Configuration: {}", builder.toString());
	}

	private NeedleConfiguration() {
		super();
	}

	public static DBDialectConfiguration getDBDialectConfiguration() {
		return DB_DIALECT_CONFIGURATION;
	}

	/**
	 * Returns the configured jpa persistence unit name.
	 *
	 * @return jpa persistence unit name
	 */
	public static String getPersistenceunitName() {
		return CONFIGURATION_LOADER.getPropertie(PERSISTENCEUNIT_NAME_KEY);
	}

	/**
	 * Returns the configured hibernate specific configuration file
	 *
	 * @return hibernate cfg file
	 */
	public static String getHibernateCfgFilename() {
		return CONFIGURATION_LOADER.getPropertie(HIBERNATE_CFG_FILENAME_KEY);
	}

	/**
	 * Returns the configured {@link DBDialect}
	 *
	 * @return {@link DBDialect}
	 */
	public static Class<? extends AbstractDBDialect> getDBDialectClass() {
		return DB_DIALECT_CLASS;
	}

	/**
	 * Returns the configured {@link MockProvider}
	 *
	 * @return {@link MockProvider}
	 */
	public static Class<? extends MockProvider> getMockProviderClass() {
		return MOCK_PROVIDER_CLASS;
	}

	/**
	 * Returns the configured custom {@link Annotation} classes for default mock
	 * injections.
	 *
	 * @return a {@link Set} of {@link Annotation} classes
	 */
	public static Set<Class<? extends Annotation>> getCustomInjectionAnnotations() {
		return CUSTOM_INJECTION_ANNOTATIONS;
	}

	@SuppressWarnings("unchecked")
	static Class<? extends AbstractDBDialect> lookupDBDialectClass(final String dbDialect) {

		try {
			if (dbDialect != null) {
				return (Class<? extends AbstractDBDialect>) Class.forName(dbDialect);
			}

		} catch (Exception e) {
			LOG.warn("error while loading db dialect class {}, {}", dbDialect, e.getMessage());
		}

		return null;
	}

	private static Set<Class<? extends Annotation>> lookupCustomInjectionAnnotations() {
		final String customAnnotationList = CONFIGURATION_LOADER.containsKey(CUSTOM_INJECTION_ANNOTATIONS_KEY) ? CONFIGURATION_LOADER
		        .getPropertie(CUSTOM_INJECTION_ANNOTATIONS_KEY) : "";
		final Set<Class<? extends Annotation>> customInjectionAnnotations = new HashSet<Class<? extends Annotation>>();
		final StringTokenizer tokenizer = new StringTokenizer(customAnnotationList, ",");

		String token = null;
		while (tokenizer.hasMoreElements()) {
			try {
				token = tokenizer.nextToken();

				@SuppressWarnings("unchecked")
				final Class<? extends Annotation> customAnnotation = (Class<? extends Annotation>) ReflectionUtil
				        .forName(token);

				if (customAnnotation != null) {
					customInjectionAnnotations.add(customAnnotation);
				} else {
					LOG.warn("no annotation class found for {}", token);
				}
			} catch (Exception e) {
				LOG.warn("could not load annotation " + token, e);
			}
		}

		return customInjectionAnnotations;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends MockProvider> lookupMockProviderClass() {
		final String mockProvider = CONFIGURATION_LOADER.containsKey(MOCK_PROVIDER_KEY) ? CONFIGURATION_LOADER
		        .getPropertie(MOCK_PROVIDER_KEY) : null;

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
