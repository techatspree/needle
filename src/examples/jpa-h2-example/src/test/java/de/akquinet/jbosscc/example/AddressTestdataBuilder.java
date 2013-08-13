package de.akquinet.jbosscc.example;

import javax.persistence.EntityManager;

import de.akquinet.jbosscc.needle.db.testdata.AbstractTestdataBuilder;
import de.akquinet.jbosscc.needle.example.Address;
import de.akquinet.jbosscc.needle.example.Person;

public class AddressTestdataBuilder extends AbstractTestdataBuilder<Address> {

	private Person withPerson;

	public AddressTestdataBuilder() {
		super();
	}

	public AddressTestdataBuilder(EntityManager entityManager) {
		super(entityManager);
	}

	public AddressTestdataBuilder withPerson(final Person person) {
		this.withPerson = person;
		return this;
	}

	private Person getPerson() {
		if (withPerson != null) {
			return withPerson;
		}

		return hasEntityManager() ? new PersonTestdataBuilder(
				getEntityManager()).buildAndSave()
				: new PersonTestdataBuilder().build();
	}

	@Override
	public Address build() {
		Address address = new Address();
		address.setPerson(getPerson());
		return address;
	}

}
