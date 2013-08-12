package de.akquinet.jbosscc.needle.injection.cdi.instance;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

public class InstanceConstructorInjectionBean {

    private Instance<InstanceTestBean> instance;

    @Inject
    public InstanceConstructorInjectionBean(Instance<InstanceTestBean> instance) {
        super();
        this.instance = instance;
    }

    public Instance<InstanceTestBean> getInstance() {
        return instance;
    }

}
