package de.akquinet.jbosscc.needle.injection.cdi.instance;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;


public class InstanceMethodInjectionBean {
    
    
    private Instance<InstanceTestBean> instance;


    public Instance<InstanceTestBean> getInstance(){
        return instance;
    }
    
    @Inject
    public void setInstance(final Instance<InstanceTestBean> instance){
        this.instance = instance;
    }
    

}
