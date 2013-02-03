package de.akquinet.jbosscc.needle.injection.inheritance;

import javax.inject.Inject;

import de.akquinet.jbosscc.needle.MyComponent;

public class ConstructorInjectionBaseComponent {

    private MyComponent myComponent;

    @Inject
    public ConstructorInjectionBaseComponent(final MyComponent myComponent) {
        super();
        this.myComponent = myComponent;
    }

    public MyComponent getMyComponent() {
        return myComponent;
    }

}
