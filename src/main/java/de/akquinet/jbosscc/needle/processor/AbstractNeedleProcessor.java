package de.akquinet.jbosscc.needle.processor;

import static de.akquinet.jbosscc.needle.common.Preconditions.checkArgument;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;

public abstract class AbstractNeedleProcessor implements NeedleProcessor {

    protected InjectionConfiguration configuration;

    public AbstractNeedleProcessor(final InjectionConfiguration configuration) {
        checkArgument(configuration != null, "configuration must not be null");
        this.configuration = configuration;
    }


}
