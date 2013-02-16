package de.akquinet.jbosscc.needle.mock;

import java.lang.annotation.Annotation;

/**
 * Interface to abstract the creation of spy() instances, if the used framework allows to do so.
 * 
 * @author Jan Galinski, Holisticon AG
 */
public interface SpyProvider {

    /**
     * @param instance
     * @return Spy of instance (spy(instance) for Mockito)
     */
    <T> T createSpyComponent(T instance);

    /**
     * @return the Annotation used to trigger the spy creation. (@Spy for Mockito)
     */
    Class<? extends Annotation> getSpyAnnotation();
}
