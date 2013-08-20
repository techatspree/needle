package de.akquinet.jbosscc.needle.injection;



public class CDIInstanceInjectionProvider extends DefaultMockInjectionProvider {

    public CDIInstanceInjectionProvider(final Class<?> annotationClass,
            final InjectionConfiguration injectionConfiguration) {
        super(annotationClass, injectionConfiguration);
    }


    @Override
    public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
        return injectionTargetInformation.getGenericTypeParameter();
    }
}
