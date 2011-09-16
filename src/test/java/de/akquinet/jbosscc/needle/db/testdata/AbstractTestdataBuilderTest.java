package de.akquinet.jbosscc.needle.db.testdata;

import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.db.Person;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class AbstractTestdataBuilderTest {

	@Rule
	public DatabaseRule databaseRule = new DatabaseRule();

	@Test
	public void testSaveOrUpdate() throws Exception {
		new AddressTestDataBuilder(databaseRule.getEntityManager()).buildAndSave();

		List<Address> loadAllObjects = databaseRule.getTransactionHelper().loadAllObjects(Address.class);

		Assert.assertEquals(1, loadAllObjects.size());
	}

	@Test
	public void testHasEntityManager() throws Exception {

		Assert.assertFalse(new AddressTestDataBuilder().hasEntityManager());
		Assert.assertTrue(new AddressTestDataBuilder(databaseRule.getEntityManager()).hasEntityManager());

	}

	@Test(expected = IllegalStateException.class)
	public void testSaveOrUpdate_withOutEntityManager() throws Exception {
		new AddressTestDataBuilder().buildAndSave();
	}

	@Test
	public void testGetId() throws Exception {
		Assert.assertEquals(0, new AddressTestDataBuilder().getId());
		Assert.assertEquals(1, new AddressTestDataBuilder().getId());
		Assert.assertEquals(2, new AddressTestDataBuilder().getId());
	}

	@Test(expected = RuntimeException.class)
	public void testBuildAndSave_Unsuccessful() throws Exception {
		new AbstractTestdataBuilder<Person>(databaseRule.getEntityManager()) {

			@Override
			public Person build() {

				return null;
			}

		}.buildAndSave();
	}

	class AddressTestDataBuilder extends AbstractTestdataBuilder<Address> {

		public AddressTestDataBuilder() {
			super();
		}

		public AddressTestDataBuilder(EntityManager entityManager) {
			super(entityManager);
		}

		@Override
		public Address build() {

			Address address = new Address();

			address.setStreet("street");
			return address;
		}
	}

}
