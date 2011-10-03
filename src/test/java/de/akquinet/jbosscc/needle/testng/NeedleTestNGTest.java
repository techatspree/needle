package de.akquinet.jbosscc.needle.testng;

import javax.ejb.SessionContext;
import javax.persistence.EntityManager;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.akquinet.jbosscc.needle.MyComponentBean;
import de.akquinet.jbosscc.needle.MyEjbComponent;
import de.akquinet.jbosscc.needle.MyEjbComponentBean;
import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;

public class NeedleTestNGTest extends AbstractNeedleTestcase {


	public NeedleTestNGTest() {
		super(new DatabaseTestcase("TestDataModel"));
	}

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

		MyEjbComponent mock = (MyEjbComponent) getInjectedObject(MyEjbComponent.class);

		Assert.assertNotNull(mock);
	}

	@Test
	public void testResourceMock() throws Exception {
		SessionContext sessionContextMock = (SessionContext) getInjectedObject(SessionContext.class);
		Assert.assertNotNull(sessionContextMock);

		Assert.assertNotNull(getInjectedObject("queue1"));
		Assert.assertNotNull(getInjectedObject("queue2"));
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
	public void testGetEntityManager() throws Exception {
	    EntityManager entityManager = getEntityManager();
	    Assert.assertNotNull(entityManager);
    }




}
