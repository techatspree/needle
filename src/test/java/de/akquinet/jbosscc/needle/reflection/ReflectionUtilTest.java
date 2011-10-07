package de.akquinet.jbosscc.needle.reflection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;
import de.akquinet.jbosscc.needle.mock.MockitoProvider;

public class ReflectionUtilTest {

	@Test
	public void testCanLookupPrivateFieldFromSuperclass() {
		DerivedClass sample = new DerivedClass();

		List<Field> result = ReflectionUtil.getAllFieldsWithAnnotation(sample, MyAnnotation.class);

		assertThat(result.size(), equalTo(1));
	}

	@Test
	public void testCanInjectIntoPrivateFieldFromSuperclass() {
		DerivedClass sample = new DerivedClass();

		ReflectionUtil.setFieldValue(sample, "aPrivateField", "aValue");

		assertThat(sample.getPrivateField(), equalTo("aValue"));
	}

	@Test
	public void testGetAllFields() throws Exception {
		List<Field> allFields = ReflectionUtil.getAllFields(DerivedClass.class);

		assertThat(allFields.size(), equalTo(5));
	}

	@Test
	public void testInvokeMethod() throws Exception {
		String invokeMethod = (String) ReflectionUtil.invokeMethod(this, "test");
		Assert.assertEquals("Hello World", invokeMethod);
	}

	@Test
	public void testGetFieldValue() throws Exception {
		Address address = new Address();
		address.setId(1L);

		Assert.assertEquals(1L, ReflectionUtil.getFieldValue(address, Address.class, "id"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldValue_Exception() throws Exception {
		Address address = new Address();

		Assert.assertEquals(1L, ReflectionUtil.getFieldValue(address, Address.class, "notexisting"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldValue_ByField_Exception() throws Exception {

		Assert.assertEquals(1L, ReflectionUtil.getFieldValue(null, ReflectionUtil.getField(Address.class, "id")));
	}

	@Test
	public void testGetField_NoSuchField() throws Exception {
		Assert.assertNull(ReflectionUtil.getField(String.class, "fieldName"));
	}

	@Test
	public void testGetField_DerivedClass() throws Exception {
		Assert.assertNotNull(ReflectionUtil.getField(DerivedClass.class, "aPrivateField"));
	}

	@Test
	public void testGetMethodAndInvoke() throws Exception {
		Method method = ReflectionUtil.getMethod(DerivedClass.class, "testGetMethod", String.class, int.class,
		        Object.class);
		Assert.assertNotNull(method);

		Object result = ReflectionUtil.invokeMethod(method, new DerivedClass(), "Hello", 1, "");
		Assert.assertEquals("Hello", result.toString());

	}

	@Test
	public void testGetMethod() throws Exception {
		List<Method> methods = ReflectionUtil.getMethods(DerivedClass.class);

		Assert.assertEquals(13, methods.size());

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testInvokeMethod_Exception() throws Exception {
		ReflectionUtil.invokeMethod(this, "testException");
	}

	@Test
	public void testGetAllFieldsAssinableFrom() throws Exception {
		List<Field> allFieldsAssinableFromBoolean = ReflectionUtil.getAllFieldsAssinableFrom(Boolean.class,
		        DerivedClass.class);
		Assert.assertEquals(1, allFieldsAssinableFromBoolean.size());

		List<Field> allFieldsAssinableFromList = ReflectionUtil.getAllFieldsAssinableFrom(List.class,
		        DerivedClass.class);
		Assert.assertEquals(2, allFieldsAssinableFromList.size());

		List<Field> allFieldsAssinableFromCollection = ReflectionUtil.getAllFieldsAssinableFrom(Collection.class,
		        DerivedClass.class);
		Assert.assertEquals(2, allFieldsAssinableFromCollection.size());

		List<Field> allFieldsAssinableFromString = ReflectionUtil.getAllFieldsAssinableFrom(String.class,
		        DerivedClass.class);
		Assert.assertEquals(2, allFieldsAssinableFromString.size());
	}

	@Test
	public void testCreateInstance() throws Exception {
		Assert.assertNotNull(ReflectionUtil.createInstance(MockitoProvider.class));

		Assert.assertEquals("Hello", ReflectionUtil.createInstance(String.class, "Hello"));
	}

	@Test(expected = Exception.class)
	public void testCreateInstance_Exception() throws Exception {
		ReflectionUtil.createInstance(InjectionTargetInformation.class);
	}

	@Test
	public void testInvokeMethod_checkArgumentsWithPrimitives() throws Exception {
		DerivedClass derivedClass = new DerivedClass();

		final int intValue = 1;
		final float floatValue = 0F;
		final char charValue = 'c';
		final boolean booleanValue = true;
		final long longValue = 10L;
		final byte byteValue = 2;
		final short shortValue = 32;
		final double doubleValue = 24.1;

		Object resultPrimatives = ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithPrimitive", intValue,
		        floatValue, charValue, booleanValue, longValue, byteValue, shortValue, doubleValue);
		Assert.assertEquals(true, resultPrimatives);

		Object resultObjects = ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithObjects", intValue, floatValue,
		        charValue, booleanValue, longValue, byteValue, shortValue, doubleValue);
		Assert.assertEquals(true, resultObjects);
	}

	@Test
	public void testInvokeMethod_checkArgumentsWithObjects() throws Exception {
		DerivedClass derivedClass = new DerivedClass();

		final Integer intValue = 1;
		final Float floatValue = 0F;
		final Character charValue = 'c';
		final Boolean booleanValue = true;
		final Long longValue = 10L;
		final Byte byteValue = 2;
		final Short shortValue = 32;
		final Double doubleValue = 24.1;

		Object resultPrimatives = ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithPrimitive", intValue,
		        floatValue, charValue, booleanValue, longValue, byteValue, shortValue, doubleValue);
		Assert.assertEquals(true, resultPrimatives);

		Object resultObjects = ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithObjects", intValue, floatValue,
		        charValue, booleanValue, longValue, byteValue, shortValue, doubleValue);
		Assert.assertEquals(true, resultObjects);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvokeMethod_NoSuchMethod() throws Exception {
		DerivedClass derivedClass = new DerivedClass();

		ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithPrimitive");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvokeMethod_WithWrongParameter() throws Exception {
		ReflectionUtil.invokeMethod(this, "test", new Double(1.));
	}

	@SuppressWarnings("unused")
	private String test() {
		return "Hello World";
	}

	@SuppressWarnings("unused")
	private String test(int value) {
		return "";
	}

	@SuppressWarnings("unused")
	private void testException() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}
