package org.fornever.api.types;

import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import org.apache.olingo.server.api.prefer.Preferences.Return;

public class JDBCHelper {

	public static Function<ResultSet, Function<String, String>> mustGetString = (rs) -> (columnName) -> {
		try {
			return rs.getString(columnName);
		} catch (SQLException e) {
			// no error
		}
		return null;
	};

	public static Function<ResultSet, Function<String, Integer>> mustGetInteger = (rs) -> (columnName) -> {
		try {
			return new Integer(rs.getInt(columnName));
		} catch (SQLException e) {
			// no error
		}
		return null;
	};

}
