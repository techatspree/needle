package de.akquinet.jbosscc.example;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.transaction.VoidRunnable;
import de.akquinet.jbosscc.needle.example.Address;
import de.akquinet.jbosscc.needle.example.Person;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.DatabaseRuleBuilder;

public class AddressTest {

	@Rule
	public DatabaseRule databaseRule = new DatabaseRuleBuilder().build();   

	
	private EntityManager entityManager = databaseRule.getEntityManager();

	@Test
	public void testPersist() throws Exception {
		final Address address = new AddressTestdataBuilder(entityManager)
				.build();

		// execute in transaction
		databaseRule.getTransactionHelper().executeInTransaction(
				new VoidRunnable() {
					@Override
					public void doRun(EntityManager entityManager)
							throws Exception {
						entityManager.persist(address);

					}
				});

		Address addressFromDB = entityManager.find(Address.class,
				address.getId());

		Assert.assertNotNull(addressFromDB);
		Assert.assertNotNull(addressFromDB.getPerson());
		Assert.assertNotSame(address, addressFromDB);
	}

	@Test
	public void testPersist_withPerson() throws Exception {
		Person person = new PersonTestdataBuilder(entityManager).buildAndSave();
		new AddressTestdataBuilder(entityManager).withPerson(person)
				.buildAndSave();
		new AddressTestdataBuilder(entityManager).withPerson(person)
				.buildAndSave();
		new AddressTestdataBuilder(entityManager).withPerson(person)
				.buildAndSave();

		Query query = entityManager
				.createQuery("select address from Address address where address.person = :person");

		query.setParameter("person", person);

		Assert.assertEquals(3, query.getResultList().size());
	}
}
