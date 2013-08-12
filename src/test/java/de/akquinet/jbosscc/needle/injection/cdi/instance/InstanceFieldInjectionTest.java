package de.akquinet.jbosscc.needle.injection.cdi.instance;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InstanceFieldInjectionTest {
    
    @Rule
    public NeedleRule needleRule = new NeedleRule();
    
    @ObjectUnderTest
    private InstanceFieldInjectionBean component;
    
    @Inject
    private Instance<InstanceTestBean> instance;
    
    @Inject
    private Instance<Runnable> runnableInstances;
    
    @Test
    public void testInstanceFieldInjection() throws Exception {
        
        Assert.assertNotNull(instance);
        Assert.assertNotNull(runnableInstances);
        
        Assert.assertNotSame(instance, runnableInstances);
        Assert.assertSame(instance, component.getInstance());
    }

}
