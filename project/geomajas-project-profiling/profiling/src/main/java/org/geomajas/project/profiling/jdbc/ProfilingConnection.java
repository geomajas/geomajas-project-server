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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Wrapping JDBC connection which can be used to profile the time spent communicating with the database.
 *
 * @author Joachim Van der Auwera
 */
public class ProfilingConnection implements Connection {

	private Connection delegate;

	/**
	 * Delegate connection which handles the actual work.
	 *
	 * @param delegate delegate
	 */
	public ProfilingConnection(Connection delegate) {
		this.delegate = delegate;
	}

	@Override
	public Statement createStatement() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingStatement(delegate.createStatement());
		} finally {
			ProfilingDriver.register("Connection.createStatement", System.currentTimeMillis() - start);
		}
	}

	@Override
	public PreparedStatement prepareStatement(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingPreparedStatement(delegate.prepareStatement(s));
		} finally {
			ProfilingDriver.register("Connection.prepareStatement", System.currentTimeMillis() - start);
		}

	}

	@Override
	public CallableStatement prepareCall(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingCallableStatement(delegate.prepareCall(s));
		} finally {
			ProfilingDriver.register("Connection.prepareCall", System.currentTimeMillis() - start);
		}

	}

	@Override
	public String nativeSQL(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.nativeSQL(s);
		} finally {
			ProfilingDriver.register("Connection.nativeSQL", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void setAutoCommit(boolean b) throws SQLException {
		delegate.setAutoCommit(b);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return delegate.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.commit();
		} finally {
			ProfilingDriver.register("Connection.commit", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void rollback() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.rollback();
		} finally {
			ProfilingDriver.register("Connection.rollback", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void close() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.close();
		} finally {
			ProfilingDriver.register("Connection.close", System.currentTimeMillis() - start);
		}

	}

	@Override
	public boolean isClosed() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.isClosed();
		} finally {
			ProfilingDriver.register("Connection.isClosed", System.currentTimeMillis() - start);
		}

	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getMetaData();
		} finally {
			ProfilingDriver.register("Connection.getMetaData", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void setReadOnly(boolean b) throws SQLException {
		delegate.setReadOnly(b);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return delegate.isReadOnly();
	}

	@Override
	public void setCatalog(String s) throws SQLException {
		delegate.setCatalog(s);
	}

	@Override
	public String getCatalog() throws SQLException {
		return delegate.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int i) throws SQLException {
		delegate.setTransactionIsolation(i);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return delegate.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return delegate.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		delegate.clearWarnings();
	}

	@Override
	public Statement createStatement(int i, int i2) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingStatement(delegate.createStatement(i, i2));
		} finally {
			ProfilingDriver.register("Connection.createStatement", System.currentTimeMillis() - start);
		}

	}

	@Override
	public PreparedStatement prepareStatement(String s, int i, int i2) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingPreparedStatement(delegate.prepareStatement(s, i, i2));
		} finally {
			ProfilingDriver.register("Connection.prepareStatement", System.currentTimeMillis() - start);
		}

	}

	@Override
	public CallableStatement prepareCall(String s, int i, int i2) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingCallableStatement(delegate.prepareCall(s, i, i2));
		} finally {
			ProfilingDriver.register("Connection.prepareCall", System.currentTimeMillis() - start);
		}

	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getTypeMap();
		} finally {
			ProfilingDriver.register("Connection.getTypeMap", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void setTypeMap(Map<String, Class<?>> stringClassMap) throws SQLException {
		delegate.setTypeMap(stringClassMap);
	}

	@Override
	public void setHoldability(int i) throws SQLException {
		delegate.setHoldability(i);
	}

	@Override
	public int getHoldability() throws SQLException {
		return delegate.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.setSavepoint();
		} finally {
			ProfilingDriver.register("Connection.setSavePoint", System.currentTimeMillis() - start);
		}

	}

	@Override
	public Savepoint setSavepoint(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.setSavepoint(s);
		} finally {
			ProfilingDriver.register("Connection.setSavePoint", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.rollback(savepoint);
		} finally {
			ProfilingDriver.register("Connection.rollback", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.releaseSavepoint(savepoint);
		} finally {
			ProfilingDriver.register("Connection.releaseSavepoint", System.currentTimeMillis() - start);
		}
	}

	@Override
	public Statement createStatement(int i, int i2, int i3) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingStatement(delegate.createStatement(i, i2, i3));
		} finally {
			ProfilingDriver.register("Connection.createStatement", System.currentTimeMillis() - start);
		}
	}

	@Override
	public PreparedStatement prepareStatement(String s, int i, int i2, int i3) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingPreparedStatement(delegate.prepareStatement(s, i, i2, i3));
		} finally {
			ProfilingDriver.register("Connection.prepareStatement", System.currentTimeMillis() - start);
		}
	}

	@Override
	public CallableStatement prepareCall(String s, int i, int i2, int i3) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingCallableStatement(delegate.prepareCall(s, i, i2, i3));
		} finally {
			ProfilingDriver.register("Connection.prepareCall", System.currentTimeMillis() - start);
		}
	}

	@Override
	public PreparedStatement prepareStatement(String s, int i) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingPreparedStatement(delegate.prepareStatement(s, i));
		} finally {
			ProfilingDriver.register("Connection.prepareStatement", System.currentTimeMillis() - start);
		}

	}

	@Override
	public PreparedStatement prepareStatement(String s, int[] ints) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingPreparedStatement(delegate.prepareStatement(s, ints));
		} finally {
			ProfilingDriver.register("Connection.prepareStatement", System.currentTimeMillis() - start);
		}

	}

	@Override
	public PreparedStatement prepareStatement(String s, String[] strings) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return new ProfilingPreparedStatement(delegate.prepareStatement(s, strings));
		} finally {
			ProfilingDriver.register("Connection.prepareStatement", System.currentTimeMillis() - start);
		}

	}

	@Override
	public Clob createClob() throws SQLException {
		return delegate.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return delegate.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return delegate.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return delegate.createSQLXML();
	}

	@Override
	public boolean isValid(int i) throws SQLException {
		return delegate.isValid(i);
	}

	@Override
	public void setClientInfo(String s, String s2) throws SQLClientInfoException {
		delegate.setClientInfo(s, s2);
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		delegate.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String s) throws SQLException {
		return delegate.getClientInfo(s);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return delegate.getClientInfo();
	}

	@Override
	public Array createArrayOf(String s, Object[] objects) throws SQLException {
		return delegate.createArrayOf(s, objects);
	}

	@Override
	public Struct createStruct(String s, Object[] objects) throws SQLException {
		return delegate.createStruct(s, objects);
	}

	@Override
	public void setSchema(String s) throws SQLException {
		delegate.setSchema(s);
	}

	@Override
	public String getSchema() throws SQLException {
		return delegate.getSchema();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		delegate.abort(executor);
	}

	@Override
	public void setNetworkTimeout(Executor executor, int i) throws SQLException {
		delegate.setNetworkTimeout(executor, i);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return delegate.getNetworkTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> tClass) throws SQLException {
		return delegate.unwrap(tClass);
	}

	@Override
	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		return delegate.isWrapperFor(aClass);
	}
}
