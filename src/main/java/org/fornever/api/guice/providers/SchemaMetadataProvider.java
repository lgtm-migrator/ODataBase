package org.fornever.api.guice.providers;

import org.fornever.api.database.mysql.MySQLSchemaMetadataParser;
import org.fornever.api.types.SchemaMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SchemaMetadataProvider implements Provider<SchemaMetadata> {

	@Inject
	MySQLSchemaMetadataParser metadataParser;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public SchemaMetadata get() {
		SchemaMetadata rt = null;
		try {
			rt = metadataParser.parseSchemaMetadata();
		} catch (Exception e) {
			logger.error("error happened when parse schema metadata", e);
			e.printStackTrace();
		}
		return rt;
	}

}
