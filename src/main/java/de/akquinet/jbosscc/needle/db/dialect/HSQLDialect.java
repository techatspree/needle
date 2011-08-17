package de.akquinet.jbosscc.needle.db.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HSQLDialect extends AbstractDBDialect implements DBDialect {

	private static final Logger LOG = LoggerFactory.getLogger(HSQLDialect.class);

	@Override
	public void disableReferentialIntegrity() throws SQLException {
		setReferentialIntegrity(false);
	}

	@Override
	public void enableReferentialIntegrity() throws SQLException {
		setReferentialIntegrity(true);
	}

	private void setReferentialIntegrity(final boolean enable) throws SQLException {
		String referentialIntegrity = enable ? "TRUE" : "FALSE";

		getConnection().prepareStatement("set referential_integrity " + referentialIntegrity).execute();
	}

	@Override
	public Set<String> getTableNames() throws SQLException {
		ResultSet resultSet = null;

		try {
			final HashSet<String> tables = new HashSet<String>();

			resultSet = getConnection().getMetaData().getTables(null, null, "%", new String[] { "TABLE" });

			while (resultSet.next()) {
				tables.add(resultSet.getString("TABLE_NAME"));
			}
			return tables;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}

	}

	@Override
	public void deleteContent(final Set<String> tables) throws SQLException {
		final Statement statement = getConnection().createStatement();

		final ArrayList<String> tempTables = new ArrayList<String>(tables);

		final String hibernateTable = "hibernate_unique_key";
		boolean hibernateTableSkipped = tempTables.remove(hibernateTable);
		hibernateTableSkipped = hibernateTableSkipped || tempTables.remove(hibernateTable.toUpperCase());

		if (hibernateTableSkipped) {
			LOG.debug("skipped deletion of hiberate_unique_key");
		}

		// Loop until all data is deleted: we don't know the correct DROP order,
		// so we have to retry upon failure
		while (!tempTables.isEmpty()) {
			int sizeBefore = tempTables.size();
			for (final ListIterator<String> iterator = tempTables.listIterator(); iterator.hasNext();) {
				final String table = iterator.next();

				try {
					statement.executeUpdate("DELETE FROM " + table);
					iterator.remove();
				} catch (final SQLException exc) {
					LOG.debug("Ignored exception: " + exc.getMessage() + ". WILL RETRY.");
				}
			}
			if (tempTables.size() == sizeBefore) {
				throw new AssertionError("unable to clean tables " + tempTables);
			}
		}

		statement.close();
	}

}
