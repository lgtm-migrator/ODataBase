package org.fornever.api.types;

public class ForeignKeyMetadata {
	private String keyName;
	private String table;

	/**
	 * @return the keyName
	 */
	public String getKeyName() {
		return keyName;
	}

	/**
	 * @param keyName
	 *            the keyName to set
	 */
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * @return the refTable
	 */
	public String getRefTable() {
		return refTable;
	}

	/**
	 * @param refTable
	 *            the refTable to set
	 */
	public void setRefTable(String refTable) {
		this.refTable = refTable;
	}

	/**
	 * @return the refColumn
	 */
	public String getRefColumn() {
		return refColumn;
	}

	/**
	 * @param refColumn
	 *            the refColumn to set
	 */
	public void setRefColumn(String refColumn) {
		this.refColumn = refColumn;
	}

	private String column;
	private String refTable;
	private String refColumn;

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
		ForeignKeyMetadata other = (ForeignKeyMetadata) obj;
		if (refColumn == null) {
			if (other.refColumn != null)
				return false;
		} else if (!refColumn.equals(other.refColumn))
			return false;
		if (refTable == null) {
			if (other.refTable != null)
				return false;
		} else if (!refTable.equals(other.refTable))
			return false;
		if (keyName == null) {
			if (other.keyName != null)
				return false;
		} else if (!keyName.equals(other.keyName))
			return false;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
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
		result = prime * result + ((refColumn == null) ? 0 : refColumn.hashCode());
		result = prime * result + ((refTable == null) ? 0 : refTable.hashCode());
		result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ForeignKeyMetadata [keyName=" + keyName + ", pkTable=" + table + ", fkTable=" + refTable + ", pkColumn="
				+ column + ", fkColumn=" + refColumn + "]";
	}

}
