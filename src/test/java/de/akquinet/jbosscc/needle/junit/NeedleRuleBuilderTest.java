package de.akquinet.jbosscc.needle.junit;

import org.junit.Assert;
import org.junit.Test;

import de.akquinet.jbosscc.needle.mock.EasyMockProvider;
import de.akquinet.jbosscc.needle.mock.MockitoProvider;

public class NeedleRuleBuilderTest {
    
    @Test
    public void testWithMockitoProvider() throws Exception {
        NeedleRule needleRule = new NeedleRuleBuilder().with(MockitoProvider.class).build();
        Assert.assertTrue(needleRule.getMockProvider() instanceof MockitoProvider);
    }
    
    
    @Test
    public void testWithDefaultMockProvider() throws Exception {
        NeedleRule needleRule = new NeedleRuleBuilder().build();
        Assert.assertTrue(needleRule.getMockProvider() instanceof EasyMockProvider);
    }
}
