package de.akquinet.jbosscc.needle.injection.inheritance;

import javax.inject.Inject;

import de.akquinet.jbosscc.needle.MyComponent;

public class GraphDependencyComponent {

    @Inject
    private MyComponent component;

    public MyComponent getComponent() {
        return component;
    }

}
