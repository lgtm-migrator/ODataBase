/**
 * 
 */
package org.fornever.api.olingo;

import java.io.InputStream;
import java.util.List;

import javax.sql.DataSource;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlEdmProvider;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.fornever.api.types.MySQLJDBCHelper;
import org.fornever.api.types.SchemaMetadata;
import org.fornever.api.types.TableMetadata;

import com.google.inject.Inject;

/**
 * @author Theo Sun
 *
 */
public class MySQLEntityCollectionProcessor implements EntityCollectionProcessor {

	@Inject
	private OData odata;

	@Inject
	private ServiceMetadata serviceMetadata;

	@Inject
	private CsdlEdmProvider provider;

	@Inject
	private SchemaMetadata schemaMetadata;

	@Inject
	private DataSource dataSource;
	
	@Inject 
	private MySQLJDBCHelper jdbcHelper;

	private EntityCollection getData(EdmEntitySet edmEntitySet) {
		EntityCollection rt = new EntityCollection();
		List<Entity> entities = rt.getEntities();
		TableMetadata tableMetadata = this.schemaMetadata.getTableByEntitySetName(edmEntitySet.getName());
		if (tableMetadata != null) {
			entities.addAll(jdbcHelper.retriveEntities(tableMetadata));
		}
		return rt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.olingo.server.api.processor.Processor#init(org.apache.olingo.
	 * server.api.OData, org.apache.olingo.server.api.ServiceMetadata)
	 */
	@Override
	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.olingo.server.api.processor.EntityCollectionProcessor#
	 * readEntityCollection(org.apache.olingo.server.api.ODataRequest,
	 * org.apache.olingo.server.api.ODataResponse,
	 * org.apache.olingo.server.api.uri.UriInfo,
	 * org.apache.olingo.commons.api.format.ContentType)
	 */
	@Override
	public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
		EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
		EntityCollection entitySet = getData(edmEntitySet);

		ODataSerializer serializer = odata.createSerializer(responseFormat);
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).build();

		final String id = request.getRawBaseUri() + "/" + edmEntitySet.getName();
		EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with().id(id).contextURL(contextUrl)
				.build();
		SerializerResult serializerResult = serializer.entityCollection(serviceMetadata, edmEntityType, entitySet,
				opts);
		InputStream serializedContent = serializerResult.getContent();

		response.setContent(serializedContent);
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());

	}

}
