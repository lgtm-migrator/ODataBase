package org.fornever.api.types;

import java.util.ArrayList;
import java.util.List;
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
