package de.akquinet.jbosscc.needle.reflection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import de.akquinet.jbosscc.needle.MyComponentBean;
import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;
import de.akquinet.jbosscc.needle.mock.MockitoProvider;

public class ReflectionUtilTest {

    @Test
    public void testCanLookupPrivateFieldFromSuperclass() {
        final DerivedClass sample = new DerivedClass();

        final List<Field> result = ReflectionUtil.getAllFieldsWithAnnotation(sample, MyAnnotation.class);

        assertThat(result.size(), equalTo(1));
    }

    @Test
    public void testCanInjectIntoPrivateFieldFromSuperclass() {
        final DerivedClass sample = new DerivedClass();

        ReflectionUtil.setFieldValue(sample, "aPrivateField", "aValue");

        assertThat(sample.getPrivateField(), equalTo("aValue"));
    }

    @Test
    public void testGetAllFields() throws Exception {
        final List<Field> allFields = ReflectionUtil.getAllFields(DerivedClass.class);

        assertThat(allFields.size(), equalTo(5));
    }

    @Test
    public void testAllAnnotatedFields() throws Exception {
        final Map<Class<? extends Annotation>, List<Field>> allAnnotatedFields = ReflectionUtil.getAllAnnotatedFields(MyComponentBean.class);
        assertEquals(4, allAnnotatedFields.size());

        final List<Field> list = allAnnotatedFields.get(Resource.class);
        assertEquals(3, list.size());

    }

    @Test
    public void testInvokeMethod() throws Exception {
        final String invokeMethod = (String)ReflectionUtil.invokeMethod(this, "test");
        assertEquals("Hello World", invokeMethod);
    }

    @Test
    public void testGetFieldValue() throws Exception {
        final Address address = new Address();
        address.setId(1L);

        assertEquals(1L, ReflectionUtil.getFieldValue(address, Address.class, "id"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldValue_Exception() throws Exception {
        final Address address = new Address();

        assertEquals(1L, ReflectionUtil.getFieldValue(address, Address.class, "notexisting"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldValue_ByField_Exception() throws Exception {

        assertEquals(1L, ReflectionUtil.getFieldValue(null, ReflectionUtil.getField(Address.class, "id")));
    }

    @Test
    public void testGetField_NoSuchField() throws Exception {
        assertNull(ReflectionUtil.getField(String.class, "fieldName"));
    }

    @Test
    public void testGetField_DerivedClass() throws Exception {
        assertNotNull(ReflectionUtil.getField(DerivedClass.class, "aPrivateField"));
    }

    @Test
    public void testGetMethodAndInvoke() throws Exception {
        final Method method = ReflectionUtil.getMethod(DerivedClass.class, "testGetMethod", String.class, int.class,
                Object.class);
        assertNotNull(method);

        final Object result = ReflectionUtil.invokeMethod(method, new DerivedClass(), "Hello", 1, "");
        assertEquals("Hello", result.toString());

    }

    @Test
    public void testGetMethod() throws Exception {
        final List<Method> methods = ReflectionUtil.getMethods(DerivedClass.class);

        assertEquals(13, methods.size());

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvokeMethod_Exception() throws Exception {
        ReflectionUtil.invokeMethod(this, "testException");
    }

    @Test
    public void testGetAllFieldsAssinableFrom() throws Exception {
        final List<Field> allFieldsAssinableFromBoolean = ReflectionUtil.getAllFieldsAssinableFrom(Boolean.class,
                DerivedClass.class);
        assertEquals(1, allFieldsAssinableFromBoolean.size());

        final List<Field> allFieldsAssinableFromList = ReflectionUtil.getAllFieldsAssinableFrom(List.class,
                DerivedClass.class);
        assertEquals(2, allFieldsAssinableFromList.size());

        final List<Field> allFieldsAssinableFromCollection = ReflectionUtil.getAllFieldsAssinableFrom(Collection.class,
                DerivedClass.class);
        assertEquals(2, allFieldsAssinableFromCollection.size());

        final List<Field> allFieldsAssinableFromString = ReflectionUtil.getAllFieldsAssinableFrom(String.class,
                DerivedClass.class);
        assertEquals(2, allFieldsAssinableFromString.size());
    }

    @Test
    public void testCreateInstance() throws Exception {
        assertNotNull(ReflectionUtil.createInstance(MockitoProvider.class));

        assertEquals("Hello", ReflectionUtil.createInstance(String.class, "Hello"));
    }

    @Test(expected = Exception.class)
    public void testCreateInstance_Exception() throws Exception {
        ReflectionUtil.createInstance(InjectionTargetInformation.class);
    }

    @Test
    public void testInvokeMethod_checkArgumentsWithPrimitives() throws Exception {
        final DerivedClass derivedClass = new DerivedClass();

        final int intValue = 1;
        final float floatValue = 0F;
        final char charValue = 'c';
        final boolean booleanValue = true;
        final long longValue = 10L;
        final byte byteValue = 2;
        final short shortValue = 32;
        final double doubleValue = 24.1;

        final Object resultPrimatives = ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithPrimitive", intValue,
                floatValue, charValue, booleanValue, longValue, byteValue, shortValue, doubleValue);
        assertEquals(true, resultPrimatives);

        final Object resultObjects = ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithObjects", intValue, floatValue,
                charValue, booleanValue, longValue, byteValue, shortValue, doubleValue);
        assertEquals(true, resultObjects);
    }

    @Test
    public void testInvokeMethod_checkArgumentsWithObjects() throws Exception {
        final DerivedClass derivedClass = new DerivedClass();

        final Integer intValue = 1;
        final Float floatValue = 0F;
        final Character charValue = 'c';
        final Boolean booleanValue = true;
        final Long longValue = 10L;
        final Byte byteValue = 2;
        final Short shortValue = 32;
        final Double doubleValue = 24.1;

        final Object resultPrimatives = ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithPrimitive", intValue,
                floatValue, charValue, booleanValue, longValue, byteValue, shortValue, doubleValue);
        assertEquals(true, resultPrimatives);

        final Object resultObjects = ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithObjects", intValue, floatValue,
                charValue, booleanValue, longValue, byteValue, shortValue, doubleValue);
        assertEquals(true, resultObjects);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeMethod_NoSuchMethod() throws Exception {
        final DerivedClass derivedClass = new DerivedClass();

        ReflectionUtil.invokeMethod(derivedClass, "testInvokeWithPrimitive");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeMethod_WithWrongParameter() throws Exception {
        ReflectionUtil.invokeMethod(this, "test", new Double(1.));
    }

    @Test
    public void shouldFindAllMethodsWithMyAnnotation() throws Exception {
        final List<Method> result = ReflectionUtil.getAllMethodsWithAnnotation(DerivedClass.class, MyAnnotation.class);
        assertThat(result.size(), is(2));
    }

    @SuppressWarnings("unused")
    private String test() {
        return "Hello World";
    }

    @SuppressWarnings("unused")
    private String test(final int value) {
        return "";
    }

    @SuppressWarnings("unused")
    private void testException() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
