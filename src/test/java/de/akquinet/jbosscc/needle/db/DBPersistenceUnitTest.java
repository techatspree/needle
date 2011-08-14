package de.akquinet.jbosscc.needle.db;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class DBPersistenceUnitTest {

	@Rule
	public DatabaseRule db = new DatabaseRule("TestDataModel");


	@Test
	public void testDB_withPersistenceUnit() throws Exception {
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
