package de.akquinet.jbosscc.needle.mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Interface to abstract the creation of spy() instances, if the used framework allows to do so.
 * 
 * @author Jan Galinski, Holisticon AG
 */
public interface SpyProvider {


    /**
     * Just return the given instance. Use this as default provider when the
     * mockProvider does not support spies.
     */
    SpyProvider FAKE = new SpyProvider() {

        @Override
        public <T> T createSpyComponent(final T instance) {
            return instance;
        }

        @Override
        public Class<? extends Annotation> getSpyAnnotation() {
            return null;
        }

        @Override
        public boolean isSpyRequested(final Field field) {
            return false;
        }
    };

    /**
     * @param instance
     * @return Spy of instance (spy(instance) for Mockito)
     */
    <T> T createSpyComponent(T instance);

    /**
     * @return the Annotation used to trigger the spy creation. (@Spy for Mockito)
     */
    Class<? extends Annotation> getSpyAnnotation();

    boolean isSpyRequested(Field field);
}
