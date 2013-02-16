package de.akquinet.jbosscc.needle.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Spy;

import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.configuration.NeedleConfigurationResetRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class SpyAnnotationProcessorTest {

    @ClassRule
    public static NeedleConfigurationResetRule configurationReset = new NeedleConfigurationResetRule("needle-mockito");

    public static interface B {

        String getName();
    }

    public static class BImpl implements B {

        @Override
        public String getName() {
            return "foo";
        }
    }

    public static class A {

        @Inject
        private B b;

        public String hello() {
            return "hello " + b.getName();
        }
    }

    @Rule
    public final NeedleRule needle = new NeedleRule();

    @ObjectUnderTest
    @Spy
    private A a;

    @ObjectUnderTest(implementation = BImpl.class)
    @InjectInto(targetComponentId = "a")
    @Spy
    private B b;

    @Test
    public void shouldInjectSpyForA() {
        when(b.getName()).thenReturn("world");

        assertThat(a.hello(), is("hello world"));
        verify(a).hello();
        verify(b).getName();
    }
}
