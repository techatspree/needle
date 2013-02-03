package de.akquinet.jbosscc.needle.injection.inheritance;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyComponent;
import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InheritanceInjectionTest {

    @Rule
    public NeedleRule rule = new NeedleRule();

    @ObjectUnderTest
    private DerivedComponent derivedComponent;

    @ObjectUnderTest
    @InjectIntoMany
    private GraphDependencyComponent dependencyComponent;

    @Inject
    private MyComponent component;

    @Test
    public void testFieldInjection_SameMockObject() {
        assertNotNull(derivedComponent);
        assertSame(derivedComponent.getComponentFromBaseByFieldInjection(),
                derivedComponent.getComponentByFieldInjection());
        assertSame(component, derivedComponent.getComponentByFieldInjection());
    }

    @Test
    public void testSetterInjection_SameMockObject() {
        assertNotNull(derivedComponent);
        assertNotNull(derivedComponent.getComponentFromBaseBySetter());
        assertSame(derivedComponent.getComponentFromBaseBySetter(), derivedComponent.getComponentBySetter());
        assertSame(component, derivedComponent.getComponentBySetter());
    }

    @Test
    public void testGarphInjection() {

        MyComponent componentByFieldInjection = derivedComponent.getComponentByFieldInjection();
        MyComponent component = dependencyComponent.getComponent();

        assertSame(component, componentByFieldInjection);

    }

}
