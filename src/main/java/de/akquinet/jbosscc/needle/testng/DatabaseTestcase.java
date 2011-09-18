package de.akquinet.jbosscc.needle.testng;

import org.testng.annotations.AfterMethod;

public class DatabaseTestcase extends de.akquinet.jbosscc.needle.db.DatabaseTestcase {

	public DatabaseTestcase() {
		super();
	}

	public DatabaseTestcase(Class<?>... clazzes) {
		super(clazzes);
	}

	public DatabaseTestcase(String puName) {
		super(puName);
	}

	@Override
	@AfterMethod
	public void after() {
		super.after();
	}
	
	
	

}
