package de.akquinet.jbosscc.needle.postconstruct.injection;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class PostConstructInjectIntoTest {

    @Rule
    public NeedleRule needleRule = new NeedleRule();

    @ObjectUnderTest(postConstruct = true)
    private ComponentWithPostConstruct componentWithPostConstruct;

    @ObjectUnderTest
    @InjectIntoMany
    private DependentComponent dependentComponent;

    @Test
    public void testPostConstruct_InjectIntoMany() throws Exception {
        int counter = dependentComponent.getCounter();
        Assert.assertEquals(1, counter);
    }
}
