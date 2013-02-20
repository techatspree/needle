package de.akquinet.jbosscc.needle.reflection;

import java.util.Collection;

@SuppressWarnings("unused")
public class DerivedClass extends SampleClass {

    private String field;

    private Boolean booleanField;

    private Collection<?> collectionField2;

    public Boolean getBooleanField() {
        return booleanField;
    }

    @Override
    public void toOverride() {
        super.toOverride();
    }

    private boolean testInvokeWithPrimitive(final int intValue, final float floatValue, final char charValue,
            final boolean booleanValue, final long longValue, final byte byteValue, final short shortValue,
            final double doubleValue) {
        return true;
    }

    private boolean testInvokeWithObjects(final Integer intValue, final Float floatValue, final Character charValue,
            final Boolean booleanValue, final Long longValue, final Byte byteValue, final Short shortValue,
            final Double doubleValue) {
        return true;
    }

    @MyAnnotation
    private void aPrivateMethod() {
        // empty
    }
}
