package de.akquinet.jbosscc.needle.injection;

import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.DatabaseTestcase;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class EntityManagerFactoryProviderTest {
	private EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider(
	        new DatabaseTestcase());

	@SuppressWarnings("unused")
    private EntityManagerFactory entityManagerFactory;

	@Test
	public void testVerify() throws Exception {
		InjectionTargetInformation injectionTargetInformation = new InjectionTargetInformation(
		        EntityManagerFactory.class, ReflectionUtil.getField(this.getClass(), "entityManagerFactory"));
		Assert.assertTrue(entityManagerFactoryProvider.verify(injectionTargetInformation));
	}

}
