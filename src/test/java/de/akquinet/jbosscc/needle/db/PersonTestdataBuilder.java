package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.db.testdata.AbstractTestdataBuilder;

public class PersonTestdataBuilder extends AbstractTestdataBuilder<Person> {

    private String name = "defaultname";

    public PersonTestdataBuilder() {
        super();
    }

    public PersonTestdataBuilder(EntityManager entityManager) {
        super(entityManager);
    }

    public PersonTestdataBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public Person build() {
        Person person = new Person();
        person.setMyName(name);
        return person;
    }

}
