package de.akquinet.jbosscc.needle.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;

public class NeedleTestRuleTest {

    public static class DummyTarget {

        @Inject
        private Runnable runner;

        public void execute() {
            runner.run();
        }

    }

    @ObjectUnderTest
    private DummyTarget dummyTarget;

    @Inject
    private Runnable runnerMock;

    @Rule
    public final NeedleTestRule needle = new NeedleTestRule(this);

    @Before
    public void setUp() throws Exception {
        doNothing().when(runnerMock).run();
    }

    @Test
    public void shouldCreateClassAndExecute() {
        assertNotNull(dummyTarget);
        assertNotNull(runnerMock);

        dummyTarget.execute();
        verify(runnerMock).run();

    }

}
