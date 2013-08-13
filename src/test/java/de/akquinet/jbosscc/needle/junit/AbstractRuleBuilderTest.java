package de.akquinet.jbosscc.needle.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;

public class AbstractRuleBuilderTest {

    public static class SpecializedBuilder extends AbstractRuleBuilder<SpecializedBuilder, String> {

        @Override
        protected String build(NeedleConfiguration needleConfiguration) {
            return "foo";
        }

    }

    @Test
    public void shouldReturnSpecializedBuilder() {
        assertThat(new SpecializedBuilder().with("needle").getClass().getCanonicalName(),
                is(SpecializedBuilder.class.getCanonicalName()));
    }

}
