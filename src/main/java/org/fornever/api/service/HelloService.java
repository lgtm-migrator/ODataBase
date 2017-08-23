package org.fornever.api.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class HelloService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public String getHello() {
		try {
			Statement statement = dataSource.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("select 100");
			while (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return e.getMessage();
		}
		return "hello world !";
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
