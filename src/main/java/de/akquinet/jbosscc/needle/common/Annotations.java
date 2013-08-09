package de.akquinet.jbosscc.needle.common;

import static de.akquinet.jbosscc.needle.common.Preconditions.checkArgument;

import java.lang.annotation.Annotation;

import javax.inject.Qualifier;

public final class Annotations {

    private Annotations() {
        // hide
    }

    /**
     * @param annotation
     *            annotation to check
     * @return <code>true</code> if annotation is marked with {@link Qualifier}.
     */
    public static final boolean isQualifier(final Class<? extends Annotation> annotation) {
        checkArgument(annotation != null, "annotation must not be null");
        return annotation.getAnnotation(Qualifier.class) != null;
    }

    public static final void assertIsQualifier(final Class<? extends Annotation> annotation) {
        checkArgument(isQualifier(annotation), "annotation is no qualifier");
    }
}
