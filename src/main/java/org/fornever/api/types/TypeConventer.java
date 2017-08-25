package org.fornever.api.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeConventer {

	private static Logger logger = LoggerFactory.getLogger(TypeConventer.class);

	static interface IIntergerRow {
		public Integer Get(String columnName);
	}

	static interface IStringRow {
		public String Get(String columnName);
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

	@SuppressWarnings("deprecation")
	public static FullQualifiedName convertEdmTypeFrom(String udtType) {
		FullQualifiedName rt = null;
		switch (udtType.toUpperCase()) {
		case "BFILE":
		case "LONG RAW":
		case "RAW":
		case "UROWID":
			rt = EdmPrimitiveTypeKind.Binary.getFullQualifiedName();
			break;
		case "BINARY_DOUBLE":
			rt = EdmPrimitiveTypeKind.Double.getFullQualifiedName();
			break;
		case "BINARY_FLOAT":
			rt = EdmPrimitiveTypeKind.Single.getFullQualifiedName();
			break;
		case "NUMBER(1,0)":
		case "NUMBER(2,0)":
		case "NUMBER(3,0)":
		case "NUMBER(4,0)":
		case "NUMBER(5,0)":
		case "SMALLINT":
		case "BIT":
			rt = EdmPrimitiveTypeKind.Int16.getFullQualifiedName();
			break;
		case "BINARY_INTEGER":
		case "NUMBER(6,0)":
		case "NUMBER(7,0)":
		case "NUMBER(8,0)":
		case "NUMBER(9,0)":
		case "NUMBER(10,0)":
		case "INT":
		case "INT UNSIGNED":
			rt = EdmPrimitiveTypeKind.Int32.getFullQualifiedName();
			break;
		case "NUMBER(11,0)":
		case "NUMBER(12,0)":
		case "NUMBER(13,0)":
		case "NUMBER(14,0)":
		case "NUMBER(15,0)":
		case "NUMBER(16,0)":
		case "NUMBER(17,0)":
		case "NUMBER(18,0)":
		case "NUMBER(19,0)":
			rt = EdmPrimitiveTypeKind.Int64.getFullQualifiedName();
			break;
		case "CHAR":
		case "CLOB":
		case "LONG":
		case "NCHAR":
		case "NCLOB":
		case "VARCHAR":
		case "NVARCHAR":
		case "XMLTYPE":
		case "VARCHAR2":
		case "NVARCHAR2":
		case "ROWID":
		case "ENUM":
		case "TEXT":
			rt = EdmPrimitiveTypeKind.String.getFullQualifiedName();
			break;
		case "RAW(16)":
			rt = EdmPrimitiveTypeKind.Guid.getFullQualifiedName();
			break;
		case "DECIMAL":
		case "FLOAT":
		case "NUMBER":
			rt = EdmPrimitiveTypeKind.Decimal.getFullQualifiedName();
			break;
		case "DATE":
		case "DATETIME":
		case "TIMESTAMP":
			rt = EdmPrimitiveTypeKind.Date.getFullQualifiedName();
			break;
		default:
			logger.warn("type {} not a udt type, use string as default", udtType);
			rt = EdmPrimitiveTypeKind.String.getFullQualifiedName();
			break;
		}
		return rt;
	}

	public static String convertUdtTypeFrom(String edmType) {
		String rt = null;

		return rt;
	}

}
