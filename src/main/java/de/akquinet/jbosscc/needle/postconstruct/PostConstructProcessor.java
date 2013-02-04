package de.akquinet.jbosscc.needle.postconstruct;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.akquinet.jbosscc.needle.ObjectUnderTestInstantiationException;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

/**
 * Handles execution of postConstruction methods of instances marked with
 * {@link ObjectUnderTest#postConstruct()}
 * 
 * <p>
 * Note: Behaviour in an inheritance hierarchy is not defined by the common
 * annotations specification
 * </p>
 * 
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 * @author Heinz Wilming, akquinet AG (heinz.wilming@akquinet.de)
 * 
 */
public class PostConstructProcessor {

    /**
     * internal Container of all Annotations that trigger invocation
     */
    private final Set<Class<?>> postConstructAnnotations;

    public PostConstructProcessor(final Set<Class<?>> postConstructAnnotations) {
        this.postConstructAnnotations = new HashSet<Class<?>>(postConstructAnnotations);
    }

    /**
     * calls process(instance) only if field is marked with
     * 
     * @ObjectUNderTest(postConstruct=true), else ignored
     * 
     * @param objectUnderTestField
     * @param instance
     * @throws ObjectUnderTestInstantiationException
     */
    public void process(final ObjectUnderTest objectUnderTestAnnotation, final Object instance)
            throws ObjectUnderTestInstantiationException {
        if (objectUnderTestAnnotation != null && objectUnderTestAnnotation.postConstruct()) {
            process(instance);
        }
    }

    /**
     * invokes @PostConstruct annotated method
     * 
     * @param instance
     * @throws ObjectUnderTestInstantiationException
     */
    private void process(final Object instance) throws ObjectUnderTestInstantiationException {

        List<Method> postConstructMethods = getPostConstructMethods(instance);

        for (Method method : postConstructMethods) {
            try {
                ReflectionUtil.invokeMethod(method, instance);
            } catch (final Exception e) {
                throw new ObjectUnderTestInstantiationException("error executing postConstruction method '"
                        + method.getName() + "'", e);
            }

        }
    }

    /**
     * @param instance
     * @return all instance methods that are marked as postConstruction methods
     */
    @SuppressWarnings("unchecked")
    private List<Method> getPostConstructMethods(final Object instance) {
        final List<Method> postConstructMethods = new ArrayList<Method>();

        for (final Class<?> postConstructAnnotation : postConstructAnnotations) {
            for (final Method method : ReflectionUtil.getMethods(instance.getClass())) {
                if (method.isAnnotationPresent((Class<Annotation>) postConstructAnnotation)) {
                    postConstructMethods.add(method);
                }
            }

        }
        return postConstructMethods;
    }

}
