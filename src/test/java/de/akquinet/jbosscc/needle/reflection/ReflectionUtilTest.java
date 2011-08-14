package de.akquinet.jbosscc.needle.reflection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

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

		assertThat(allFields.size(), equalTo(2));
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

	@SuppressWarnings("unused")
	private String test() {
		return "Hello World";
	}

	@SuppressWarnings("unused")
	private void testException() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}
