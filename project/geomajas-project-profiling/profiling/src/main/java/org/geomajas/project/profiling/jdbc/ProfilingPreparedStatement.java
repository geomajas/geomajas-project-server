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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Wrapping JDBC prepared statement which can be used to profile the time spent communicating with the database.
 *
 * @author Joachim Van der Auwera
 */
public class ProfilingPreparedStatement extends ProfilingStatement implements PreparedStatement {

	private PreparedStatement delegate;

	/**
	 * Delegate prepared statement which handles the actual work.
	 *
	 * @param delegate delegate
	 */
	public ProfilingPreparedStatement(PreparedStatement delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.executeQuery();
		} finally {
			ProfilingDriver.register("PreparedStatement.executeQuery", System.currentTimeMillis() - start);
		}

	}

	@Override
	public int executeUpdate() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.executeUpdate();
		} finally {
			ProfilingDriver.register("PreparedStatement.executeUpdate", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void setNull(int i, int i2) throws SQLException {
		delegate.setNull(i, i2);
	}

	@Override
	public void setBoolean(int i, boolean b) throws SQLException {
		delegate.setBoolean(i, b);
	}

	@Override
	public void setByte(int i, byte b) throws SQLException {
		delegate.setByte(i, b);
	}

	@Override
	public void setShort(int i, short i2) throws SQLException {
		delegate.setShort(i, i2);
	}

	@Override
	public void setInt(int i, int i2) throws SQLException {
		delegate.setInt(i, i2);
	}

	@Override
	public void setLong(int i, long l) throws SQLException {
		delegate.setLong(i, l);
	}

	@Override
	public void setFloat(int i, float v) throws SQLException {
		delegate.setFloat(i, v);
	}

	@Override
	public void setDouble(int i, double v) throws SQLException {
		delegate.setDouble(i, v);
	}

	@Override
	public void setBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
		delegate.setBigDecimal(i, bigDecimal);
	}

	@Override
	public void setString(int i, String s) throws SQLException {
		delegate.setString(i, s);
	}

	@Override
	public void setBytes(int i, byte[] bytes) throws SQLException {
		delegate.setBytes(i, bytes);
	}

	@Override
	public void setDate(int i, Date date) throws SQLException {
		delegate.setDate(i, date);
	}

	@Override
	public void setTime(int i, Time time) throws SQLException {
		delegate.setTime(i, time);
	}

	@Override
	public void setTimestamp(int i, Timestamp timestamp) throws SQLException {
		delegate.setTimestamp(i, timestamp);
	}

	@Override
	public void setAsciiStream(int i, InputStream inputStream, int i2) throws SQLException {
		delegate.setAsciiStream(i, inputStream, i2);
	}

	@Override
	public void setUnicodeStream(int i, InputStream inputStream, int i2) throws SQLException {
		delegate.setUnicodeStream(i, inputStream, i2);
	}

	@Override
	public void setBinaryStream(int i, InputStream inputStream, int i2) throws SQLException {
		delegate.setBinaryStream(i, inputStream, i2);
	}

	@Override
	public void clearParameters() throws SQLException {
		delegate.clearParameters();
	}

	@Override
	public void setObject(int i, Object o, int i2) throws SQLException {
		delegate.setObject(i, o, i2);
	}

	@Override
	public void setObject(int i, Object o) throws SQLException {
		delegate.setObject(i, o);
	}

	@Override
	public boolean execute() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.execute();
		} finally {
			ProfilingDriver.register("PreparedStatement.execute", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void addBatch() throws SQLException {
		delegate.addBatch();
	}

	@Override
	public void setCharacterStream(int i, Reader reader, int i2) throws SQLException {
		delegate.setCharacterStream(i, reader, i2);
	}

	@Override
	public void setRef(int i, Ref ref) throws SQLException {
		delegate.setRef(i, ref);
	}

	@Override
	public void setBlob(int i, Blob blob) throws SQLException {
		delegate.setBlob(i, blob);
	}

	@Override
	public void setClob(int i, Clob clob) throws SQLException {
		delegate.setClob(i, clob);
	}

	@Override
	public void setArray(int i, Array array) throws SQLException {
		delegate.setArray(i, array);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getMetaData();
		} finally {
			ProfilingDriver.register("PreparedStatement.getMetaData", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void setDate(int i, Date date, Calendar calendar) throws SQLException {
		delegate.setDate(i, date, calendar);
	}

	@Override
	public void setTime(int i, Time time, Calendar calendar) throws SQLException {
		delegate.setTime(i, time, calendar);
	}

	@Override
	public void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException {
		delegate.setTimestamp(i, timestamp, calendar);
	}

	@Override
	public void setNull(int i, int i2, String s) throws SQLException {
		delegate.setNull(i, i2, s);
	}

	@Override
	public void setURL(int i, URL url) throws SQLException {
		delegate.setURL(i, url);
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		long start = System.currentTimeMillis();
		try {
			return delegate.getParameterMetaData();
		} finally {
			ProfilingDriver.register("PreparedStatement.getParameterMetaData", System.currentTimeMillis() - start);
		}

	}

	@Override
	public void setRowId(int i, RowId rowId) throws SQLException {
		delegate.setRowId(i, rowId);
	}

	@Override
	public void setNString(int i, String s) throws SQLException {
		delegate.setNString(i, s);
	}

	@Override
	public void setNCharacterStream(int i, Reader reader, long l) throws SQLException {
		delegate.setNCharacterStream(i, reader, l);
	}

	@Override
	public void setNClob(int i, NClob nClob) throws SQLException {
		delegate.setNClob(i, nClob);
	}

	@Override
	public void setClob(int i, Reader reader, long l) throws SQLException {
		delegate.setClob(i, reader, l);
	}

	@Override
	public void setBlob(int i, InputStream inputStream, long l) throws SQLException {
		delegate.setBlob(i, inputStream, l);
	}

	@Override
	public void setNClob(int i, Reader reader, long l) throws SQLException {
		delegate.setNClob(i, reader, l);
	}

	@Override
	public void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
		delegate.setSQLXML(i, sqlxml);
	}

	@Override
	public void setObject(int i, Object o, int i2, int i3) throws SQLException {
		delegate.setObject(i, o, i2, i3);
	}

	@Override
	public void setAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
		delegate.setAsciiStream(i, inputStream, l);
	}

	@Override
	public void setBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
		delegate.setBinaryStream(i, inputStream, l);
	}

	@Override
	public void setCharacterStream(int i, Reader reader, long l) throws SQLException {
		delegate.setCharacterStream(i, reader, l);
	}

	@Override
	public void setAsciiStream(int i, InputStream inputStream) throws SQLException {
		delegate.setAsciiStream(i, inputStream);
	}

	@Override
	public void setBinaryStream(int i, InputStream inputStream) throws SQLException {
		delegate.setBinaryStream(i, inputStream);
	}

	@Override
	public void setCharacterStream(int i, Reader reader) throws SQLException {
		delegate.setCharacterStream(i, reader);
	}

	@Override
	public void setNCharacterStream(int i, Reader reader) throws SQLException {
		delegate.setNCharacterStream(i, reader);
	}

	@Override
	public void setClob(int i, Reader reader) throws SQLException {
		delegate.setClob(i, reader);
	}

	@Override
	public void setBlob(int i, InputStream inputStream) throws SQLException {
		delegate.setBlob(i, inputStream);
	}

	@Override
	public void setNClob(int i, Reader reader) throws SQLException {
		delegate.setNClob(i, reader);
	}
}
