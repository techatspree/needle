package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import de.akquinet.jbosscc.needle.db.operation.DBOperation;
import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;

/**
 * Base class for a database test case. Execute optional database operation on
 * test setup and tear down.
 *
 * May used as injection provider for {@link EntityManager} and
 * {@link EntityManagerFactory}.
 *
 * @see InjectionProvider
 * @see DBOperation
 *
 */
public class DatabaseTestcase implements InjectionProvider<Object> {

	private final DatabaseTestcaseConfiguration configuration;

	private TransactionHelper transactionHelper;

	private DBOperation dbOperation;

	private EntityManagerProvider entityManagerProvider = new EntityManagerProvider(this);
	private EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider(this);

	/**
	 * Create an instance of {@link DatabaseTestcase} with the global configured
	 * persistence unit name and the global configured {@link DBOperation}.
	 *
	 * @see DBOperation
	 */
	public DatabaseTestcase() {
		configuration = new DatabaseTestcaseConfiguration();
	}

	/**
	 * Create an instance of {@link DatabaseTestcase} with the global configured
	 * persistence unit name and override the global configured
	 * {@link DBOperation} with the give database operation.
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
	 * Create an instance of {@link DatabaseTestcase} with the given persistence
	 * unit name and the global configured {@link DBOperation}.
	 *
	 * @param persistenceUnitName
	 *            the name of the persistence unit
	 *
	 * @see DBOperation
	 */
	public DatabaseTestcase(final String persistenceUnitName) {
		configuration = new DatabaseTestcaseConfiguration(persistenceUnitName);

	}

	/**
	 * Create an instance of {@link DatabaseTestcase} with the given persistence
	 * unit name and override the global configured {@link DBOperation} with the
	 * give database operation.
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
	 * Create an instance of {@link DatabaseTestcase} for the given entity
	 * classes by using the configured hibernate specific configuration file
	 * (*cfg.xml) and use the global configured {@link DBOperation}.
	 *
	 * @param clazzes
	 *            the entity classes
	 * @see DBOperation
	 *
	 */
	public DatabaseTestcase(final Class<?>... clazzes) {
		configuration = new DatabaseTestcaseConfiguration(clazzes);
	}

	/**
	 * Create an instance of {@link DatabaseTestcase} for the given entity
	 * classes by using the configured hibernate specific configuration file
	 * (*cfg.xml) and override the global configured {@link DBOperation} with
	 * the give database operation.
	 *
	 * @param dbOperation
	 *            database operation to execute on test setup and tear down
	 * @param clazzes
	 *            the entity classes
	 *
	 * @see DBOperation
	 */
	public DatabaseTestcase(final DBOperation dbOperation, final Class<?>... clazzes) {
		this(clazzes);
		this.dbOperation = dbOperation;
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
		return getInjectionProvider(injectionTargetInformation.getType()).verify(injectionTargetInformation);
	}

	@Override
	public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
		return getInjectionProvider(injectionTargetInformation.getType()).getKey(injectionTargetInformation);
	}

	private InjectionProvider<?> getInjectionProvider(final Class<?> type) {
		return type == EntityManager.class ? entityManagerProvider : entityManagerFactoryProvider;
	}
}
