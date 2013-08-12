package de.akquinet.jbosscc.needle.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractNeedleRuleBuilderTest {

    public static class SpecializedBuilder extends AbstractNeedleRuleBuilder<SpecializedBuilder, String> {

        @Override
        public String build() {
            return "foo";
        }

    }

    @Test
    public void shouldReturnSpecializedBuilder() {
        assertThat(new SpecializedBuilder().with("needle").getClass().getCanonicalName(), is(SpecializedBuilder.class.getCanonicalName()));
    }

}
