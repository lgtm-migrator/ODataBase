package org.fornever.api.types;

public class ForeignKeyMetadata {
	private String pkTable;
	private String fkTable;
	private String pkColumn;
	private String fkColumn;

	/**
	 * @return the pkTable
	 */
	public String getPkTable() {
		return pkTable;
	}

	/**
	 * @param pkTable
	 *            the pkTable to set
	 */
	public void setPkTable(String pkTable) {
		this.pkTable = pkTable;
	}

	/**
	 * @return the fkTable
	 */
	public String getFkTable() {
		return fkTable;
	}

	/**
	 * @param fkTable
	 *            the fkTable to set
	 */
	public void setFkTable(String fkTable) {
		this.fkTable = fkTable;
	}

	/**
	 * @return the pkColumn
	 */
	public String getPkColumn() {
		return pkColumn;
	}

	/**
	 * @param pkColumn
	 *            the pkColumn to set
	 */
	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ForeignKeyMetadata [pkTable=" + pkTable + ", fkTable=" + fkTable + ", pkColumn=" + pkColumn
				+ ", fkColumn=" + fkColumn + "]";
	}

	/**
	 * @return the fkColumn
	 */
	public String getFkColumn() {
		return fkColumn;
	}

	/**
	 * @param fkColumn
	 *            the fkColumn to set
	 */
	public void setFkColumn(String fkColumn) {
		this.fkColumn = fkColumn;
	}

}
