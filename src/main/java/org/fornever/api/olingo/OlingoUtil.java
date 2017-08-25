package org.fornever.api.olingo;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;

public class OlingoUtil {
	public static EdmEntitySet getEdmEntitySet(UriInfoResource uriInfo) throws ODataApplicationException {

		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		// To get the entity set we have to interpret all URI segments
		if (!(resourcePaths.get(0) instanceof UriResourceEntitySet)) {
			throw new ODataApplicationException("Invalid resource type for first segment.",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}

		UriResourceEntitySet uriResource = (UriResourceEntitySet) resourcePaths.get(0);

		return uriResource.getEntitySet();
	}
}
