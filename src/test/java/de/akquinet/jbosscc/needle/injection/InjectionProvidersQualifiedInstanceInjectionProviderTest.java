package de.akquinet.jbosscc.needle.injection;

import static de.akquinet.jbosscc.needle.injection.InjectionProviders.providerForQualifiedInstance;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.MyConcreteComponent;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionProvidersQualifiedInstanceInjectionProviderTest {



    private final MyConcreteComponent providedQualifiedInstance = new MyConcreteComponent();

    @Rule
    public final NeedleRule needle = new NeedleRule(providerForQualifiedInstance(CurrentUser.class, providedQualifiedInstance));


    @Inject
    private MyConcreteComponent mockInstance;

    @Inject
    @CurrentUser
    private MyConcreteComponent qualifiedInstance;


    @Test
    public void shouldInjectNamedInstance() {
        assertThat(qualifiedInstance, is(providedQualifiedInstance));
    }

    @Test
    public void shouldInjectDefaultInstance() {
        assertThat(mockInstance, not(providedQualifiedInstance));
    }


}
