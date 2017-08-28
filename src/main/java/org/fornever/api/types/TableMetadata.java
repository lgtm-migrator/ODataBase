package org.fornever.api.types;

import java.util.ArrayList;
import java.util.List;

public class TableMetadata {

	private List<ColumnMetadata> columns;

	private String tableCatalog;

	private String tableSchema;
	private String tableName;
	private String tableType;
	private String primaryKey;

	private String remarks;

	private String typeCatalog;

	private String typeSchema;

	private String typeName;

	private String selfReferencingColName;

	private String refGeneration;

	/**
	 * This table foreign keys <br>
	 * 此Table的外键
	 */
	private List<ForeignKeyMetadata> foreignKeys;

	/**
	 * The other table foreign keys which reference this table primary key <br>
	 * 其它Table中引用了此Table组件的外键
	 */
	private List<ForeignKeyMetadata> refPkForeignKeys;

	public TableMetadata() {
		this.columns = new ArrayList<>();
		this.foreignKeys = new ArrayList<>();
	}

	public ForeignKeyMetadata getForeignKeyByName(String fkName) {
		for (ForeignKeyMetadata foreignKeyMetadata : foreignKeys) {
			if (fkName.equalsIgnoreCase(foreignKeyMetadata.getKeyName())) {
				return foreignKeyMetadata;
			}
		}
		for (ForeignKeyMetadata foreignKeyMetadata : refPkForeignKeys) {
			if (fkName.equalsIgnoreCase(foreignKeyMetadata.getKeyName())) {
				return foreignKeyMetadata;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableMetadata other = (TableMetadata) obj;
		if (columns == null) {
			if (other.columns != null)
				return false;
		} else if (!columns.equals(other.columns))
			return false;
		if (foreignKeys == null) {
			if (other.foreignKeys != null)
				return false;
		} else if (!foreignKeys.equals(other.foreignKeys))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (refGeneration == null) {
			if (other.refGeneration != null)
				return false;
		} else if (!refGeneration.equals(other.refGeneration))
			return false;
		if (refPkForeignKeys == null) {
			if (other.refPkForeignKeys != null)
				return false;
		} else if (!refPkForeignKeys.equals(other.refPkForeignKeys))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (selfReferencingColName == null) {
			if (other.selfReferencingColName != null)
				return false;
		} else if (!selfReferencingColName.equals(other.selfReferencingColName))
			return false;
		if (tableCatalog == null) {
			if (other.tableCatalog != null)
				return false;
		} else if (!tableCatalog.equals(other.tableCatalog))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (tableSchema == null) {
			if (other.tableSchema != null)
				return false;
		} else if (!tableSchema.equals(other.tableSchema))
			return false;
		if (tableType == null) {
			if (other.tableType != null)
				return false;
		} else if (!tableType.equals(other.tableType))
			return false;
		if (typeCatalog == null) {
			if (other.typeCatalog != null)
				return false;
		} else if (!typeCatalog.equals(other.typeCatalog))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		if (typeSchema == null) {
			if (other.typeSchema != null)
				return false;
		} else if (!typeSchema.equals(other.typeSchema))
			return false;
		return true;
	}

	public ColumnMetadata getColumn(String columnName) {
		ColumnMetadata rt = null;
		for (ColumnMetadata cMeta : getColumns()) {
			if (cMeta.getColumnName().equalsIgnoreCase(columnName)) {
				rt = cMeta;
				break;
			}
		}
		return rt;
	}

	public List<ColumnMetadata> getColumns() {
		return columns;
	}

	public String getEntitySetName() {
		return tableName + "s";
	}

	/**
	 * @return the foreignKeys
	 */
	public List<ForeignKeyMetadata> getForeignKeys() {
		return foreignKeys;
	}

	/**
	 * @return the primaryKey
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}

	public String getRefGeneration() {
		return refGeneration;
	}

	/**
	 * @return the refPkForeignKeys
	 */
	public List<ForeignKeyMetadata> getRefedForeignKeys() {
		return refPkForeignKeys;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columns == null) ? 0 : columns.hashCode());
		result = prime * result + ((foreignKeys == null) ? 0 : foreignKeys.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((refGeneration == null) ? 0 : refGeneration.hashCode());
		result = prime * result + ((refPkForeignKeys == null) ? 0 : refPkForeignKeys.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((selfReferencingColName == null) ? 0 : selfReferencingColName.hashCode());
		result = prime * result + ((tableCatalog == null) ? 0 : tableCatalog.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((tableSchema == null) ? 0 : tableSchema.hashCode());
		result = prime * result + ((tableType == null) ? 0 : tableType.hashCode());
		result = prime * result + ((typeCatalog == null) ? 0 : typeCatalog.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		result = prime * result + ((typeSchema == null) ? 0 : typeSchema.hashCode());
		return result;
	}

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(List<ColumnMetadata> columns) {
		this.columns = columns;
	}

	/**
	 * @param foreignKeys
	 *            the foreignKeys to set
	 */
	public void setForeignKeys(List<ForeignKeyMetadata> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	/**
	 * @param primaryKey
	 *            the primaryKey to set
	 */
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setRefGeneration(String refGeneration) {
		this.refGeneration = refGeneration;
	}

	/**
	 * @param refPkForeignKeys
	 *            the refPkForeignKeys to set
	 */
	public void setRefPkForeignKeys(List<ForeignKeyMetadata> refPkForeignKeys) {
		this.refPkForeignKeys = refPkForeignKeys;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TableMetadata [columns=" + columns + ", tableCatalog=" + tableCatalog + ", tableSchema=" + tableSchema
				+ ", tableName=" + tableName + ", tableType=" + tableType + ", primaryKey=" + primaryKey + ", remarks="
				+ remarks + ", typeCatalog=" + typeCatalog + ", typeSchema=" + typeSchema + ", typeName=" + typeName
				+ ", selfReferencingColName=" + selfReferencingColName + ", refGeneration=" + refGeneration
				+ ", foreignKeys=" + foreignKeys + ", refPkForeignKeys=" + refPkForeignKeys + "]";
	}

}
