package org.fornever.api.database.mysql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.ResultSetHandler;
import org.fornever.api.database.AbstractMetadataParser;
import org.fornever.api.types.ForeignKeyMetadata;
import org.fornever.api.types.TypeConventer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLSchemaMetadataParser extends AbstractMetadataParser {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public List<ForeignKeyMetadata> parseForeignKey(String tableSchema, String tableName, Boolean refedForeignkey)
			throws Exception {
		String sql = "SELECT "
				+ "`CONSTRAINT_NAME`,`TABLE_NAME`,`COLUMN_NAME`,`REFERENCED_TABLE_NAME`,`REFERENCED_COLUMN_NAME` "
				+ "FROM information_schema.`KEY_COLUMN_USAGE` " + "WHERE `REFERENCED_COLUMN_NAME` is not null ";
		if (refedForeignkey) {
			sql += String.format("AND `REFERENCED_TABLE_NAME` = '%s'", tableName);
		} else {
			sql += String.format("AND `TABLE_NAME` = '%s'", tableName);
		}
		return queryRunner.query(sql, (ResultSetHandler<List<ForeignKeyMetadata>>) rs -> {
			List<ForeignKeyMetadata> rt = new ArrayList<>();
			TypeConventer.IMustGetRow<String> stringRow = TypeConventer.mustGetString.apply(rs);
			while (rs.next()) {
				ForeignKeyMetadata aFK = new ForeignKeyMetadata();
				aFK.setKeyName(stringRow.Get("CONSTRAINT_NAME"));
				aFK.setTable(stringRow.Get("TABLE_NAME"));
				aFK.setColumn(stringRow.Get("COLUMN_NAME"));
				aFK.setRefColumn(stringRow.Get("REFERENCED_COLUMN_NAME"));
				aFK.setRefTable(stringRow.Get("REFERENCED_TABLE_NAME"));
				rt.add(aFK);
			}
			return rt;
		});
	}

}
