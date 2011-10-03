package de.akquinet.jbosscc.needle;

import javax.ejb.SessionContext;
import javax.persistence.EntityManagerFactory;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class NeedleTest {

	@Rule
	public static DatabaseRule databaseRule = new DatabaseRule();

	@Rule
	public NeedleRule needle = new NeedleRule(databaseRule);

	@ObjectUnderTest
	private MyComponentBean componentBean;

	@InjectIntoMany
	@ObjectUnderTest(implementation = MyEjbComponentBean.class)
	private MyEjbComponent ejbComponent;

	private MyComponentBean componentBean1 = new MyComponentBean();

	@ObjectUnderTest
	private MyComponentBean componentBean2 = componentBean1;

	@Test
	public void testBasicInjection() throws Exception {
		Assert.assertNotNull(componentBean);
		Assert.assertNotNull(componentBean.getEntityManager());
		Assert.assertNotNull(componentBean.getMyEjbComponent());

		MyEjbComponent mock = (MyEjbComponent) needle.getInjectedObject(MyEjbComponent.class);

		Assert.assertNotNull(mock);
	}

	@Test
	public void testResourceMock() throws Exception {
		SessionContext sessionContextMock = (SessionContext) needle.getInjectedObject(SessionContext.class);
		Assert.assertNotNull(sessionContextMock);

		Assert.assertNotNull(needle.getInjectedObject("queue1"));
		Assert.assertNotNull(needle.getInjectedObject("queue2"));
	}

	@Test
	public void testInjectInto() throws Exception {
		Assert.assertNotNull(ejbComponent);
		Assert.assertEquals(ejbComponent, componentBean.getMyEjbComponent());
	}

	@Test
	public void testInitInstance() throws Exception {
		Assert.assertEquals(componentBean1, componentBean2);
	}

	@Test
	public void testEntityManagerFactoryInjection() throws Exception {
		EntityManagerFactory entityManagerFactory = componentBean2.getEntityManagerFactory();
		Assert.assertNotNull(entityManagerFactory);

		Assert.assertNotNull(needle.getInjectedObject(EntityManagerFactory.class));

	}

}
