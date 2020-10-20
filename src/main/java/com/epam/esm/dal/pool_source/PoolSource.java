package com.epam.esm.dal.pool_source;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.epam.esm.dal.exception.DataSourceNamingException;

@Component
public class PoolSource {

	private final DataSource dataSource;

	private final static String envContextLocation = "java:/comp/env";
	private final static String resourceName = "jdbc/mjc-giftService";

	public PoolSource() throws DataSourceNamingException {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup(envContextLocation);
			this.dataSource = (DataSource) envContext.lookup(resourceName);
		} catch (NamingException e) {
			throw new DataSourceNamingException();
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

}
