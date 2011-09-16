package de.akquinet.jbosscc.needle.reflection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

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

	@SuppressWarnings("unused")
	private String test() {
		return "Hello World";
	}

	@SuppressWarnings("unused")
	private void testException() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}
