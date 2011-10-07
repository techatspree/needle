package de.akquinet.jbosscc.needle.testng;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import de.akquinet.jbosscc.needle.db.operation.DBOperation;

/**
 *
 * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase
 *
 */
public class DatabaseTestcase extends de.akquinet.jbosscc.needle.db.DatabaseTestcase {

	/**
	 * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase()
	 */
	public DatabaseTestcase() {
		super();
	}

	/**
	 * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase(Class...)
	 */
	public DatabaseTestcase(final Class<?>... clazzes) {
		super(clazzes);
	}

	/**
	 * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase(DBOperation, Class...)
	 */
	public DatabaseTestcase(final DBOperation dbOperation, final Class<?>... clazzes) {
		super(dbOperation, clazzes);
	}

	/**
	 * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase(DBOperation)
	 */
	public DatabaseTestcase(final DBOperation dbOperation) {
		super(dbOperation);
	}

	/**
	 * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase(String, DBOperation)
	 */
	public DatabaseTestcase(final String persistenceUnitName, final DBOperation dbOperation) {
		super(persistenceUnitName, dbOperation);
	}

	/**
	 * @see de.akquinet.jbosscc.needle.db.DatabaseTestcase#DatabaseTestcase(String)
	 */
	public DatabaseTestcase(final String persistenceUnitName) {
		super(persistenceUnitName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@AfterMethod
	public void after() throws Exception {
		super.after();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeforeMethod
	public void before() throws Exception {
		super.before();
	}

}
