package org.fornever.api.olingo;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.DeserializerResult;
import org.apache.olingo.server.api.deserializer.ODataDeserializer;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.fornever.api.database.mysql.MySQLJDBCHelper;
import org.fornever.api.types.SchemaMetadata;
import org.fornever.api.types.TableMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class EntityProcessorImpl implements EntityProcessor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private OData odata;
	@Inject
	private ServiceMetadata serviceMetadata;
	@Inject
	private DataSource dataSource;
	@Inject
	private SchemaMetadata schemaMetadata;
	@Inject
	private MySQLJDBCHelper jdbcHelper;

	@Override
	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, ODataLibraryException {
		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();

		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
		EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
		List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
		Entity entity = null;
		TableMetadata tableMetadata = schemaMetadata.getTableByEntitySetName(edmEntitySet.getName());
		if (tableMetadata != null) {
			entity = jdbcHelper.retriveEntityByKey(tableMetadata, keyPredicates.get(0).getText());
		}
		EdmEntityType entityType = edmEntitySet.getEntityType();

		ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).build();
		EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextUrl).build();

		ODataSerializer serializer = odata.createSerializer(responseFormat);
		int statusCode = HttpStatusCode.OK.getStatusCode();
		InputStream contentStream = null;

		if (entity != null) {
			contentStream = serializer.entity(serviceMetadata, entityType, entity, options).getContent();
		} else {
			throw new ODataApplicationException(HttpStatusCode.NOT_FOUND.getInfo(),
					HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}
		response.setContent(contentStream);
		response.setStatusCode(statusCode);
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());

	}

	@Override
	public void createEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

		EdmEntitySet edmEntitySet = OlingoUtil.getEdmEntitySet(uriInfo);
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		InputStream requestInputStream = request.getBody();
		ODataDeserializer deserializer = this.odata.createDeserializer(requestFormat);
		DeserializerResult result = deserializer.entity(requestInputStream, edmEntityType);
		Entity requestEntity = result.getEntity();

		TableMetadata tableMetadata = schemaMetadata.getTableByEntitySetName(edmEntitySet.getName());

		if (tableMetadata == null) {
			throw new ODataApplicationException("No such entity", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(),
					Locale.ENGLISH);
		}

		Entity createdEntity = null;
		// validate entity before
		try {
			createdEntity = jdbcHelper.createEntityByTableMetadata(tableMetadata, requestEntity);
		} catch (SQLException e) {
			String msg = "Error happened when create entity";
			logger.error("{}, {}", msg, e.getMessage());
			throw new ODataApplicationException(msg, HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(),
					Locale.ENGLISH);
		}

		if (createdEntity == null) {
			throw new ODataApplicationException("Not find created entity",
					HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
		}

		logger.info("entity created: {} {}", edmEntityType.getName(), createdEntity);

		ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).build();
		EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextUrl).build();

		ODataSerializer serializer = this.odata.createSerializer(responseFormat);
		SerializerResult serializedResponse = serializer.entity(serviceMetadata, edmEntityType, createdEntity, options);

		response.setContent(serializedResponse.getContent());
		response.setStatusCode(HttpStatusCode.CREATED.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	@Override
	public void updateEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
		EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		InputStream requestInputStream = request.getBody();
		ODataDeserializer deserializer = this.odata.createDeserializer(requestFormat);
		DeserializerResult result = deserializer.entity(requestInputStream, edmEntityType);
		Entity requestEntity = result.getEntity();

		List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();

		HttpMethod httpMethod = request.getMethod();

		TableMetadata tableMetadata = schemaMetadata.getTableByEntitySetName(edmEntitySet.getName());

		if (tableMetadata == null) {
			throw new ODataApplicationException("No such entity", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(),
					Locale.ENGLISH);
		}

		try {
			int updated = jdbcHelper.updateEntityByKey(tableMetadata, keyPredicates.get(0).getText(), requestEntity,
					httpMethod);
			if (updated < 1) {
				throw new ODataApplicationException("Not updated any record",
						HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
			}
		} catch (Exception e) {
			String msg = "Error happened when update entity";
			logger.error(msg + ", {}", e.getMessage());
			throw new ODataApplicationException(msg, HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(),
					Locale.ENGLISH, e);
		}

		response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
	}

	@Override
	public void deleteEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo)
			throws ODataApplicationException, ODataLibraryException {
		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
		EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

		List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();

		try {
			jdbcHelper.deleteEntityData(edmEntitySet, OlingoUtil.getPrimaryKeyValue(keyPredicates));
		} catch (SQLException e) {
			throw new ODataApplicationException("Error happened when delete record",
					HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, e);
		}

		response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());

	}

}
