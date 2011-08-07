package de.akquinet.jbosscc.needle.db.dialect;

import java.sql.SQLException;
import java.util.Set;

public interface DBDialect {

	void disableReferentialIntegrity() throws SQLException;

	void enableReferentialIntegrity() throws SQLException;

	Set<String> getTableNames() throws SQLException;

	void openConnection() throws SQLException;

	void closeConnection() throws SQLException;

	void deleteContent(Set<String> tables) throws SQLException;

	void commit() throws SQLException;

	void rollback() throws SQLException;

}
