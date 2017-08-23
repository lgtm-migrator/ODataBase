package org.fornever.api.types;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class DatabaseMetadata {

	private List<TableMetadata> tables;

	public List<TableMetadata> getTables() {
		return tables;
	}

	public DatabaseMetadata() {
		this.tables = new ArrayList<>();
	}

	public void loadMetadata(DataSource dataSource) throws SQLException {
		Connection connection = dataSource.getConnection();
		loadMetadata(connection);
		connection.close();
	}

	public void loadMetadata(Connection connection) throws SQLException {
		DatabaseMetaData databaseMetaData = connection.getMetaData();

	}

	private List<String> parseTableMetadata(DatabaseMetaData databaseMetaData) throws SQLException {
		List<String> rt = new ArrayList<>();
		ResultSet rSet = databaseMetaData.getTables(null, null, null, null);
		while (rSet.next()) {
			TableMetadata tableMetadata = new TableMetadata();

		}
		return rt;
	}

}
