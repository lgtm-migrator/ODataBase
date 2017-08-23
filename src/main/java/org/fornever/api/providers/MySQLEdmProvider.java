package org.fornever.api.providers;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAction;
import org.apache.olingo.commons.api.edm.provider.CsdlActionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlAliasInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotations;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlEnumType;
import org.apache.olingo.commons.api.edm.provider.CsdlFunction;
import org.apache.olingo.commons.api.edm.provider.CsdlFunctionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.edm.provider.CsdlSingleton;
import org.apache.olingo.commons.api.edm.provider.CsdlTerm;
import org.apache.olingo.commons.api.edm.provider.CsdlTypeDefinition;
import org.apache.olingo.commons.api.ex.ODataException;

import com.google.inject.Inject;

/**
 * Edm Provider for MySQL Database
 * 
 * @author Theo Sun
 *
 */
public class MySQLEdmProvider implements CsdlEdmProvider {

	@Inject
	private DataSource datasource;

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	@Override
	public CsdlEnumType getEnumType(FullQualifiedName enumTypeName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlTypeDefinition getTypeDefinition(FullQualifiedName typeDefinitionName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
		try {
			String entityName = entityTypeName.getName();
			CsdlEntityType entityType  = new CsdlEntityType();
			DatabaseMetaData databaseMetaData = datasource.getConnection().getMetaData();
			ResultSet columns = databaseMetaData.getColumns(null, null, entityName, null);
			while (columns.next()) {
				CsdlProperty property = new CsdlProperty();
				String columnName = columns.getString("COLUMN_NAME");
				String datatype = columns.getString("DATA_TYPE");
				String columnsize = columns.getString("COLUMN_SIZE");
				String decimaldigits = columns.getString("DECIMAL_DIGITS");
				String isNullable = columns.getString("IS_NULLABLE");
				String isAutoIncrment = columns.getString("IS_AUTOINCREMENT");
				
			}
		} catch (SQLException e) {
			throw new ODataException(e.getMessage());
		}

		return null;
	}

	@Override
	public CsdlComplexType getComplexType(FullQualifiedName complexTypeName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CsdlAction> getActions(FullQualifiedName actionName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CsdlFunction> getFunctions(FullQualifiedName functionName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlTerm getTerm(FullQualifiedName termName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlSingleton getSingleton(FullQualifiedName entityContainer, String singletonName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlActionImport getActionImport(FullQualifiedName entityContainer, String actionImportName)
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlFunctionImport getFunctionImport(FullQualifiedName entityContainer, String functionImportName)
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CsdlAliasInfo> getAliasInfos() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CsdlSchema> getSchemas() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlEntityContainer getEntityContainer() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlAnnotations getAnnotationsGroup(FullQualifiedName targetName, String qualifier) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

}
