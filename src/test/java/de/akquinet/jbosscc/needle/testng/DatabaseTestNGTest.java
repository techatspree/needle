package de.akquinet.jbosscc.needle.testng;

import org.junit.Assert;
import org.testng.annotations.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.db.Person;

public class DatabaseTestNGTest extends DatabaseTestcase {

	public DatabaseTestNGTest() {
		super(Person.class, Address.class);
	}

	@Test
	public void testGetDBAccess() throws Exception {
		Assert.assertNotNull(getEntityManagerFactory());
		Assert.assertNotNull(getEntityManager());
	}

}
