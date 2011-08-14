package de.akquinet.jbosscc.needle.db;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class DbTestcaseHbmCfgTest {

	private final static Class<?>[] entityClasses = { MyEntity.class };

	@Rule
	public DatabaseRule db = new DatabaseRule(entityClasses);

	@Test
	public void testPersist() throws Exception {
		final MyEntity myEntity = new MyEntity();
		final EntityManager entityManager = db.getEntityManager();

		myEntity.setMyName("My Name");

		assertNotNull(db);
		assertNotNull(entityManager);

		entityManager.persist(myEntity);

		MyEntity fromDB = entityManager.find(MyEntity.class, myEntity.getId());

		assertThat(fromDB.getMyName(), equalTo(myEntity.getMyName()));
	}

}
