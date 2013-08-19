package de.akquinet.jbosscc.needle.processor;

import static de.akquinet.jbosscc.needle.common.Preconditions.checkArgument;
import java.util.List;

import de.akquinet.jbosscc.needle.NeedleContext;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;

public class ChainedNeedleProcessor extends AbstractNeedleProcessor {

    private List<NeedleProcessor> processors;

    public ChainedNeedleProcessor(final InjectionConfiguration configuration) {
        this(configuration, new NeedleProcessor[0]);
    }

    public ChainedNeedleProcessor(final InjectionConfiguration configuration, final NeedleProcessor... processors) {
        super(configuration);
        addProcessor(processors);
    }

    public void addProcessor(final NeedleProcessor... processors) {
        if (processors == null) {
            return;
        }
        for (NeedleProcessor processor : processors) {
            this.processors.add(processor);
        }
    }

    @Override
    public void process(final NeedleContext context) {
        checkArgument(context != null, "context must not be null");
        for (NeedleProcessor processor : this.processors) {
            processor.process(context);
        }
    }

}
