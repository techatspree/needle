package de.akquinet.jbosscc.needle.configuration;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

/**
 * Load an holds the configuration of needle.
 *
 * Needle configuration can be defined in <b>needle.properties</b> files
 * somewhere in the classpath.
 */
public final class NeedleConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(NeedleConfiguration.class);

	private static final ConfigurationLoader CONFIGURATION_LOADER = new ConfigurationLoader();

	static final String JDBC_URL_KEY = "jdbc.url";
	static final String JDBC_DRIVER_KEY = "jdbc.driver";
	static final String JDBC_USER_KEY = "jdbc.user";
	static final String JDBC_PASSWORD_KEY = "jdbc.password";

	static final String DB_OPERATION_KEY = "db.operation";

	static final String PERSISTENCEUNIT_NAME_KEY = "persistenceUnit.name";

	static final String MOCK_PROVIDER_KEY = "mock.provider";

	static final String HIBERNATE_CFG_FILENAME_KEY = "hibernate.cfg.filename";

	static final String CUSTOM_INJECTION_ANNOTATIONS_KEY = "custom.injection.annotations";

	static final Set<Class<Annotation>> CUSTOM_INJECTION_ANNOTATIONS = lookupClasses(CUSTOM_INJECTION_ANNOTATIONS_KEY);

	static final String CUSTOM_INJECTION_PROVIDER_CLASSES_KEY = "custom.injection.provider.classes";

	@SuppressWarnings("rawtypes")
	static final Set<Class<InjectionProvider>> CUSTOM_INJECTION_PROVIDER_CLASSES = lookupClasses(CUSTOM_INJECTION_PROVIDER_CLASSES_KEY);

	static final String JDBC_URL = CONFIGURATION_LOADER.getPropertie(JDBC_URL_KEY);

	static final String JDBC_DRIVER = CONFIGURATION_LOADER.getPropertie(JDBC_DRIVER_KEY);

	static final String JDBC_USER = CONFIGURATION_LOADER.getPropertie(JDBC_USER_KEY);

	static final String JDBC_PASSWORD = CONFIGURATION_LOADER.getPropertie(JDBC_PASSWORD_KEY);

	static final String DB_OPERATION_CLASS_NAME = CONFIGURATION_LOADER.getPropertie(DB_OPERATION_KEY);

	static final String MOCK_PROVIDER_CLASS_NAME = CONFIGURATION_LOADER.getPropertie(MOCK_PROVIDER_KEY);

	static {
		StringBuilder builder = new StringBuilder();
		builder.append("\nPU_NAME=").append(getPersistenceunitName());
		builder.append("\nCFG_FILE=").append(getHibernateCfgFilename());
		builder.append("\nDB_OPERATION=").append(getDBOperationClassName());
		builder.append("\nMOCK_PROVIDER=").append(getMockProviderClassName());

		LOG.info("Needle Configuration: {}", builder.toString());
	}

	private NeedleConfiguration() {
		super();
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
	 * Returns the name of the configured hibernate.cfg file
	 *
	 * @return name of hibernate.cfg file
	 */
	public static String getHibernateCfgFilename() {
		return CONFIGURATION_LOADER.getPropertie(HIBERNATE_CFG_FILENAME_KEY);
	}

	/**
	 * Returns the configured database operation class name.
	 *
	 * @return database operation class name or null
	 */
	public static String getDBOperationClassName() {
		return DB_OPERATION_CLASS_NAME;
	}

	public static String getJdbcUrl() {
		return JDBC_URL;
	}

	public static String getJdbcDriver() {
		return JDBC_DRIVER;
	}

	public static String getJdbcUser() {
		return JDBC_USER;
	}

	public static String getJdbcPassword() {
		return JDBC_PASSWORD;
	}

	/**
	 * Returns the configured mock provider class name
	 *
	 * @return mock provider class name or null
	 */
	public static String getMockProviderClassName() {
		return MOCK_PROVIDER_CLASS_NAME;
	}

	/**
	 * Returns the configured custom {@link Annotation} classes for default mock
	 * injections.
	 *
	 * @return a {@link Set} of {@link Annotation} classes
	 */
	public static Set<Class<Annotation>> getCustomInjectionAnnotations() {
		return CUSTOM_INJECTION_ANNOTATIONS;
	}

	/**
	 * Returns the configured custom {@link InjectionProvider} classes.
	 *
	 * @return a {@link Set} of {@link InjectionProvider} classes
	 */
	@SuppressWarnings("rawtypes")
	public static Set<Class<InjectionProvider>> getCustomInjectionProviderClasses() {
		return CUSTOM_INJECTION_PROVIDER_CLASSES;
	}

	private static <T> Set<Class<T>> lookupClasses(final String key) {
		final String classesList = CONFIGURATION_LOADER.containsKey(key) ? CONFIGURATION_LOADER.getPropertie(key) : "";

		final Set<Class<T>> result = new HashSet<Class<T>>();
		final StringTokenizer tokenizer = new StringTokenizer(classesList, ",");

		String token = null;
		while (tokenizer.hasMoreElements()) {
			try {
				token = tokenizer.nextToken();

				@SuppressWarnings("unchecked")
				final Class<T> clazz = (Class<T>) ReflectionUtil.forName(token);

				if (clazz != null) {
					result.add(clazz);
				} else {
					LOG.warn("could not load class {}", token);
				}
			} catch (Exception e) {
				LOG.warn("could not load class " + token, e);
			}
		}

		return result;
	}
}
