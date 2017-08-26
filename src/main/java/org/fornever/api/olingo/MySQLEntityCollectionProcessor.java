/**
 * 
 */
package org.fornever.api.olingo;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmBindingTarget;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationPropertyBinding;
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
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.olingo.server.api.processor.Processor#init(org.apache.olingo.
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
		Integer segmentCount = resourcePaths.size();

		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
		EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();
		EntityCollection entitySet = new EntityCollection();
		List<Entity> entities = entitySet.getEntities();

		switch (segmentCount) {
		case 1:
			entities.addAll(jdbcHelper.retriveEntities(startEdmEntitySet));
			break;
//		case 2:
//			UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) resourcePaths.get(1);
//			EdmBindingTarget target = startEdmEntitySet.getRelatedBindingTarget(resourcePaths.get(1).toString());
//			EdmEntitySet secondEdmEntitySet = ((UriResourceEntitySet) resourcePaths.get(1)).getEntitySet();
//			String key = OlingoUtil.getPrimaryKeyValue(uriResourceEntitySet.getKeyPredicates());
//			try {
//				Entity startEntity = jdbcHelper.retriveEntityByKey(startEdmEntitySet, key);
//				List<Entity> relatedEntities = jdbcHelper.retriveRelatedEntiteis(startEdmEntitySet, key,
//						secondEdmEntitySet);
//				EdmNavigationPropertyBinding navigationBinding = null;
//				for (EdmNavigationPropertyBinding binding : startEdmEntitySet.getNavigationPropertyBindings()) {
//					if (binding.getTarget().equalsIgnoreCase(
//							secondEdmEntitySet.getEntityType().getFullQualifiedName().getFullQualifiedNameAsString())) {
//
//					}
//				}
//
//			} catch (SQLException e) {
//				throw new ODataApplicationException("Retrive Error",
//						HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
//			}
//			break;
		default:
			throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ENGLISH);
		}

		ODataSerializer serializer = odata.createSerializer(responseFormat);
		EdmEntityType edmEntityType = startEdmEntitySet.getEntityType();
		ContextURL contextUrl = ContextURL.with().entitySet(startEdmEntitySet).build();

		final String id = request.getRawBaseUri() + "/" + startEdmEntitySet.getName();
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
