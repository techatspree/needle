package de.akquinet.jbosscc.needle.db.configuration;

import org.junit.Assert;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.db.Person;

public class PersistenceConfigurationFactoryTest {

	@Test
	public void testEqualsWithClasses() throws Exception {
		Class<?>[] personClazzes = { Person.class, Address.class };
		PersistenceConfigurationFactory persistenceConfigurationFactory1 = new PersistenceConfigurationFactory(
		        personClazzes);

		Class<?>[] clazzes2 = { Person.class, Address.class };
		PersistenceConfigurationFactory persistenceConfigurationFactory2 = new PersistenceConfigurationFactory(clazzes2);

		Assert.assertSame(persistenceConfigurationFactory1.getEntityManagerFactory(),
		        persistenceConfigurationFactory2.getEntityManagerFactory());

		Assert.assertSame(persistenceConfigurationFactory1.getEntityManager(),
		        persistenceConfigurationFactory2.getEntityManager());
	}

	@Test
	public void testEqualsWithPersistenceUnitName() throws Exception {
		PersistenceConfigurationFactory persistenceConfigurationFactory1 = new PersistenceConfigurationFactory(
		        "TestDataModel");

		PersistenceConfigurationFactory persistenceConfigurationFactory2 = new PersistenceConfigurationFactory(
		        "TestDataModel");

		Assert.assertSame(persistenceConfigurationFactory1.getEntityManagerFactory(),
		        persistenceConfigurationFactory1.getEntityManagerFactory());

		Assert.assertSame(persistenceConfigurationFactory1.getEntityManager(),
		        persistenceConfigurationFactory2.getEntityManager());
	}
}
