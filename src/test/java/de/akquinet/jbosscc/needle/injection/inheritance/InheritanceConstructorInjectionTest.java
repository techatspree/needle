package de.akquinet.jbosscc.needle.injection.inheritance;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyComponent;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InheritanceConstructorInjectionTest {

    @Rule
    public NeedleRule rule = new NeedleRule();

    @ObjectUnderTest
    private ConstructorInjectionDerivedComponent derivedComponent;
    
    @Inject
    private MyComponent component;

    @Test
    public void testFieldInjection_SameMockObject() {
        assertNotNull(derivedComponent);
        assertNotNull(component);
        assertSame(derivedComponent.getMyComponentFromBase(), derivedComponent.getMyComponent());
        assertSame(component, derivedComponent.getMyComponent());
    }
    
    

}
