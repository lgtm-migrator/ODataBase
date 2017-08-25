package org.fornever.api.types;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.fornever.api.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ca.krasnay.sqlbuilder.InsertBuilder;
import ca.krasnay.sqlbuilder.SelectBuilder;
import ca.krasnay.sqlbuilder.UpdateBuilder;

public class MySQLJDBCHelper {

	@Inject
	private QueryRunner queryRunner;

	private Logger logger = LoggerFactory.getLogger(MySQLJDBCHelper.class);

	public Entity parseEntityFromRow(ResultSet rs, TableMetadata tableMetadata) throws SQLException {
		Entity entity = new Entity();
		for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
			String columnName = columnMetadata.getColumnName();
			entity.addProperty(new Property(null, columnName, ValueType.PRIMITIVE, rs.getObject(columnName)));
		}
		if (tableMetadata.getPrimaryKey() != null) {
			try {
				entity.setId(new URI(String.format("%s(%s)", tableMetadata.getEntitySetName(),
						rs.getString(tableMetadata.getPrimaryKey()))));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	public Entity createEntityByTableMetadata(TableMetadata tableMetadata, Entity entity) throws SQLException {
		InsertBuilder builder = new InsertBuilder(tableMetadata.getTableName());

		for (Property property : entity.getProperties()) {
			builder.set("`" + property.getName() + "`", "'" + property.getValue() + "'");
		}

		String sql = builder.toString();

		QueryRunner runner = queryRunner;

		Object insertedId = runner.insert(sql, new ScalarHandler<>());
		return retriveEntityByKey(tableMetadata, insertedId);
	}

	public Entity retriveEntityByKey(TableMetadata tableMetadata, Object primaryKeyValue) {
		Entity rt = null;

		QueryRunner runner = queryRunner;

		SelectBuilder builder = new SelectBuilder(tableMetadata.getTableName());
		builder.where(String.format("`%s` = '%s'", tableMetadata.getPrimaryKey(), primaryKeyValue.toString()));
		String sql = builder.toString();

		try {
			rt = runner.query(sql, new ResultSetHandler<Entity>() {

				@Override
				public Entity handle(ResultSet rs) throws SQLException {
					if (rs.next()) {
						return parseEntityFromRow(rs, tableMetadata);
					} else
						return null;
				}
			});
		} catch (SQLException e1) {
			logger.error("Error happened when retrive entity, sql is: {}", sql);
			e1.printStackTrace();
		}

		return rt;
	}

	public List<Entity> retriveEntities(TableMetadata tableMetadata) {
		List<Entity> rt = new ArrayList<>();
		QueryRunner runner = queryRunner;

		SelectBuilder builder = new SelectBuilder(tableMetadata.getTableName());
		String sql = builder.toString();

		try {
			rt = runner.query(sql, new ResultSetHandler<List<Entity>>() {

				@Override
				public List<Entity> handle(ResultSet rs) throws SQLException {
					List<Entity> entities = new ArrayList<>();
					while (rs.next()) {
						entities.add(parseEntityFromRow(rs, tableMetadata));
					}
					return entities;
				}

			});
		} catch (SQLException e1) {
			logger.error("Error happened when retrive entitiy {}, sql is: {}", tableMetadata.getTableName(), sql);
			e1.printStackTrace();
		}

		return rt;
	}

	public Integer updateEntityByKey(TableMetadata tableMetadata, String priKeyValue, Entity entity,
			HttpMethod httpMethod) throws Exception {
		UpdateBuilder builder = new UpdateBuilder(tableMetadata.getTableName());
		QueryRunner runner = Entry.injector.getInstance(QueryRunner.class);

		if (httpMethod == HttpMethod.PATCH) {
			for (Property property : entity.getProperties()) {
				// if not primary key
				if (!tableMetadata.getPrimaryKey().equalsIgnoreCase(property.getName())) {
					builder.set(String.format("`%s` = '%s'", property.getName(), property.getValue()));
				}
			}
		} else if (httpMethod == HttpMethod.PUT) {
			for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
				Property property = entity.getProperty(columnMetadata.getColumnName());
				Boolean isPrimaryColumn = tableMetadata.getPrimaryKey()
						.equalsIgnoreCase(columnMetadata.getColumnName());
				// skip primary key column
				if (!isPrimaryColumn) {
					if (property != null) {
						builder.set(String.format("`%s` = '%s'", columnMetadata.getColumnName(),
								property.getValue().toString()));
					} else {
						builder.set(String.format("`%s` = null", columnMetadata.getColumnName()));
					}
				}

			}
		} else {
			throw new Exception("Not support HTTP method !");
		}

		builder.where(String.format("`%s` = '%s'", tableMetadata.getPrimaryKey(), priKeyValue));
		return runner.update(builder.toString());

	}

}
