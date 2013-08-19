package de.akquinet.jbosscc.needle.processor;

import de.akquinet.jbosscc.needle.NeedleContext;

public interface NeedleProcessor {

    /**
     * @param context
     *            needle context for test class
     */
    void process(NeedleContext context);
}
