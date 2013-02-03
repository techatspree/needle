package de.akquinet.jbosscc.needle.injection;

import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionFinalClassTest {

    @Rule
    public NeedleRule needleRule = new NeedleRule();

    @ObjectUnderTest
    private InjectionFinalClass testClass;

    @Test
    public void testFinal() throws Exception {
        assertNull(testClass.getString());

    }

}
