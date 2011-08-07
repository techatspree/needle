package de.akquinet.jbosscc.needle;

import javax.ejb.SessionContext;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.db.DatabaseRule;

public class NeedleTest {


	@Rule
	public DatabaseRule databaseRule = new DatabaseRule();

	@Rule
	public NeedleRule needle = new NeedleRule(databaseRule);


	@ObjectUnderTest
	private MyComponentBean componentBean;

	@InjectInto(targetComponent = "componentBean", fieldName = "myEjbComponent")
	@ObjectUnderTest(implementation = MyEjbComponentBean.class)
	private MyEjbComponent ejbComponent;



	@Test
	public void testname() throws Exception {
		Assert.assertNotNull(componentBean);
		Assert.assertNotNull(componentBean.getEntityManager());
		Assert.assertNotNull(componentBean.getMyEjbComponent());

		MyEjbComponent mock = needle.getMock(MyEjbComponent.class);

		Assert.assertNotNull(mock);


	}


	@Test
	public void testResourceMock() throws Exception {
		SessionContext sessionContextMock = needle.getMock(SessionContext.class);
		Assert.assertNotNull(sessionContextMock);

		Assert.assertNotNull(needle.getMock("queue1"));
		Assert.assertNotNull(needle.getMock("queue2"));
    }

	@Test
	public void testInjectInto() throws Exception {
		Assert.assertNotNull(ejbComponent);
		Assert.assertEquals(ejbComponent, componentBean.getMyEjbComponent());
    }

}
