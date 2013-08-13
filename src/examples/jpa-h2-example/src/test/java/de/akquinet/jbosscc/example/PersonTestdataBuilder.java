package de.akquinet.jbosscc.example;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.db.testdata.AbstractTestdataBuilder;
import de.akquinet.jbosscc.needle.example.Person;

public class PersonTestdataBuilder extends AbstractTestdataBuilder<Person> {

	private String withFirstname = "defaultFirstname";
	private String withLastname = "defaultLastname";

	public PersonTestdataBuilder() {
		super();
	}

	public PersonTestdataBuilder(EntityManager entityManager) {
		super(entityManager);
	}

	public PersonTestdataBuilder withFirstname(final String firstname) {
		this.withFirstname = firstname;
		return this;
	}

	public PersonTestdataBuilder withLastname(final String lastname) {
		this.withLastname = lastname;
		return this;
	}

	@Override
	public Person build() {
		Person person = new Person();
		person.setFirstname(withFirstname);
		person.setLastname(withLastname);
		return person;
	}

}
