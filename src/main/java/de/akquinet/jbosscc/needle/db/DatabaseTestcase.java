package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import de.akquinet.jbosscc.needle.db.operation.DBOperation;
import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;

public class DatabaseTestcase implements InjectionProvider<Object> {

	private final DatabaseTestcaseConfiguration configuration;

	private TransactionHelper transactionHelper;

	private DBOperation dbOperation;

	private EntityManagerProvider entityManagerProvider = new EntityManagerProvider(this);
	private EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider(this);

	public DatabaseTestcase() {
		configuration = new DatabaseTestcaseConfiguration();

	}

	public DatabaseTestcase(final DBOperation dbOperation) {
		this();
		this.dbOperation = dbOperation;
	}

	public DatabaseTestcase(final String puName) {
		configuration = new DatabaseTestcaseConfiguration(puName);

	}

	public DatabaseTestcase(final String puName, final DBOperation dbOperation) {
		this(puName);
		this.dbOperation = dbOperation;
	}

	public DatabaseTestcase(final Class<?>... clazzes) {
		configuration = new DatabaseTestcaseConfiguration(clazzes);
	}

	public DatabaseTestcase(final DBOperation dbOperation, final Class<?>... clazzes) {
		this(clazzes);
		this.dbOperation = dbOperation;
	}

	protected void after() throws Exception {
		final DBOperation dbOperation = getDBOperation();

		if (dbOperation != null) {
			dbOperation.tearDownOperation();
		}
	}

	protected void before() throws Exception {
		final DBOperation dbOperation = getDBOperation();

		if (dbOperation != null) {
			dbOperation.setUpOperation();
		}
	}

	private DBOperation getDBOperation() {
		return dbOperation != null ? dbOperation : configuration.getDBOperation();
	}

	public EntityManager getEntityManager() {
		return configuration.getEntityManager();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return configuration.getEntityManagerFactory();
	}

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
