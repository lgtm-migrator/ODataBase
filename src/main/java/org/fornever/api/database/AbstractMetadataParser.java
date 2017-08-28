/**
 * 
 */
package org.fornever.api.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.fornever.api.types.ColumnMetadata;
import org.fornever.api.types.ForeignKeyMetadata;
import org.fornever.api.types.SchemaMetadata;
import org.fornever.api.types.TableMetadata;
import org.fornever.api.types.TypeConventer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * @author theos
 *
 */
public abstract class AbstractMetadataParser {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	protected DataSource dataSource;

	@Inject
	protected QueryRunner queryRunner;

	protected interface ConnectionRunner<T> {
		T run(Connection connection) throws Exception;
	}

	protected interface DatabaseMetaDataRunner<T> {
		T run(DatabaseMetaData metaData) throws Exception;
	}

	/**
	 * Run with connection, auto closed after running
	 * 
	 * @param runner
	 * @throws Exception
	 */
	protected <T> T runWithConnection(ConnectionRunner<T> runner) throws Exception {
		Connection connection = dataSource.getConnection();
		T rt = null;
		try {
			rt = runner.run(connection);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		return rt;
	}

	protected <T> T runWithDatabaseMetaData(DatabaseMetaDataRunner<T> runner) throws Exception {
		return runWithConnection(conn -> {
			DatabaseMetaData metaData = conn.getMetaData();
			return runner.run(metaData);
		});
	}

	/**
	 * parseTableOwnedForeignKey
	 * 
	 * @param tableSchema
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<ForeignKeyMetadata> parseTableOwnedForeignKey(String tableSchema, String tableName) throws Exception {
		return parseForeignKey(tableSchema, tableName, false);
	}

	/**
	 * parseTableBeReferencedForeignKey
	 * 
	 * @param tableSchema
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<ForeignKeyMetadata> parseTableBeReferencedForeignKey(String tableSchema, String tableName)
			throws Exception {
		return parseForeignKey(tableSchema, tableName, true);
	}

	/**
	 * parseForeignKey for a table
	 * 
	 * 
	 * @param tableSchema
	 *            table schema/database name, can be null
	 * @param tableName
	 *            table name
	 * @param refedForeignkey
	 *            retrieve be referenced foreign key ?
	 * @return
	 * @throws Exception
	 */
	abstract public List<ForeignKeyMetadata> parseForeignKey(String tableSchema, String tableName,
			Boolean refedForeignkey) throws Exception;

	/**
	 * parse current schema metadata
	 * 
	 * @return schema metadata instance
	 * @throws Exception
	 */
	public SchemaMetadata parseSchemaMetadata() throws Exception {
		logger.info("parsing schema metadata");
		return runWithConnection((ConnectionRunner<SchemaMetadata>) (conn) -> {
			SchemaMetadata rt = new SchemaMetadata();
			List<TableMetadata> tables = new ArrayList<>();
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			ResultSet rs = databaseMetaData.getTables(null, null, null, null);
			TypeConventer.IMustGetRow<String> stringRow = TypeConventer.mustGetString.apply(rs);
			while (rs.next()) {
				TableMetadata tablemeta = new TableMetadata();
				// some fields maybe not exist, use try but not catch
				tablemeta.setRefGeneration(stringRow.Get("REF_GENERATION"));
				tablemeta.setRemarks(stringRow.Get("REMARKS"));
				tablemeta.setSelfReferencingColName(stringRow.Get("SELF_REFERENCING_COL_NAME"));
				tablemeta.setTableCatalog(stringRow.Get("TABLE_CAT"));
				tablemeta.setTableName(stringRow.Get("TABLE_NAME"));
				tablemeta.setTableSchema(stringRow.Get("TABLE_SCHEM"));
				tablemeta.setTableSchema(stringRow.Get("TABLE_SCHEM"));
				tablemeta.setTableType(stringRow.Get("TABLE_TYPE"));
				tablemeta.setTypeCatalog(stringRow.Get("TYPE_CAT"));
				tablemeta.setTypeName(stringRow.Get("TYPE_NAME"));
				tablemeta.setTypeSchema(stringRow.Get("TYPE_SCHEM"));
				// parse table columns
				tablemeta.setColumns(parseTableColumns(tablemeta.getTableSchema(), tablemeta.getTableName()));
				// parse table primary key name
				tablemeta.setPrimaryKey(parseTablePrimaryKey(tablemeta.getTableSchema(), tablemeta.getTableName()));
				// parse foreign keys
				tablemeta.setForeignKeys(
						parseTableOwnedForeignKey(tablemeta.getTableSchema(), tablemeta.getTableName()));
				tablemeta.setRefPkForeignKeys(
						parseTableBeReferencedForeignKey(tablemeta.getTableSchema(), tablemeta.getTableName()));
				tables.add(tablemeta);
			}
			rs.close();
			rt.setTables(tables);
			return rt;
		});
	}

	/**
	 * parse tableMetadata columns
	 * 
	 * @param tableName
	 *            table name
	 * @param schemaName
	 *            schema(database) name
	 * @return
	 * @throws Exception
	 */
	public List<ColumnMetadata> parseTableColumns(String schemaName, String tableName) throws Exception {
		return runWithConnection((ConnectionRunner<List<ColumnMetadata>>) (connection) -> {
			List<ColumnMetadata> columns = new ArrayList<>();
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet rs = metaData.getColumns(null, schemaName, tableName, null);
			TypeConventer.IMustGetRow<String> stringRow = TypeConventer.mustGetString.apply(rs);
			TypeConventer.IMustGetRow<Integer> intergerRow = TypeConventer.mustGetInteger.apply(rs);
			while (rs.next()) {
				ColumnMetadata columnMetadata = new ColumnMetadata();
				columnMetadata.setCharOcetLength(intergerRow.Get("CHAR_OCTET_LENGTH"));
				columnMetadata.setColumnName(stringRow.Get("COLUMN_NAME"));
				columnMetadata.setColumnSize(intergerRow.Get("COLUMN_SIZE"));
				columnMetadata.setDataType(intergerRow.Get("DATA_TYPE"));
				columnMetadata.setDecimalDigits(intergerRow.Get("DECIMAL_DIGITS"));
				columnMetadata.setDefaultValue(stringRow.Get("COLUMN_DEF"));
				columnMetadata.setIsGenrationColumn(stringRow.Get("IS_GENERATEDCOLUMN"));
				columnMetadata.setIsNullable(stringRow.Get("IS_NULLABLE"));
				columnMetadata.setNullable(intergerRow.Get("NULLABLE"));
				columnMetadata.setNumPrexRadix(intergerRow.Get("NUM_PREC_RADIX"));
				columnMetadata.setOridinalPosition(intergerRow.Get("ORDINAL_POSITION"));
				columnMetadata.setRemarks(stringRow.Get("REMARKS"));
				columnMetadata.setTableCatalog(stringRow.Get("TABLE_CAT"));
				columnMetadata.setTableName(stringRow.Get("TABLE_NAME"));
				columnMetadata.setTableSchema(stringRow.Get("TABLE_SCHEM"));
				columnMetadata.setTypeName(stringRow.Get("TYPE_NAME"));
				columns.add(columnMetadata);
			}
			rs.close();
			return columns;
		});
	}

	/**
	 * parseTablePrimaryKey
	 * 
	 * @param schemName
	 *            schema name, can be null
	 * @param tableName
	 *            table name
	 * @return
	 * @throws Exception
	 */
	public String parseTablePrimaryKey(String schemName, String tableName) throws Exception {
		return runWithDatabaseMetaData((DatabaseMetaDataRunner<String>) (meta) -> {
			String rt = null;
			ResultSet rs = meta.getPrimaryKeys(null, schemName, tableName);
			while (rs.next()) {
				TypeConventer.IMustGetRow<String> stringRow = TypeConventer.mustGetString.apply(rs);
				rt = stringRow.Get("COLUMN_NAME");
			}
			rs.close();
			return rt;
		});
	}
}
