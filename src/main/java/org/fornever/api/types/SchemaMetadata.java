package org.fornever.api.types;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.sql.DataSource;

public class SchemaMetadata {

	private List<TableMetadata> tables;

	public List<TableMetadata> getTables() {
		return tables;
	}

	public SchemaMetadata() {
		this.tables = new ArrayList<>();
	}

	public void loadMetadata(DataSource dataSource) throws SQLException {
		Connection connection = dataSource.getConnection();
		loadMetadata(connection);
		connection.close();
	}

	/**
	 * @param tables
	 *            the tables to set
	 */
	public void setTables(List<TableMetadata> tables) {
		this.tables = tables;
	}

	public void loadMetadata(Connection connection) throws SQLException {
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		this.tables = parseTableMetadata(databaseMetaData);
	}

	private List<TableMetadata> parseTableMetadata(DatabaseMetaData databaseMetaData) throws SQLException {
		List<TableMetadata> rt = new ArrayList<>();
		ResultSet rs = databaseMetaData.getTables(null, null, null, null);
		Function<String, String> getColumnString = JDBCHelper.mustGetString.apply(rs);
		while (rs.next()) {
			TableMetadata tableMetadata = new TableMetadata();
			// some fields maybe not exist, use try but not catch
			tableMetadata.setRefGeneration(getColumnString.apply("REF_GENERATION"));
			tableMetadata.setRemarks(getColumnString.apply("REMARKS"));
			tableMetadata.setSelfReferencingColName(getColumnString.apply("SELF_REFERENCING_COL_NAME"));
			tableMetadata.setTableCatalog(getColumnString.apply("TABLE_CAT"));
			tableMetadata.setTableName(getColumnString.apply("TABLE_NAME"));
			tableMetadata.setTableSchema(getColumnString.apply("TABLE_SCHEM"));
			tableMetadata.setTableSchema(getColumnString.apply("TABLE_SCHEM"));
			tableMetadata.setTableType(getColumnString.apply("TABLE_TYPE"));
			tableMetadata.setTypeCatalog(getColumnString.apply("TYPE_CAT"));
			tableMetadata.setTypeName(getColumnString.apply("TYPE_NAME"));
			tableMetadata.setTypeSchema(getColumnString.apply("TYPE_SCHEM"));
			tableMetadata.loadMetadata(databaseMetaData.getConnection());
			rt.add(tableMetadata);
		}
		return rt;
	}

}
