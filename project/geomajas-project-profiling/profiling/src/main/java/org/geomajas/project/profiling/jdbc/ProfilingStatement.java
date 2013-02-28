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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Wrapping JDBC statement which can be used to profile the time spent communicating with the database.
 *
 * @author Joachim Van der Auwera
 */
public class ProfilingStatement implements Statement {

	private Statement delegate;

	/**
	 * Delegate statement which handles the actual work.
	 *
	 * @param delegate delegate
	 */
	public ProfilingStatement(Statement delegate) {
		this.delegate = delegate;
	}

	@Override
	public ResultSet executeQuery(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.executeQuery(s);
		} finally {
			ProfilingDriver.register("Statement.executeQuery", System.currentTimeMillis() - start);
		}
	}

	@Override
	public int executeUpdate(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.executeUpdate(s);
		} finally {
			ProfilingDriver.register("Statement.executeUpdate", System.currentTimeMillis() - start);
		}
	}

	@Override
	public void close() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.close();
		} finally {
			ProfilingDriver.register("Statement.close", System.currentTimeMillis() - start);
		}
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return delegate.getMaxFieldSize();
	}

	@Override
	public void setMaxFieldSize(int i) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.setMaxFieldSize(i);
		} finally {
			ProfilingDriver.register("Statement.setMaxFieldSize", System.currentTimeMillis() - start);
		}
	}

	@Override
	public int getMaxRows() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getMaxRows();
		} finally {
			ProfilingDriver.register("Statement.getMaxRows", System.currentTimeMillis() - start);
		}
	}

	@Override
	public void setMaxRows(int i) throws SQLException {
		delegate.setMaxRows(i);
	}

	@Override
	public void setEscapeProcessing(boolean b) throws SQLException {
		delegate.setEscapeProcessing(b);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return delegate.getQueryTimeout();
	}

	@Override
	public void setQueryTimeout(int i) throws SQLException {
		delegate.setQueryTimeout(i);
	}

	@Override
	public void cancel() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.cancel();
		} finally {
			ProfilingDriver.register("Statement.cancel", System.currentTimeMillis() - start);
		}
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
	public void setCursorName(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.setCursorName(s);
		} finally {
			ProfilingDriver.register("Statement.setCursorName", System.currentTimeMillis() - start);
		}
	}

	@Override
	public boolean execute(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.execute(s);
		} finally {
			ProfilingDriver.register("Statement.execute", System.currentTimeMillis() - start);
		}
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getResultSet();
		} finally {
			ProfilingDriver.register("Statement.getResultSet", System.currentTimeMillis() - start);
		}
	}

	@Override
	public int getUpdateCount() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getUpdateCount();
		} finally {
			ProfilingDriver.register("Statement.getUpdateCount", System.currentTimeMillis() - start);
		}

	}

	@Override
	public boolean getMoreResults() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getMoreResults();
		} finally {
			ProfilingDriver.register("Statement.getMoreResults", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void setFetchDirection(int i) throws SQLException {
		delegate.setFetchDirection(i);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return delegate.getFetchDirection();
	}

	@Override
	public void setFetchSize(int i) throws SQLException {
		delegate.setFetchSize(i);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return delegate.getFetchSize();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return delegate.getResultSetConcurrency();
	}

	@Override
	public int getResultSetType() throws SQLException {
		return delegate.getResultSetType();
	}

	@Override
	public void addBatch(String s) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			delegate.addBatch(s);
		} finally {
			ProfilingDriver.register("Statement.addBatch", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void clearBatch() throws SQLException {
		delegate.clearBatch();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.executeBatch();
		} finally {
			ProfilingDriver.register("Statement.executeBatch", System.currentTimeMillis() - start);
		}

	}

	@Override
	public Connection getConnection() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getConnection();
		} finally {
			ProfilingDriver.register("Statement.getConnection", System.currentTimeMillis() - start);
		}

	}

	@Override
	public boolean getMoreResults(int i) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getMoreResults(i);
		} finally {
			ProfilingDriver.register("Statement.getMoreResults", System.currentTimeMillis() - start);
		}

	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getGeneratedKeys();
		} finally {
			ProfilingDriver.register("Statement.getGeneratedKeys", System.currentTimeMillis() - start);
		}

	}

	@Override
	public int executeUpdate(String s, int i) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.executeUpdate(s, i);
		} finally {
			ProfilingDriver.register("Statement.executeUpdate", System.currentTimeMillis() - start);
		}

	}

	@Override
	public int executeUpdate(String s, int[] ints) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.executeUpdate(s, ints);
		} finally {
			ProfilingDriver.register("Statement.executeUpdate", System.currentTimeMillis() - start);
		}

	}

	@Override
	public int executeUpdate(String s, String[] strings) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.executeUpdate(s, strings);
		} finally {
			ProfilingDriver.register("Statement.executeUpdate", System.currentTimeMillis() - start);
		}

	}

	@Override
	public boolean execute(String s, int i) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.execute(s, i);
		} finally {
			ProfilingDriver.register("Statement.execute", System.currentTimeMillis() - start);
		}

	}

	@Override
	public boolean execute(String s, int[] ints) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.execute(s, ints);
		} finally {
			ProfilingDriver.register("Statement.execute", System.currentTimeMillis() - start);
		}

	}

	@Override
	public boolean execute(String s, String[] strings) throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.execute(s, strings);
		} finally {
			ProfilingDriver.register("Statement.execute", System.currentTimeMillis() - start);
		}

	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return delegate.getResultSetHoldability();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return delegate.isClosed();
	}

	@Override
	public void setPoolable(boolean b) throws SQLException {
		delegate.setPoolable(b);
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return delegate.isPoolable();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		delegate.closeOnCompletion();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return delegate.isCloseOnCompletion();
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
