package de.akquinet.jbosscc.needle.postconstruct.injection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.InjectIntoMany;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class PrivatePostConstructInjectIntoTest {

    @Rule
    public final NeedleRule needleRule = new NeedleRule();

    @SuppressWarnings("unused")
    @ObjectUnderTest(postConstruct = true)
    private ComponentWithPrivatePostConstruct componentWithPostConstruct;

    @InjectIntoMany
    @ObjectUnderTest
    private DependentComponent dependentComponent;

    @Test
    public void testPostConstruct_InjectIntoMany() throws Exception {
        dependentComponent.count();

        // expect one call in postConstruct, one call in here
        assertThat(dependentComponent.getCounter(), is(2));
    }
}
