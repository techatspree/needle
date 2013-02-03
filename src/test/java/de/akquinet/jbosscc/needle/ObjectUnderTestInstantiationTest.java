package de.akquinet.jbosscc.needle;

import java.lang.reflect.Field;

import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class ObjectUnderTestInstantiationTest {

	@ObjectUnderTest
	private MyEjbComponent ejbComponent;

	@ObjectUnderTest
	private PrivateConstructorClass privateConstructorClass;

	@ObjectUnderTest
	private NoArgsConstructorClass noArgsConstructorClass;

	@Test(expected = ObjectUnderTestInstantiationException.class)
	public void testInterfaceInstantiation() throws Exception {

		NeedleTestcase needleTestcase = new NeedleTestcase() {
		};

		Field field = ObjectUnderTestInstantiationTest.class.getDeclaredField("ejbComponent");
		ReflectionUtil.invokeMethod(needleTestcase, "setInstanceIfNotNull", field, this);
	}

	@Test(expected = ObjectUnderTestInstantiationException.class)
	public void testNoArgConstuctorInstantiation() throws Exception {
		NeedleTestcase needleTestcase = new NeedleTestcase() {
		};

		Field field = ObjectUnderTestInstantiationTest.class.getDeclaredField("noArgsConstructorClass");
		ReflectionUtil.invokeMethod(needleTestcase, "setInstanceIfNotNull", field, this);
	}

	@Test(expected = ObjectUnderTestInstantiationException.class)
	public void testNoPublicConstuctorInstantiation() throws Exception {
		NeedleTestcase needleTestcase = new NeedleTestcase() {
		};

		Field field = ObjectUnderTestInstantiationTest.class.getDeclaredField("privateConstructorClass");
		ReflectionUtil.invokeMethod(needleTestcase, "setInstanceIfNotNull", field, this);
	}
}
