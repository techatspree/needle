package de.akquinet.jbosscc.needle;

public class MyConcreteComponent implements MyComponent {

    @Override
    public String testMock() {
        return toString();
    }

}
