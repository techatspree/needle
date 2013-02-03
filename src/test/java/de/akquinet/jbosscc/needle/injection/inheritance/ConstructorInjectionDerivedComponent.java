package de.akquinet.jbosscc.needle.injection.inheritance;

import javax.inject.Inject;

import de.akquinet.jbosscc.needle.MyComponent;

public class ConstructorInjectionDerivedComponent extends ConstructorInjectionBaseComponent {

    private MyComponent myComponent;

    @Inject
    public ConstructorInjectionDerivedComponent(MyComponent myComponent) {
        super(myComponent);
        this.myComponent = myComponent;

    }

    public MyComponent getMyComponent() {
        return myComponent;
    }

    public MyComponent getMyComponentFromBase() {
        return super.getMyComponent();
    }

}
