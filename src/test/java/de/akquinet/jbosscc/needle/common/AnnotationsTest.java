package de.akquinet.jbosscc.needle.common;

import static de.akquinet.jbosscc.needle.common.Annotations.isQualifier;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Named;

import org.junit.Test;

import de.akquinet.jbosscc.needle.injection.CurrentUser;

public class AnnotationsTest {

    @Test
    public void shouldReturnTrueForValidQualifier() {
        assertTrue(isQualifier(CurrentUser.class));
        assertTrue(isQualifier(Named.class));
    }

    @Test
    public void shouldReturnFalseIfAnnotationIsNotAQualifier() {
        assertFalse(isQualifier(Test.class));
    }

}
