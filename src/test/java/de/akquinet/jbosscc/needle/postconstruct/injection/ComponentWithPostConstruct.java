package de.akquinet.jbosscc.needle.postconstruct.injection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class ComponentWithPostConstruct {
    
    
    @Inject
    private DependentComponent component;
    
    @PostConstruct
    public void postconstruct(){
        component.count();
    }

}
