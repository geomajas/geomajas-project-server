/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.project.profiling.jdbc;

import org.geomajas.annotation.Api;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Wrapping JDBC driver which can be used to profile the time spent communicating with the database.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class ProfilingDriver implements Driver {

	private static final String PREFIX = "profiling:";

	private static final List<ProfilingListener> LISTENERS = new CopyOnWriteArrayList<ProfilingListener>();

	static {
		try {
			DriverManager.registerDriver(new ProfilingDriver());
		} catch (SQLException se) {
			throw new IllegalStateException("Cannot register ProfilingDriver SQL driver.", se);
		}
	}

	/**
	 * Add a new listener to the list of profiling listeners.
	 *
	 * @param listener listener to add
	 */
	@Api
	public static void addListener(ProfilingListener listener) {
		LISTENERS.add(listener);
	}

	/**
	 * Register a duration in milliseconds for running a JDBC method.
	 *
	 * @param group indication of type of command.
	 * @param durationMillis duration in milliseconds
	 */
	static void register(String group, long durationMillis) {
		for (ProfilingListener listener : LISTENERS) {
			listener.register(group, durationMillis);
		}
	}

	@Override
	public Connection connect(String s, Properties properties) throws SQLException {
		if (acceptsURL(s)) {
			long start = System.currentTimeMillis();
			try {
				Connection connection = DriverManager.getConnection(s.substring(PREFIX.length()), properties);
				if (null != connection) {
					return (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
							new Class[] { Connection.class },
							new ConnectionInvocationHandler(connection));
				}
			} finally {
				register("Driver.connect", System.currentTimeMillis() - start);
			}
		}
		return null;
	}

	@Override
	public boolean acceptsURL(String s) throws SQLException {
		return s.startsWith(PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

	/**
	 * Get parent logger, included for Java7.
	 *
	 * @return parent logger
	 * @throws SQLFeatureNotSupportedException feature not supported
	 */
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}
}
