package de.akquinet.jbosscc.needle.configuration;

import static de.akquinet.jbosscc.needle.NeedleBuilders.needleRule;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyComponent;
import de.akquinet.jbosscc.needle.injection.CustomMyComponentInjectionProviderInstancesSupplier;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

/**
 * The needle-mockito properties file sets the custom.instances.supplier.classes
 * property to {@link CustomMyComponentInjectionProviderInstancesSupplier}. The
 * test verifies, that the component instance defined in the supplier is used
 * for injection.
 */
public class LoadInstancesSupplierFromProperties {

    @Rule
    public final NeedleRule needle = needleRule("needle-mockito").build();

    @Inject
    private MyComponent component;

    @Test
    public void shouldInjectMyComponentWithFoo() {
        assertThat(component.testMock(), is(CustomMyComponentInjectionProviderInstancesSupplier.ID));
    }

}
