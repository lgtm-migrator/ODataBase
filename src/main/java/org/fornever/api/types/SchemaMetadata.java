package org.fornever.api.types;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class SchemaMetadata {

	@Inject
	private QueryRunner runner;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<TableMetadata> tables;

	public SchemaMetadata() {
		this.tables = new ArrayList<>();
	}

	public TableMetadata getTable(String tableName) {
		TableMetadata rt = null;
		for (TableMetadata tMeta : getTables()) {
			if (tMeta.getTableName().equalsIgnoreCase(tableName)) {
				rt = tMeta;
				break;
			}
		}
		return rt;
	}

	public TableMetadata getTableByEntitySetName(String entitySetName) {
		TableMetadata rt = null;
		for (TableMetadata tMeta : getTables()) {
			if (tMeta.getEntitySetName().equalsIgnoreCase(entitySetName)) {
				rt = tMeta;
				break;
			}
		}
		return rt;
	}

	public List<TableMetadata> getTables() {
		return tables;
	}

	public void loadMetadata(Connection connection) throws SQLException {
		this.logger.info("start load database metadata");
		this.tables = new ArrayList<>();
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet rs = databaseMetaData.getTables(null, null, null, null);
		TypeConventer.IStringRow stringRow = TypeConventer.mustGetString.apply(rs);
		while (rs.next()) {
			TableMetadata tableMetadata = new TableMetadata();
			// some fields maybe not exist, use try but not catch
			tableMetadata.setRefGeneration(stringRow.Get("REF_GENERATION"));
			tableMetadata.setRemarks(stringRow.Get("REMARKS"));
			tableMetadata.setSelfReferencingColName(stringRow.Get("SELF_REFERENCING_COL_NAME"));
			tableMetadata.setTableCatalog(stringRow.Get("TABLE_CAT"));
			tableMetadata.setTableName(stringRow.Get("TABLE_NAME"));
			tableMetadata.setTableSchema(stringRow.Get("TABLE_SCHEM"));
			tableMetadata.setTableSchema(stringRow.Get("TABLE_SCHEM"));
			tableMetadata.setTableType(stringRow.Get("TABLE_TYPE"));
			tableMetadata.setTypeCatalog(stringRow.Get("TYPE_CAT"));
			tableMetadata.setTypeName(stringRow.Get("TYPE_NAME"));
			tableMetadata.setTypeSchema(stringRow.Get("TYPE_SCHEM"));
			tableMetadata.loadMetadata(databaseMetaData.getConnection());
			this.tables.add(tableMetadata);
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SchemaMetadata [" + (tables != null ? "tables=" + tables : "") + "]";
	}

}
