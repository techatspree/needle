package de.akquinet.jbosscc.needle.common;

import static de.akquinet.jbosscc.needle.common.Annotations.*;
import static org.junit.Assert.*;

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
