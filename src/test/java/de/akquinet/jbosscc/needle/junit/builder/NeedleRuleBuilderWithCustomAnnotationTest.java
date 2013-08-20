package de.akquinet.jbosscc.needle.junit.builder;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import de.akquinet.jbosscc.needle.junit.NeedleRuleBuilder;

public class NeedleRuleBuilderWithCustomAnnotationTest {

    @Rule
    public NeedleRule needleRule = new NeedleRuleBuilder().add(TestBuilderQualifier.class).build();

    @ObjectUnderTest
    private ClassToTest objectUnderTest = new ClassToTest();

    @Test
    public void testInjection() throws Exception {
        Assert.assertNotNull(objectUnderTest.runnable);
    }

    class ClassToTest {

        @TestBuilderQualifier
        Runnable runnable;

    }

}
