package org.fornever.api.types;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.sql.DataSource;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCHelper {

	private static Logger logger = LoggerFactory.getLogger(JDBCHelper.class);

	static interface IStringRow {
		public String Get(String columnName);
	}

	static interface IIntergerRow {
		public Integer Get(String columnName);
	}

	public static Function<ResultSet, IStringRow> mustGetString = (rs) -> (columnName) -> {
		try {
			return rs.getString(columnName);
		} catch (SQLException e) {
			// no error
		}
		return null;
	};

	public static Function<ResultSet, IIntergerRow> mustGetInteger = (rs) -> (columnName) -> {
		try {
			return new Integer(rs.getInt(columnName));
		} catch (SQLException e) {
			// no error
		}
		return null;
	};

	public static List<Entity> retriveEntityByTableMetadata(TableMetadata tableMetadata, DataSource dataSource) {
		List<Entity> rt = new ArrayList<>();
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(String.format("select * from `%s`", tableMetadata.getTableName()));
			while (rs.next()) {
				rt.add(parseEntityFromRow(rs, tableMetadata));
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			logger.error("Error happened when retrive entitiy {}", tableMetadata.getTableName());
			e.printStackTrace();
		}
		return rt;
	}

	public static Entity parseEntityFromRow(ResultSet rs, TableMetadata tableMetadata) {
		Entity entity = new Entity();
		IStringRow stringRow = mustGetString.apply(rs);
		for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
			String columnName = columnMetadata.getColumnName();
			entity.addProperty(new Property(null, columnName, ValueType.PRIMITIVE, stringRow.Get(columnName)));
		}
		if (tableMetadata.getPrimaryKey() != null) {
			try {
				entity.setId(new URI(String.format("%s(%s)", tableMetadata.getEntitySetName(),
						stringRow.Get(tableMetadata.getPrimaryKey()))));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	public static Object getValueByColumnMetadata(ResultSet rs, ColumnMetadata columnMetadata) {
		Object rt = null;
		try {
			switch (columnMetadata.getDataType()) {
			// TODO
			default:
				break;
			}
		} catch (Exception e) {
			
		}
		return rt;
	}

}
