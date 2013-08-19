package de.akquinet.jbosscc.needle.injection.cdi.instance;

import static de.akquinet.jbosscc.needle.junit.NeedleBuilders.needleRule;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InstanceFieldInjectionTest {
    
    @Rule
    public NeedleRule needleRule = needleRule().with("needle-mockito").build();
    
    @ObjectUnderTest
    private InstanceFieldInjectionBean component;
    
    @Inject
    private Instance<InstanceTestBean> instance;
    
    @Inject
    private Instance<Runnable> runnableInstances;
    
    @Test
    public void testInstanceFieldInjection() throws Exception {
        assertNotNull(instance);
        assertNotNull(runnableInstances);
        
        assertNotSame(instance, runnableInstances);
        assertSame(instance, component.getInstance());
    }

}
