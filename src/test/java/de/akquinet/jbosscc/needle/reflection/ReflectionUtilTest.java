package de.akquinet.jbosscc.needle.reflection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

public class ReflectionUtilTest {


	@Test
	public void testCanLookupPrivateFieldFromSuperclass() {
		DerivedClass sample = new DerivedClass();

		List<Field> result = ReflectionUtil.getAllFieldsWithAnnotation(sample,
				MyAnnotation.class);

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

}
