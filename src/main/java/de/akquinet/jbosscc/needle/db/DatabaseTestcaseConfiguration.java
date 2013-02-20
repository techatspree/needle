package de.akquinet.jbosscc.needle.db;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceConfigurationFactory;
import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;
import de.akquinet.jbosscc.needle.db.operation.DBOperation;
import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

final class DatabaseTestcaseConfiguration {

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

    private final NeedleConfiguration needleConfiguration = NeedleConfiguration.get();

    private final AbstractDBOperation dbOperation;

    private final PersistenceConfigurationFactory configuration;

    private DatabaseTestcaseConfiguration(final PersistenceConfigurationFactory configuratiorn) {
        this.configuration = configuratiorn;
        this.dbOperation = createDBOperation(lookupDBOperationClass(needleConfiguration.getDBOperationClassName()));
    }

    DatabaseTestcaseConfiguration(final Class<?>... clazzes) {
        this(new PersistenceConfigurationFactory(clazzes));

    }

    DatabaseTestcaseConfiguration(final String persistenceUnitName) {
        this(new PersistenceConfigurationFactory(persistenceUnitName));

    }

    DatabaseTestcaseConfiguration() {
        this(NeedleConfiguration.get().getPersistenceunitName());
    }

    EntityManager getEntityManager() {
        return configuration.getEntityManager();
    }

    EntityManagerFactory getEntityManagerFactory() {
        return configuration.getEntityManagerFactory();
    }

    DBOperation getDBOperation() {
        return dbOperation;
    }

    AbstractDBOperation createDBOperation(final Class<? extends AbstractDBOperation> dbOperationClass) {

        if (dbOperationClass != null) {
            try {
                return ReflectionUtil.createInstance(dbOperationClass, getJdbcComfiguration());
            }
            catch (final Exception e) {
                LOG.warn("could not create a new instance of configured db operation {}, {}", dbOperationClass, e.getMessage());
                LOG.debug(e.getMessage(), e);
            }
        }
        else {
            LOG.info("no db operation configured");
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    static Class<? extends AbstractDBOperation> lookupDBOperationClass(final String dbOperation) {
        try {
            if (dbOperation != null) {
                return (Class<? extends AbstractDBOperation>)Class.forName(dbOperation);
            }

        }
        catch (final Exception e) {
            LOG.warn("error while loading db operation class {}, {}", dbOperation, e.getMessage());
        }

        return null;
    }

    private JdbcConfiguration getJdbcComfiguration() throws Exception {
        if (needleConfiguration.getJdbcDriver() != null && needleConfiguration.getJdbcUrl() != null) {
            return new JdbcConfiguration(needleConfiguration.getJdbcUrl(),
                    needleConfiguration.getJdbcDriver(),
                    needleConfiguration.getJdbcUser(),
                    needleConfiguration.getJdbcPassword());
        }

        return getEntityManagerFactoryProperties();

    }

    private JdbcConfiguration getEntityManagerFactoryProperties() throws Exception {
        try {
            final Map<String, Object> properties = getEntityManagerFactory().getProperties();

            return new JdbcConfiguration((String)properties.get(JDBC_URL_KEY),
                    (String)properties.get(JDBC_DRIVER_KEY), (String)properties.get(JDBC_USER_KEY),
                    (String)properties.get(JDBC_PASSWORD_KEY));
        }
        catch (final Exception e) {
            throw new Exception("error while loading jdbc configuration properties form EntityManagerFactory", e);
        }
    }

}
