package de.akquinet.jbosscc.needle.db.operation.h2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.db.Address;
import de.akquinet.jbosscc.needle.db.operation.JdbcConfiguration;
import de.akquinet.jbosscc.needle.db.transaction.VoidRunnable;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;

public class H2DeleteOperationTest {

    private static final JdbcConfiguration H2_DB_CONFIGURATION = new JdbcConfiguration("jdbc:h2:mem:test",
            "org.h2.Driver", "sa", "");

    private H2DeleteOperationForTest h2DeleteOperation = new H2DeleteOperationForTest(H2_DB_CONFIGURATION);
    
    
    @Rule
    public DatabaseRule databaseRule = new DatabaseRule("H2TestDataModel", h2DeleteOperation);
    
    

    @Test
    public void testDisableReferentialIntegrity() throws Exception {
        Statement statement = h2DeleteOperation.getConnection().createStatement();
        h2DeleteOperation.disableReferentialIntegrity(statement);
        insertAddressWithInvalidFk();

        statement.close();

    }

    @Test(expected = SQLException.class)
    public void testEnableReferentialIntegrity() throws Exception {
        Statement statement = null;
        try {
            statement = h2DeleteOperation.getConnection().createStatement();
            h2DeleteOperation.enableReferentialIntegrity(statement);
            insertAddressWithInvalidFk();
        } finally {
            statement.getConnection().commit();
            statement.close();
        }

    }

    private void insertAddressWithInvalidFk() throws Exception {
        Statement statement = null;
        try {

            statement = h2DeleteOperation.getConnection().createStatement();
            List<String> table = new ArrayList<String>();
            table.add(Address.TABLE_NAME);
            final Address address = new Address();

            databaseRule.getTransactionHelper().executeInTransaction(new VoidRunnable() {

                @Override
                public void doRun(EntityManager entityManager) throws Exception {
                    entityManager.persist(address);
                    entityManager.flush();

                }
            });
            h2DeleteOperation.commit();

            Statement st = h2DeleteOperation.getConnection().createStatement();
            int executeUpdate = st.executeUpdate("update " + Address.TABLE_NAME + " set person_id = 2");
            Assert.assertEquals(1, executeUpdate);
            st.close();
            h2DeleteOperation.commit();
        } finally {
            statement.close();
        }
    }

    @Test
    public void testDeleteContent() throws Exception {
        Statement statement = null;
        try {
            statement = h2DeleteOperation.getConnection().createStatement();
            databaseRule.getTransactionHelper().executeInTransaction(new VoidRunnable() {

                @Override
                public void doRun(EntityManager entityManager) throws Exception {
                    entityManager.persist(new Address());

                }
            });

            Statement st = h2DeleteOperation.getConnection().createStatement();
            ResultSet rs = st.executeQuery("select * from " + Address.TABLE_NAME);
            Assert.assertTrue(rs.next());

            List<String> tableNames = new ArrayList<String>();
            tableNames.add(Address.TABLE_NAME);
            h2DeleteOperation.deleteContent(tableNames, statement);

            st = h2DeleteOperation.getConnection().createStatement();
            st.executeQuery("select * from " + Address.TABLE_NAME);
            Assert.assertFalse(rs.next());
            rs.close();
            st.close();

        } finally {
            statement.close();
        }

    }

    @After
    public void tearDown() throws Exception {
        h2DeleteOperation.closeConnection();
    }

    class H2DeleteOperationForTest extends H2DeleteOperation {

        public H2DeleteOperationForTest(JdbcConfiguration configuration) {
            super(configuration);
        }

        @Override
        protected Connection getConnection() throws SQLException {
            return super.getConnection();
        }

        @Override
        protected void closeConnection() throws SQLException {
            super.closeConnection();
        }

        @Override
        protected void commit() throws SQLException {
            super.commit();
        }

        @Override
        protected void rollback() throws SQLException {
            super.rollback();
        }

        @Override
        protected void disableReferentialIntegrity(Statement statement) throws SQLException {
            super.disableReferentialIntegrity(statement);
        }

        @Override
        protected void enableReferentialIntegrity(Statement statement) throws SQLException {
            super.enableReferentialIntegrity(statement);
        }

        @Override
        protected void deleteContent(List<String> tables, Statement statement) throws SQLException {
            super.deleteContent(tables, statement);
        }

    }

}
