package de.akquinet.jbosscc.needle.injection.cdi.instance;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InstanceMethodInjectionTest {

    @Rule
    public final NeedleRule needleRule = new NeedleRule();
    
    @ObjectUnderTest
    private InstanceMethodInjectionBean component;
    
    @Inject
    private Instance<InstanceTestBean> instance;
    
    @Inject
    private Instance<Runnable> runnableInstances;
    
    
    @Test
    public void testInstanceMethodInjection() throws Exception {
        
        Assert.assertNotNull(instance);
        Assert.assertNotNull(runnableInstances);
        
        Assert.assertNotSame(instance, runnableInstances);
        Assert.assertNotNull(component.getInstance());
        Assert.assertSame(instance, component.getInstance());
    }
}
