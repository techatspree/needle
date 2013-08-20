package de.akquinet.jbosscc.needle.injection;

import java.lang.annotation.Annotation;

public class DefaultMockInjectionProvider implements InjectionProvider<Object> {

    private final Class<?> annotationClass;

    private final InjectionConfiguration injectionConfiguration;

    /**
     * 
     * @param annotationClass
     *            injection annotation like Resource, EJB, Inject, ...
     * @param mockProvider
     */
    public DefaultMockInjectionProvider(final Class<?> annotationClass,
            final InjectionConfiguration injectionConfiguration) {
        this.annotationClass = annotationClass;
        this.injectionConfiguration = injectionConfiguration;
    }

    @Override
    public Object getInjectedObject(final Class<?> type) {
        return injectionConfiguration.getMockProvider().createMockComponent(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean verify(final InjectionTargetInformation injectionTargetInformation) {

        return (injectionTargetInformation.getType() == annotationClass
                || (annotationClass.isAnnotation() && injectionTargetInformation
                .isAnnotationPresent((Class<? extends Annotation>) annotationClass)));
    }

    protected Class<?> getType() {
        return annotationClass;
    }

    @Override
    public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
        return injectionTargetInformation.getType();
    }

    protected InjectionConfiguration getInjectionConfiguration() {
        return injectionConfiguration;
    }

}
