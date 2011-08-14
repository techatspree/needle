package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;

public class DatabaseRuleConfigurationTest {

	@Test(expected = RuntimeException.class)
	public void testEntityManager_Close() throws Exception {
		DatabaseTestcaseConfiguration databaseRuleConfiguration = new DatabaseTestcaseConfiguration("TestDataModel");
		EntityManager entityManager = databaseRuleConfiguration.getEntityManager();
		Assert.assertNotNull(entityManager);

		entityManager.close();
    }
}
