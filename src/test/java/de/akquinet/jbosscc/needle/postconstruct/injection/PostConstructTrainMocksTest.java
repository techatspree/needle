package de.akquinet.jbosscc.needle.postconstruct.injection;

import javax.inject.Inject;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import de.akquinet.jbosscc.needle.mock.EasyMockProvider;

public class PostConstructTrainMocksTest {

    @Rule
    public NeedleRule needleRule = new NeedleRule() {
        protected void beforePostConstruct() {
            dependentComponent.count();
            EasyMock.expectLastCall().once();
            mockProvider.replayAll();
        };
    };

    @ObjectUnderTest(postConstruct = true)
    private ComponentWithPostConstruct componentWithPostConstruct;

    @Inject
    private DependentComponent dependentComponent;

    @Inject
    private EasyMockProvider mockProvider;
    
    @Before
    public void setup(){
        mockProvider.verifyAll();
        mockProvider.resetAll();
    }

    @Test
    public void testPostConstruct_InjectIntoMany() throws Exception {
         

    }
}
