package org.fornever.api.guice.providers;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class QueryRunnerProvider implements Provider<QueryRunner> {

	@Inject
	DataSource datasource;

	@Override
	public QueryRunner get() {
		QueryRunner runner = new QueryRunner(datasource);
		return runner;
	}

}
