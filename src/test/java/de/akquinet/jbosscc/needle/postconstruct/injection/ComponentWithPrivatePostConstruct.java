package de.akquinet.jbosscc.needle.postconstruct.injection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class ComponentWithPrivatePostConstruct {

    @Inject
    private DependentComponent component;

    @PostConstruct
    @SuppressWarnings("unused")
    private void postconstruct() {
        component.count();
    }

}
