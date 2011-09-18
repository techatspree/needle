package de.akquinet.jbosscc.needle.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceConfiguration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceConfigurationFactory;
import de.akquinet.jbosscc.needle.db.dialect.AbstractDBDialect;
import de.akquinet.jbosscc.needle.db.dialect.DBDialect;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class DatabaseTestcaseConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseTestcaseConfiguration.class);

	private final DBDialect dialect;

	private PersistenceConfiguration persistenceConfiguration;

	private Class<?>[] entityClazzes;

	private String persistenceUnitName;

	private DatabaseTestcaseConfiguration() {
		super();
		dialect = createDBDialect();
	}

	public DatabaseTestcaseConfiguration(final Class<?>... clazzes) {
		this();
		Assert.assertNotNull(clazzes);
		this.entityClazzes = clazzes;

	}

	public DatabaseTestcaseConfiguration(final String persistenceUnitName) {
		this();
		Assert.assertNotNull(persistenceUnitName);
		this.persistenceUnitName = persistenceUnitName;
	}

	public EntityManager getEntityManager() {
		return createProxy(getPersistenceConfiguration().getEntityManager());
	}

	private PersistenceConfiguration getPersistenceConfiguration() {
		if (persistenceConfiguration == null) {
			if (persistenceUnitName != null) {
				persistenceConfiguration = PersistenceConfigurationFactory
				        .getPersistenceConfiguration(persistenceUnitName);
			} else {
				persistenceConfiguration = PersistenceConfigurationFactory.getPersistenceConfiguration(entityClazzes);
			}

		}

		return persistenceConfiguration;
	}

	public DBDialect getDBDialect() {
		return dialect;
	}

	private AbstractDBDialect createDBDialect() {
		final Class<? extends AbstractDBDialect> dialectClass = NeedleConfiguration.getDBDialectClass();

		if (dialectClass != null) {
			try {
				return ReflectionUtil.createInstance(dialectClass, NeedleConfiguration.getDBDialectConfiguration());
			} catch (Exception e) {
				LOG.warn("could not create a new instance of db dialect {}, {}", dialectClass, e.getMessage());
			}
		} else {
			LOG.info("no db dialect configured");
		}

		return null;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return getPersistenceConfiguration().getEntityManagerFactory();
	}

	static EntityManager createProxy(final EntityManager real) {
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
}
