package de.akquinet.jbosscc.needle.mock;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SpyAnnotationProcessor modifies the given instance so it becomes a spy (hybrid of mock and real pojo).
 * It checks if the configured {@link MockProvider} supports spies, and if the current objectUnderTest instance is
 * marked with a spy annotation.
 * 
 * @author Jan Galinski, Holisticon AG
 */
public class SpyAnnotationProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SpyAnnotationProcessor.class);

    private final SpyProvider spyProvider;

    public SpyAnnotationProcessor(final MockProvider mockProvider) {
        this.spyProvider = (mockProvider instanceof SpyProvider)
                ? (SpyProvider)mockProvider
                : null;
    }

    /**
     * @param objectUnderTest - the instance that should become a spy
     * @param field - the field, needed for annotation access
     * @return modified spy-instance
     */
    public <T> T process(final T objectUnderTest, final Field field) {
        // mockProvider does not allow spies: return instance without modification
        if (!isSpySupported()) {
            LOG.debug("The mockprovider does not support spying. Skipping creation for " + field);
            return objectUnderTest;
        }

        // create Spy if Annotation is present, just return instance else.
        return (isSpyRequested(field))
                ? spyProvider.createSpyComponent(objectUnderTest)
                : objectUnderTest;
    }

    private boolean isSpySupported() {
        return spyProvider != null;
    }

    /**
     * @param field
     * @return <code>true</code> if spy-provider is set and current field is annotated as spy, <code>false</code> else.
     */
    public boolean isSpyRequested(final Field field) {
        return isSpySupported() && field.isAnnotationPresent(spyProvider.getSpyAnnotation());
    }
}
