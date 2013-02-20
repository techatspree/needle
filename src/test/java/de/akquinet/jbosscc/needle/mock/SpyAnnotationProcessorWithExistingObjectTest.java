package de.akquinet.jbosscc.needle.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Spy;

import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.configuration.NeedleConfigurationResetRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;
import de.akquinet.jbosscc.needle.mock.SpyAnnotationProcessorTest.A;
import de.akquinet.jbosscc.needle.mock.SpyAnnotationProcessorTest.B;

public class SpyAnnotationProcessorWithExistingObjectTest {

    @ClassRule
    public static NeedleConfigurationResetRule configurationReset = new NeedleConfigurationResetRule("needle-mockito");

    @Rule
    public final NeedleRule needle = new NeedleRule();

    @ObjectUnderTest
    @Spy
    private A a;

    // b becomes a spy, although it is already instantiated
    @ObjectUnderTest
    @InjectInto(targetComponentId = "a")
    @Spy
    private B b = new B() {

        @Override
        public String getName() {
            return "world";
        }
    };

    @Test
    public void shouldInjectSpyForA() {
        when(b.getName()).thenReturn("world");

        assertThat(a.hello(), is("hello world"));
        verify(a).hello();
        verify(b).getName();
    }
}
