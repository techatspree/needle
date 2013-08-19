package de.akquinet.jbosscc.needle.mock;

import static de.akquinet.jbosscc.needle.junit.NeedleBuilders.needleRule;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Spy;
import org.mockito.exceptions.misusing.NotAMockException;

import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class SpyProviderTest {

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

    public static class C {

        public String getName() {
            return "c";
        }

    }

    @Rule
    public final NeedleRule needle = needleRule().with("needle-mockito").build();

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

    @Test
    public void shouldNotRequestSpyWhenAnnotationIsNull() throws Exception {
        Field field = SpyProviderTest.class.getDeclaredField("b");
        assertFalse(SpyProvider.FAKE.isSpyRequested(field));
    }

}
