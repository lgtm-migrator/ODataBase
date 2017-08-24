package org.fornever.api.olingo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlAction;
import org.apache.olingo.commons.api.edm.provider.CsdlActionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlAliasInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotations;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlEnumType;
import org.apache.olingo.commons.api.edm.provider.CsdlFunction;
import org.apache.olingo.commons.api.edm.provider.CsdlFunctionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.edm.provider.CsdlSingleton;
import org.apache.olingo.commons.api.edm.provider.CsdlTerm;
import org.apache.olingo.commons.api.edm.provider.CsdlTypeDefinition;
import org.apache.olingo.commons.api.ex.ODataException;
import org.fornever.api.types.ColumnMetadata;
import org.fornever.api.types.SchemaMetadata;
import org.fornever.api.types.TableMetadata;
import org.fornever.api.types.TypeConventer;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Edm Provider for MySQL Database
 * 
 * @author Theo Sun
 *
 */
public class MySQLEdmProvider extends CsdlAbstractEdmProvider {

	private DataSource datasource;

	private SchemaMetadata schemaMetadata;

	@Inject
	@Named("odata.namespace")
	private String nameSpace;

	@Inject
	@Named("odata.container")
	private String containerName;

	@Inject
	public MySQLEdmProvider(DataSource datasource, SchemaMetadata schemaMetadata) throws SQLException {
		super();
		this.datasource = datasource;
		this.schemaMetadata = schemaMetadata;
		this.schemaMetadata.loadMetadata(datasource);
	}

	@Override
	public CsdlActionImport getActionImport(FullQualifiedName entityContainer, String actionImportName)
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CsdlAction> getActions(FullQualifiedName actionName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CsdlAliasInfo> getAliasInfos() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlAnnotations getAnnotationsGroup(FullQualifiedName targetName, String qualifier) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlComplexType getComplexType(FullQualifiedName complexTypeName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlEntityContainer getEntityContainer() throws ODataException {

		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();

		for (TableMetadata tableMetadata : this.schemaMetadata.getTables()) {
			entitySets.add(getEntitySet(new FullQualifiedName(this.nameSpace, this.containerName),
					tableMetadata.getEntitySetName()));
		}

		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(this.containerName);
		entityContainer.setEntitySets(entitySets);

		return entityContainer;
	}

	@Override
	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) throws ODataException {
		if (entityContainerName == null
				|| entityContainerName.equals(new FullQualifiedName(this.nameSpace, this.containerName))) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(new FullQualifiedName(this.nameSpace, this.containerName));
			return entityContainerInfo;
		}
		return null;
	}

	@Override
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
		CsdlEntitySet rt = null;
		if (entityContainer.equals(new FullQualifiedName(this.nameSpace, this.containerName))) {
			TableMetadata tableMetadata = this.schemaMetadata.getTableByEntitySetName(entitySetName);
			if (tableMetadata != null) {
				rt = new CsdlEntitySet();
				rt.setName(tableMetadata.getEntitySetName());
				rt.setType(new FullQualifiedName(this.nameSpace, tableMetadata.getTableName()));
			}
		}
		return rt;
	}

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
		CsdlEntityType rt = null;
		String entityName = entityTypeName.getName();
		TableMetadata tableMetadata = this.schemaMetadata.getTable(entityName);
		if (tableMetadata != null) {
			rt = new CsdlEntityType();
			List<CsdlProperty> properties = new ArrayList<>();

			for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
				CsdlProperty colProp = new CsdlProperty();
				colProp.setName(columnMetadata.getColumnName());
				colProp.setType(TypeConventer.convertEdmTypeFrom(columnMetadata.getTypeName()));
				if (columnMetadata.getColumnSize() != 0) {
					colProp.setMaxLength(columnMetadata.getColumnSize());
				}
				colProp.setNullable(columnMetadata.getNullable() != 0);
				colProp.setDefaultValue(colProp.getDefaultValue());
				properties.add(colProp);
			}

			rt.setName(tableMetadata.getTableName());
			rt.setProperties(properties);

			if (tableMetadata.getPrimaryKey() != null) {
				CsdlPropertyRef propertyRef = new CsdlPropertyRef();
				propertyRef.setName(tableMetadata.getPrimaryKey());
				rt.setKey(Collections.singletonList(propertyRef));
			}

		}
		return rt;
	}

	@Override
	public CsdlEnumType getEnumType(FullQualifiedName enumTypeName) throws ODataException {
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
	public List<CsdlFunction> getFunctions(FullQualifiedName functionName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the schemaMetadata
	 */
	public SchemaMetadata getSchemaMetadata() {
		return schemaMetadata;
	}

	@Override
	public List<CsdlSchema> getSchemas() throws ODataException {
		// create Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(this.nameSpace);

		// add EntityTypes
		List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		for (TableMetadata tableMetadata : getSchemaMetadata().getTables()) {
			entityTypes.add(getEntityType(new FullQualifiedName(this.nameSpace, tableMetadata.getTableName())));
		}
		schema.setEntityTypes(entityTypes);

		schema.setEntityContainer(getEntityContainer());

		// finally
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}

	@Override
	public CsdlSingleton getSingleton(FullQualifiedName entityContainer, String singletonName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlTerm getTerm(FullQualifiedName termName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CsdlTypeDefinition getTypeDefinition(FullQualifiedName typeDefinitionName) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param schemaMetadata
	 *            the schemaMetadata to set
	 */
	public void setSchemaMetadata(SchemaMetadata schemaMetadata) {
		this.schemaMetadata = schemaMetadata;
	}

}
