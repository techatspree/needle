package de.akquinet.jbosscc.needle.injection.cdi.instance;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;


public class InstanceFieldInjectionBean {
    
    @Inject
    private Instance<InstanceTestBean> instance;
    
    public Instance<InstanceTestBean> getInstance(){
        return instance;
    }

}
