package org.fornever.api.types;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class TableMetadata {

	private List<ColumnMetadata> columns;

	private String tableCatalog;

	private String tableSchema;
	private String tableName;
	private String tableType;
	private String remarks;

	private String typeCatalog;

	private String typeSchema;

	private String typeName;

	private String selfReferencingColName;

	private String refGeneration;

	public TableMetadata() {
		this.columns = new ArrayList<>();
	}

	public List<ColumnMetadata> getColumns() {
		return columns;
	}

	public String getRefGeneration() {
		return refGeneration;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getSelfReferencingColName() {
		return selfReferencingColName;
	}

	public String getTableCatalog() {
		return tableCatalog;
	}

	public String getTableName() {
		return tableName;
	}

	public String getTableSchema() {
		return tableSchema;
	}

	public String getTableType() {
		return tableType;
	}

	public String getTypeCatalog() {
		return typeCatalog;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getTypeSchema() {
		return typeSchema;
	}

	public void loadMetadata(Connection connection) {

	}

	public void setRefGeneration(String refGeneration) {
		this.refGeneration = refGeneration;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setSelfReferencingColName(String selfReferencingColName) {
		this.selfReferencingColName = selfReferencingColName;
	}

	public void setTableCatalog(String tableCatalog) {
		this.tableCatalog = tableCatalog;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public void setTypeCatalog(String typeCatalog) {
		this.typeCatalog = typeCatalog;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setTypeSchema(String typeSchema) {
		this.typeSchema = typeSchema;
	}
}
