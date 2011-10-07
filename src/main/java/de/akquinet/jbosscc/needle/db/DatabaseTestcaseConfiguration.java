package de.akquinet.jbosscc.needle.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceConfiguration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceConfigurationFactory;
import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;
import de.akquinet.jbosscc.needle.db.operation.DBOperation;
import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

class DatabaseTestcaseConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseTestcaseConfiguration.class);

	/**
	 * The name of a JDBC driver key to use to connect to the database.
	 */
	private static final String JDBC_DRIVER_KEY = "javax.persistence.jdbc.driver";

	/**
	 * The JDBC connection url key to use to connect to the database.
	 */
	private static final String JDBC_URL_KEY = "javax.persistence.jdbc.url";

	/**
	 * The JDBC connection user name key.
	 */
	private static final String JDBC_USER_KEY = "javax.persistence.jdbc.user";

	/**
	 * The JDBC connection password key.
	 */
	private static final String JDBC_PASSWORD_KEY = "javax.persistence.jdbc.password";

	private final AbstractDBOperation dbOperation;

	private final PersistenceUnitConfiguratiorn configuratiorn;

	private DatabaseTestcaseConfiguration(PersistenceUnitConfiguratiorn configuratiorn) {
		this.configuratiorn = configuratiorn;
		dbOperation = createDBOperation();
	}

	DatabaseTestcaseConfiguration(final Class<?>... clazzes) {
		this(new PersistenceUnitConfiguratiorn(clazzes));

	}

	DatabaseTestcaseConfiguration(final String persistenceUnitName) {
		this(new PersistenceUnitConfiguratiorn(persistenceUnitName));

	}

	DatabaseTestcaseConfiguration() {
		this(NeedleConfiguration.getPersistenceunitName());
	}

	EntityManager getEntityManager() {
		return configuratiorn.getEntityManager();
	}

	DBOperation getDBOperation() {
		return dbOperation;
	}

	private AbstractDBOperation createDBOperation() {
		final Class<? extends AbstractDBOperation> dbOperationClass = NeedleConfiguration.getDBOperationClass();

		if (dbOperationClass != null) {
			try {
				return ReflectionUtil.createInstance(dbOperationClass, getJdbcComfiguration());
			} catch (Exception e) {
				LOG.warn("could not create a new instance of db dialect {}, {}", dbOperationClass, e.getMessage());
			}
		} else {
			LOG.info("no db operation configured");
		}

		return null;
	}

	private JdbcConfiguration getJdbcComfiguration() throws Exception {
		if (NeedleConfiguration.getJdbcDriver() != null && NeedleConfiguration.getJdbcUrl() != null) {
			return new JdbcConfiguration(NeedleConfiguration.getJdbcUrl(), NeedleConfiguration.getJdbcDriver(),
			        NeedleConfiguration.getJdbcUser(), NeedleConfiguration.getJdbcPassword());
		}

		return getEntityManagerFactoryProperties();

	}

	private JdbcConfiguration getEntityManagerFactoryProperties() throws Exception {
		try {
			final Map<String, Object> properties = getEntityManagerFactory().getProperties();

			return new JdbcConfiguration((String) properties.get(JDBC_URL_KEY),
			        (String) properties.get(JDBC_DRIVER_KEY), (String) properties.get(JDBC_USER_KEY),
			        (String) properties.get(JDBC_PASSWORD_KEY));
		} catch (Exception e) {
			throw new Exception("error while loading jdbc configuration properties form EntityManagerFactory", e);
		}
	}

	EntityManagerFactory getEntityManagerFactory() {
		return configuratiorn.getEntityManagerFactory();
	}

}

class PersistenceUnitConfiguratiorn implements PersistenceConfiguration {

	private PersistenceConfiguration persistenceConfiguration;

	private String persistenceUnit;
	private Class<?>[] cfgClazzes;

	public PersistenceUnitConfiguratiorn(final String persistenceUnit) {
		super();
		this.persistenceUnit = persistenceUnit;
	}

	public PersistenceUnitConfiguratiorn(final Class<?>[] cfgClazzes) {
		super();
		this.cfgClazzes = cfgClazzes;
	}

	private PersistenceConfiguration getPersistenceConfiguration() {
		if (persistenceConfiguration == null) {
			if (persistenceUnit != null) {
				persistenceConfiguration = PersistenceConfigurationFactory.getPersistenceConfiguration(persistenceUnit);
			} else if (cfgClazzes != null) {
				persistenceConfiguration = PersistenceConfigurationFactory.getPersistenceConfiguration(cfgClazzes);
			} else {
				persistenceConfiguration = null;
			}
		}

		return persistenceConfiguration;
	}

	private static EntityManager createProxy(final EntityManager real) {
		return (EntityManager) Proxy.newProxyInstance(DatabaseTestcaseConfiguration.class.getClassLoader(),
		        new Class[] { EntityManager.class }, new InvocationHandler() {
			        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				        if (method.getName().equals("close")) {
					        throw new RuntimeException("you are not allowed to explicitely close this EntityManager");
				        }

				        try {
					        return method.invoke(real, args);
				        } catch (InvocationTargetException e) {
					        throw e.getCause();
				        }
			        }
		        });
	}

	@Override
	public EntityManager getEntityManager() {
		return createProxy(getPersistenceConfiguration().getEntityManager());
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return getPersistenceConfiguration().getEntityManagerFactory();
	}

}
