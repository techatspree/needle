package de.akquinet.jbosscc.needle.db;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class DbTestcaseHbmCfgTest {

	private final static Class<?>[] entityClasses = { Person.class, Address.class };

	@Rule
	public DatabaseRule db = new DatabaseRule(entityClasses);

	@Test
	public void testPersist() throws Exception {
		final Person person = new Person();
		final EntityManager entityManager = db.getEntityManager();

		person.setMyName("My Name");

		assertNotNull(db);
		assertNotNull(entityManager);

		entityManager.getTransaction().begin();
		entityManager.persist(person);

		final Person fromDB = entityManager.find(Person.class, person.getId());

		assertThat(fromDB.getMyName(), equalTo(person.getMyName()));
		entityManager.getTransaction().commit();
	}

}
