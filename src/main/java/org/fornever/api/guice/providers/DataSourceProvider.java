package org.fornever.api.guice.providers;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class DataSourceProvider implements Provider<DataSource> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	@Named("druid.maxActive")
	private Integer maxActive;

	@Override
	public DataSource get() {
		DruidDataSource ds = new DruidDataSource();
		ds.setMaxActive(maxActive);
		try {
			ds.init();
		} catch (SQLException e) {
			logger.error("init data source failed", e);
			e.printStackTrace();
			System.exit(1);
		}
		return ds;
	}

}
