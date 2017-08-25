package org.fornever.api.types;

public class ColumnMetadata {

	private String tableCatalog;
	private String tableSchema;
	private String tableName;
	private String columnName;
	private Integer dataType;
	private String typeName;
	private Integer columnSize;
	private Integer decimalDigits;
	private Integer numPrexRadix;
	private Integer nullable;
	private String remarks;
	private String defaultValue;
	private Integer charOcetLength;
	private Integer oridinalPosition;
	private String isNullable;
	private String isGenrationColumn;

	public ColumnMetadata() {

	}

	public Integer getCharOcetLength() {
		return charOcetLength;
	}

	public String getColumnName() {
		return columnName;
	}

	public Integer getColumnSize() {
		return columnSize;
	}

	public Integer getDataType() {
		return dataType;
	}

	public Integer getDecimalDigits() {
		return decimalDigits;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getIsGenrationColumn() {
		return isGenrationColumn;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public Integer getNullable() {
		return nullable;
	}

	public Integer getNumPrexRadix() {
		return numPrexRadix;
	}

	public Integer getOridinalPosition() {
		return oridinalPosition;
	}

	public String getRemarks() {
		return remarks;
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

	public String getTypeName() {
		return typeName;
	}

	public void setCharOcetLength(Integer charOcetLength) {
		this.charOcetLength = charOcetLength;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setColumnSize(Integer columnSize) {
		this.columnSize = columnSize;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public void setDecimalDigits(Integer decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setIsGenrationColumn(String isGenrationColumn) {
		this.isGenrationColumn = isGenrationColumn;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public void setNullable(Integer nullable) {
		this.nullable = nullable;
	}

	public void setNumPrexRadix(Integer numPrexRadix) {
		this.numPrexRadix = numPrexRadix;
	}

	public void setOridinalPosition(Integer oridinalPosition) {
		this.oridinalPosition = oridinalPosition;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ColumnMetadata [" + (tableCatalog != null ? "tableCatalog=" + tableCatalog + ", " : "")
				+ (tableSchema != null ? "tableSchema=" + tableSchema + ", " : "")
				+ (tableName != null ? "tableName=" + tableName + ", " : "")
				+ (columnName != null ? "columnName=" + columnName + ", " : "")
				+ (dataType != null ? "dataType=" + dataType + ", " : "")
				+ (typeName != null ? "typeName=" + typeName + ", " : "")
				+ (columnSize != null ? "columnSize=" + columnSize + ", " : "")
				+ (decimalDigits != null ? "decimalDigits=" + decimalDigits + ", " : "")
				+ (numPrexRadix != null ? "numPrexRadix=" + numPrexRadix + ", " : "")
				+ (nullable != null ? "nullable=" + nullable + ", " : "")
				+ (remarks != null ? "remarks=" + remarks + ", " : "")
				+ (defaultValue != null ? "defaultValue=" + defaultValue + ", " : "")
				+ (charOcetLength != null ? "charOcetLength=" + charOcetLength + ", " : "")
				+ (oridinalPosition != null ? "oridinalPosition=" + oridinalPosition + ", " : "")
				+ (isNullable != null ? "isNullable=" + isNullable + ", " : "")
				+ (isGenrationColumn != null ? "isGenrationColumn=" + isGenrationColumn : "") + "]";
	}

}
