package de.akquinet.jbosscc.needle.injection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyConcreteComponent;
import de.akquinet.jbosscc.needle.injection.InjectionProviders;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

/**
 * moved from original package to avoid
 */
public class InjectionProvidersDefaultInstanceInjectionProviderTest {
    

    private final MyConcreteComponent instance = new MyConcreteComponent();

    @Rule
    public final NeedleRule needle = new NeedleRule(InjectionProviders.providerForInstance(instance));

    @Inject
    private MyConcreteComponent injectedInstance;


    @Test
    public void shouldInjectInstanceA() {
        assertThat(injectedInstance, is(instance));
    }


}
