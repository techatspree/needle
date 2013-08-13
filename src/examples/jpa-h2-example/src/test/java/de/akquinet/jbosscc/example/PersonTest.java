package de.akquinet.jbosscc.example;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.operation.h2.H2DeleteOperation;
import de.akquinet.jbosscc.needle.db.transaction.VoidRunnable;
import de.akquinet.jbosscc.needle.example.Person;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.DatabaseRuleBuilder;

public class PersonTest {

	@Rule
	public DatabaseRule databaseRule = new DatabaseRuleBuilder().with(
			H2DeleteOperation.class).build();

	private EntityManager entityManager = databaseRule.getEntityManager();

	@Test
	public void testPersist() throws Exception {
		final Person person = new PersonTestdataBuilder(entityManager).build();

		// execute in transaction
		databaseRule.getTransactionHelper().executeInTransaction(
				new VoidRunnable() {
					@Override
					public void doRun(EntityManager entityManager)
							throws Exception {
						entityManager.persist(person);

					}
				});

		Person personFromDB = entityManager.find(Person.class, person.getId());

		Assert.assertNotNull(personFromDB);
		Assert.assertNotSame(person, personFromDB);
	}

}
