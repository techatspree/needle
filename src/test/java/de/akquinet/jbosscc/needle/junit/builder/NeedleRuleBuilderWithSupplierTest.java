package de.akquinet.jbosscc.needle.junit.builder;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionProviderInstancesSupplier;
import de.akquinet.jbosscc.needle.injection.InjectionProviders;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import de.akquinet.jbosscc.needle.junit.NeedleRuleBuilder;

public class NeedleRuleBuilderWithSupplierTest {
    
    private final Runnable runnable = new RunnableImpl();
    
    @Rule
    public NeedleRule needleRule = new NeedleRuleBuilder().add(TestBuilderQualifier.class).add(new SupplierImpl())
            .build();

    @ObjectUnderTest
    private ClassToTest objectUnderTest = new ClassToTest();

    

    @Test
    public void testInjection() throws Exception {
        Assert.assertNotNull(objectUnderTest.runnable);
        Assert.assertSame(runnable, runnable);
    }

    class ClassToTest {

        @TestBuilderQualifier
        Runnable runnable;

    }

    class RunnableImpl implements Runnable {

        @Override
        public void run() {

        }

    }

    class SupplierImpl implements InjectionProviderInstancesSupplier {

        private Set<InjectionProvider<?>> provider = new HashSet<InjectionProvider<?>>();

        public SupplierImpl() {
            provider.add(InjectionProviders.providerForQualifiedInstance(TestBuilderQualifier.class, runnable));
        }

        @Override
        public Set<InjectionProvider<?>> get() {
            return provider;
        }
    }

}
