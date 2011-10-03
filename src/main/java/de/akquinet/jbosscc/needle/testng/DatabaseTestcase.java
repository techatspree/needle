package de.akquinet.jbosscc.needle.testng;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import de.akquinet.jbosscc.needle.db.operation.DBOperation;

public class DatabaseTestcase extends de.akquinet.jbosscc.needle.db.DatabaseTestcase {

	public DatabaseTestcase() {
		super();
	}

	public DatabaseTestcase(final Class<?>... clazzes) {
		super(clazzes);
	}

	public DatabaseTestcase(final DBOperation dbOperation, final Class<?>... clazzes) {
		super(dbOperation, clazzes);
	}

	public DatabaseTestcase(final DBOperation dbOperation) {
		super(dbOperation);
	}

	public DatabaseTestcase(final String puName, final DBOperation dbOperation) {
		super(puName, dbOperation);
	}

	public DatabaseTestcase(final String puName) {
		super(puName);
	}

	@Override
	@AfterMethod
	public void after() throws Exception {
		super.after();
	}

	@Override
	@BeforeMethod
	public void before() throws Exception {
		super.before();
	}

}
