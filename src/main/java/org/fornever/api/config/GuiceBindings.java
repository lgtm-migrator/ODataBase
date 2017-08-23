package org.fornever.api.config;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * This class is used for binding interface to its' implementation
 * 
 * @author Theo Sun
 *
 */
public class GuiceBindings extends AbstractModule {

	@Override
	protected void configure() {
		bind(DataSource.class).to(DruidDataSource.class).in(Scopes.SINGLETON);
	}

}
