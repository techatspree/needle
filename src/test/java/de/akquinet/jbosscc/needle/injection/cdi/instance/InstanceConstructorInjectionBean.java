package de.akquinet.jbosscc.needle.injection.cdi.instance;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

public class InstanceConstructorInjectionBean {

    private final Instance<InstanceTestBean> instance;

    @Inject
    public InstanceConstructorInjectionBean(final Instance<InstanceTestBean> instance) {
        this.instance = instance;
    }

    public Instance<InstanceTestBean> getInstance() {
        return instance;
    }

}
