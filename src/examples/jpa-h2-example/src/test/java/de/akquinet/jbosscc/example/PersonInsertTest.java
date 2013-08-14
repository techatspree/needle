package de.akquinet.jbosscc.example;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.operation.ExecuteScriptOperation;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.DatabaseRuleBuilder;

public class PersonInsertTest {

	@Rule
	public DatabaseRule databaseRule = new DatabaseRuleBuilder().with(
			ExecuteScriptOperation.class).build();
	
	private EntityManager entityManager = databaseRule.getEntityManager();

	@Test
	public void testname() throws Exception {
		Query query = entityManager.createQuery(
				"select p from Person p");

		Assert.assertEquals(3, query.getResultList().size());

	}
}
