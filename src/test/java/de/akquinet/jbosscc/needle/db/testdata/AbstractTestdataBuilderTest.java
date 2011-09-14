package de.akquinet.jbosscc.needle.db.testdata;

import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.MyEntity;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class AbstractTestdataBuilderTest {

	@Rule
	public DatabaseRule databaseRule = new DatabaseRule();

	@Test
	public void testSaveOrUpdate() throws Exception {
		new MyEntityTestDataBuilder(databaseRule.getEntityManager()).buildAndSave();

		List<MyEntity> loadAllObjects = databaseRule.getTransactionHelper().loadAllObjects(MyEntity.class);

		Assert.assertEquals(1, loadAllObjects.size());
	}

	@Test(expected = IllegalStateException.class)
	public void testSaveOrUpdate_withOutEntityManager() throws Exception {
		new MyEntityTestDataBuilder().buildAndSave();
	}

	@Test
	public void testGetId() throws Exception {
		Assert.assertEquals(0, new MyEntityTestDataBuilder().getId());
		Assert.assertEquals(1, new MyEntityTestDataBuilder().getId());
		Assert.assertEquals(2, new MyEntityTestDataBuilder().getId());
	}


	@Test(expected = RuntimeException.class)
	public void testBuildAndSave_Unsuccessful() throws Exception {
		new AbstractTestdataBuilder<MyEntity>(databaseRule.getEntityManager()){

			@Override
            public MyEntity build() {

	            return null;
            }

		}.buildAndSave();
    }

	class MyEntityTestDataBuilder extends AbstractTestdataBuilder<MyEntity> {

		public MyEntityTestDataBuilder() {
			super();
		}

		public MyEntityTestDataBuilder(EntityManager entityManager) {
			super(entityManager);
		}

		@Override
		public MyEntity build() {

			MyEntity myEntity = new MyEntity();

			myEntity.setMyName("myName");
			return myEntity;
		}
	}

}
