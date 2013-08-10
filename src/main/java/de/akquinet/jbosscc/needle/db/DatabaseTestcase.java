package de.akquinet.jbosscc.needle.db;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.configuration.PropertyBasedConfigurationFactory;
import de.akquinet.jbosscc.needle.db.operation.DBOperation;
import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;

/**
 * Base class for a database test case. Executes optional database operation on
 * test setup and tear down.
 * 
 * May be used as an injection provider for {@link EntityManager},
 * {@link EntityManagerFactory} and {@link EntityTransaction}.
 * 
 * @see InjectionProvider
 * @see DBOperation
 * 
 */
public class DatabaseTestcase implements InjectionProvider<Object> {

    private final DatabaseTestcaseConfiguration configuration;

    private TransactionHelper transactionHelper;

    private DBOperation dbOperation;

    private Map<Class<?>, InjectionProvider<?>> injectionProviderMap = new HashMap<Class<?>, InjectionProvider<?>>();

    {
        injectionProviderMap.put(EntityManager.class, new EntityManagerProvider(this));
        injectionProviderMap.put(EntityManagerFactory.class, new EntityManagerFactoryProvider(this));
        injectionProviderMap.put(EntityTransaction.class, new EntityTransactionProvider(this));
        injectionProviderMap.put(TransactionHelper.class, new TransactionHelperProvider(this));
    }

    /**
     * Creates an instance of {@link DatabaseTestcase} with the global
     * configured persistence unit name and the global configured
     * {@link DBOperation}.
     * 
     * @see DBOperation
     */
    public DatabaseTestcase() {

        configuration = new DatabaseTestcaseConfiguration(PropertyBasedConfigurationFactory.get());
    }

    /**
     * Creates an instance of {@link DatabaseTestcase} with the global
     * configured persistence unit name and overrides the global configured
     * {@link DBOperation} with the given database operation.
     * 
     * @param dbOperation
     *            database operation to execute on test setup and tear down
     * 
     * @see DBOperation
     */
    public DatabaseTestcase(final DBOperation dbOperation) {
        this();
        this.dbOperation = dbOperation;
    }

    /**
     * Creates an instance of {@link DatabaseTestcase} with the given
     * persistence unit name and the global configured {@link DBOperation}.
     * 
     * @param persistenceUnitName
     *            the name of the persistence unit
     * 
     * @see DBOperation
     */
    public DatabaseTestcase(final String persistenceUnitName) {
        configuration = new DatabaseTestcaseConfiguration(PropertyBasedConfigurationFactory.get(), persistenceUnitName);
    }

    /**
     * Creates an instance of {@link DatabaseTestcase} with the given
     * persistence unit name and overrides the global configured
     * {@link DBOperation} with the given database operation.
     * 
     * @param persistenceUnitName
     *            the name of the persistence unit
     * @param dbOperation
     *            database operation to execute on test setup and tear down
     * 
     * @see DBOperation
     */
    public DatabaseTestcase(final String persistenceUnitName, final DBOperation dbOperation) {
        this(persistenceUnitName);
        this.dbOperation = dbOperation;
    }

    /**
     * Creates an instance of {@link DatabaseTestcase} for the given entity
     * classes by using the configured hibernate specific configuration file
     * (*cfg.xml) and use the global configured {@link DBOperation}.
     * 
     * @param clazzes
     *            the entity classes
     * @see DBOperation
     * 
     */
    @Deprecated
    public DatabaseTestcase(final Class<?>... clazzes) {
        configuration = new DatabaseTestcaseConfiguration(PropertyBasedConfigurationFactory.get(), clazzes);
    }

    /**
     * Creates an instance of {@link DatabaseTestcase} for the given entity
     * classes by using the configured hibernate specific configuration file
     * (*cfg.xml) and overrides the global configured {@link DBOperation} with
     * the give database operation.
     * 
     * @param dbOperation
     *            database operation to execute on test setup and tear down
     * @param clazzes
     *            the entity classes
     * 
     * @see DBOperation
     */
    @Deprecated
    public DatabaseTestcase(final DBOperation dbOperation, final Class<?>... clazzes) {
        this(clazzes);
        this.dbOperation = dbOperation;
    }

    protected DatabaseTestcase(final NeedleConfiguration needleConfiguration) {
        configuration = new DatabaseTestcaseConfiguration(needleConfiguration,
                needleConfiguration.getPersistenceunitName());

    }

    /**
     * Execute tear down database operation, if configured.
     * 
     * @throws Exception
     *             thrown if an error occurs
     */
    protected void after() throws Exception {
        final DBOperation operation = getDBOperation();

        if (operation != null) {
            operation.tearDownOperation();
        }

        getEntityManager().clear();
    }

    /**
     * Execute setup database operation, if configured.
     * 
     * @throws Exception
     *             thrown if an error occurs
     */
    protected void before() throws Exception {
        final DBOperation operation = getDBOperation();

        if (operation != null) {
            operation.setUpOperation();
        }
    }

    private DBOperation getDBOperation() {
        return dbOperation != null ? dbOperation : configuration.getDBOperation();
    }

    /**
     * Returns the {@link EntityManager}.
     * 
     * @return {@link EntityManager}.
     */
    public EntityManager getEntityManager() {
        return configuration.getEntityManager();
    }

    /**
     * Returns the {@link EntityManagerFactory}.
     * 
     * @return {@link EntityManagerFactory}.
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return configuration.getEntityManagerFactory();
    }

    /**
     * Returns an instance of {@link TransactionHelper}
     * 
     * @see TransactionHelper
     * @return {@link TransactionHelper}
     */
    public TransactionHelper getTransactionHelper() {
        if (transactionHelper == null) {
            transactionHelper = new TransactionHelper(getEntityManager());
        }

        return transactionHelper;
    }

    @Override
    public Object getInjectedObject(final Class<?> injectionPointType) {
        return getInjectionProvider(injectionPointType).getInjectedObject(injectionPointType);
    }

    @Override
    public boolean verify(final InjectionTargetInformation injectionTargetInformation) {
        final InjectionProvider<?> injectionProvider = getInjectionProvider(injectionTargetInformation.getType());
        return injectionProvider != null && injectionProvider.verify(injectionTargetInformation);
    }

    @Override
    public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
        return getInjectionProvider(injectionTargetInformation.getType()).getKey(injectionTargetInformation);
    }

    private InjectionProvider<?> getInjectionProvider(final Class<?> type) {
        return injectionProviderMap.get(type);
    }
}
