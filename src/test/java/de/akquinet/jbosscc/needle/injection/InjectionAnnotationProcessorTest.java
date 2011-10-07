package de.akquinet.jbosscc.needle.injection;

import static org.junit.Assert.assertSame;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyEjbComponentBean;
import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.db.User;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionAnnotationProcessorTest {
	@Rule
	public NeedleRule _needleRule = new NeedleRule();

	@ObjectUnderTest
	private final UserDao _userDao1 = new UserDao();

	@ObjectUnderTest
	private final UserDao _userDao2 = new UserDao();

	@ObjectUnderTest(id = "testInjectionId")
	private MyEjbComponentBean bean;

	@InjectInto(targetComponentId = "testInjectionId")
	private String test = "Hello";

	@InjectIntoMany
	private final User _user = new User();

	@InjectIntoMany(value = { @InjectInto(targetComponentId = "testInjectionId", fieldName = "queue"),
	        @InjectInto(targetComponentId = "_userDao2") })
	private Queue<?> queue = new LinkedBlockingDeque<Object>();

	@Test
	public void testInjectMany() throws Exception {
		assertSame(_user, _userDao1.getCurrentUser());
		assertSame(_user, _userDao2.getCurrentUser());
	}

	@Test
	public void testInjectManyWithInjectInto() throws Exception {
		assertSame(queue, _userDao2.getQueue());
		assertSame(queue, bean.getQueue());

		Assert.assertNull(_userDao1.getQueue());
	}

	@Test
	public void testInjectIntoById() throws Exception {
		assertSame(test, bean.getTestInjection());
	}
}
