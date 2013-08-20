package de.akquinet.jbosscc.needle.injection;

import javax.annotation.Resource;

public class ResourceMockInjectionProvider extends DefaultMockInjectionProvider {

    public ResourceMockInjectionProvider(final InjectionConfiguration injectionConfiguration) {
        super(Resource.class, injectionConfiguration);
    }

    @Override
    public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
        final Resource annotation = injectionTargetInformation.getAnnotation(Resource.class);

        if (annotation != null && !annotation.mappedName().equals("")) {
            return annotation.mappedName();
        }

        return super.getKey(injectionTargetInformation);
    }
}
