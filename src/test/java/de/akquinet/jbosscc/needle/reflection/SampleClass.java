package de.akquinet.jbosscc.needle.reflection;

import java.util.Collection;

public class SampleClass {

    @MyAnnotation
    private String aPrivateField;

    @SuppressWarnings("unused")
    private Collection<?> collectionField2;

    public String getPrivateField() {
        return aPrivateField;
    }

    @SuppressWarnings("unused")
    private String testGetMethod(final String string, final int value, final Object obejct) {
        return string;
    }

    public String testGetPulblicDerivedMethod(final String string, final int value, final Object obejct) {
        return string;
    }

    public void toOverride() {
    }

    @MyAnnotation
    protected void aProtectedMethod() {
        // empty
    }

}
