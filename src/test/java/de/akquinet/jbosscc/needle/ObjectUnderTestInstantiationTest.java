package de.akquinet.jbosscc.needle;

import java.lang.reflect.Field;

import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class ObjectUnderTestInstantiationTest {

	@SuppressWarnings("unused")
	@ObjectUnderTest
	private MyEjbComponent ejbComponent;

	@SuppressWarnings("unused")
	@ObjectUnderTest
	private PrivateConstructorClass privateConstructorClass;

	@SuppressWarnings("unused")
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
