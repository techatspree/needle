package de.akquinet.jbosscc.needle.processor;

import static de.akquinet.jbosscc.needle.common.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.needle.NeedleContext;

/**
 * {@link NeedleProcessor} that calls chain of processors.
 */
public class ChainedNeedleProcessor implements NeedleProcessor {

    private final List<NeedleProcessor> processors = new ArrayList<NeedleProcessor>();

    public ChainedNeedleProcessor(final NeedleProcessor... processors) {
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
