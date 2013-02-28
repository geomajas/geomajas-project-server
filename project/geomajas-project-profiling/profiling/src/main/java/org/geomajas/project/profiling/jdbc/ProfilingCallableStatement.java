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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Wrapping JDBC callable statement which can be used to profile the time spent communicating with the database.
 *
 * @author Joachim Van der Auwera
 */
public class ProfilingCallableStatement extends ProfilingPreparedStatement implements CallableStatement {

	private CallableStatement delegate;

	/**
	 * Delegate callable statement which handles the actual work.
	 *
	 * @param delegate delegate
	 */
	public ProfilingCallableStatement(CallableStatement delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	@Override
	public void registerOutParameter(int i, int i2) throws SQLException {
		delegate.registerOutParameter(i, i2);
	}

	@Override
	public void registerOutParameter(int i, int i2, int i3) throws SQLException {
		delegate.registerOutParameter(i, i2, i3);
	}

	@Override
	public boolean wasNull() throws SQLException {
		return delegate.wasNull();
	}

	@Override
	public String getString(int i) throws SQLException {
		return delegate.getString(i);
	}

	@Override
	public boolean getBoolean(int i) throws SQLException {
		return delegate.getBoolean(i);
	}

	@Override
	public byte getByte(int i) throws SQLException {
		return delegate.getByte(i);
	}

	@Override
	public short getShort(int i) throws SQLException {
		return delegate.getShort(i);
	}

	@Override
	public int getInt(int i) throws SQLException {
		return delegate.getInt(i);
	}

	@Override
	public long getLong(int i) throws SQLException {
		return delegate.getLong(i);
	}

	@Override
	public float getFloat(int i) throws SQLException {
		return delegate.getFloat(i);
	}

	@Override
	public double getDouble(int i) throws SQLException {
		return delegate.getDouble(i);
	}

	@Override
	public BigDecimal getBigDecimal(int i, int i2) throws SQLException {
		return delegate.getBigDecimal(i, i2);
	}

	@Override
	public byte[] getBytes(int i) throws SQLException {
		return delegate.getBytes(i);
	}

	@Override
	public Date getDate(int i) throws SQLException {
		return delegate.getDate(i);
	}

	@Override
	public Time getTime(int i) throws SQLException {
		return delegate.getTime(i);
	}

	@Override
	public Timestamp getTimestamp(int i) throws SQLException {
		return delegate.getTimestamp(i);
	}

	@Override
	public Object getObject(int i) throws SQLException {
		return delegate.getObject(i);
	}

	@Override
	public BigDecimal getBigDecimal(int i) throws SQLException {
		return delegate.getBigDecimal(i);
	}

	@Override
	public Object getObject(int i, Map<String, Class<?>> stringClassMap) throws SQLException {
		return delegate.getObject(i, stringClassMap);
	}

	@Override
	public Ref getRef(int i) throws SQLException {
		return delegate.getRef(i);
	}

	@Override
	public Blob getBlob(int i) throws SQLException {
		return delegate.getBlob(i);
	}

	@Override
	public Clob getClob(int i) throws SQLException {
		return delegate.getClob(i);
	}

	@Override
	public Array getArray(int i) throws SQLException {
		return delegate.getArray(i);
	}

	@Override
	public Date getDate(int i, Calendar calendar) throws SQLException {
		return delegate.getDate(i, calendar);
	}

	@Override
	public Time getTime(int i, Calendar calendar) throws SQLException {
		return delegate.getTime(i, calendar);
	}

	@Override
	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		return delegate.getTimestamp(i, calendar);
	}

	@Override
	public void registerOutParameter(int i, int i2, String s) throws SQLException {
		delegate.registerOutParameter(i, i2, s);
	}

	@Override
	public void registerOutParameter(String s, int i) throws SQLException {
		delegate.registerOutParameter(s, i);
	}

	@Override
	public void registerOutParameter(String s, int i, int i2) throws SQLException {
		delegate.registerOutParameter(s, i, i2);
	}

	@Override
	public void registerOutParameter(String s, int i, String s2) throws SQLException {
		delegate.registerOutParameter(s, i, s2);
	}

	@Override
	public URL getURL(int i) throws SQLException {
		return delegate.getURL(i);
	}

	@Override
	public void setURL(String s, URL url) throws SQLException {
		delegate.setURL(s, url);
	}

	@Override
	public void setNull(String s, int i) throws SQLException {
		delegate.setNull(s, i);
	}

	@Override
	public void setBoolean(String s, boolean b) throws SQLException {
		delegate.setBoolean(s, b);
	}

	@Override
	public void setByte(String s, byte b) throws SQLException {
		delegate.setByte(s, b);
	}

	@Override
	public void setShort(String s, short i) throws SQLException {
		delegate.setShort(s, i);
	}

	@Override
	public void setInt(String s, int i) throws SQLException {
		delegate.setInt(s, i);
	}

	@Override
	public void setLong(String s, long l) throws SQLException {
		delegate.setLong(s, l);
	}

	@Override
	public void setFloat(String s, float v) throws SQLException {
		delegate.setFloat(s, v);
	}

	@Override
	public void setDouble(String s, double v) throws SQLException {
		delegate.setDouble(s, v);
	}

	@Override
	public void setBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
		delegate.setBigDecimal(s, bigDecimal);
	}

	@Override
	public void setString(String s, String s2) throws SQLException {
		delegate.setString(s, s2);
	}

	@Override
	public void setBytes(String s, byte[] bytes) throws SQLException {
		delegate.setBytes(s, bytes);
	}

	@Override
	public void setDate(String s, Date date) throws SQLException {
		delegate.setDate(s, date);
	}

	@Override
	public void setTime(String s, Time time) throws SQLException {
		delegate.setTime(s, time);
	}

	@Override
	public void setTimestamp(String s, Timestamp timestamp) throws SQLException {
		delegate.setTimestamp(s, timestamp);
	}

	@Override
	public void setAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
		delegate.setAsciiStream(s, inputStream, i);
	}

	@Override
	public void setBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
		delegate.setBinaryStream(s, inputStream, i);
	}

	@Override
	public void setObject(String s, Object o, int i, int i2) throws SQLException {
		delegate.setObject(s, o, i, i2);
	}

	@Override
	public void setObject(String s, Object o, int i) throws SQLException {
		delegate.setObject(s, o, i);
	}

	@Override
	public void setObject(String s, Object o) throws SQLException {
		delegate.setObject(s, o);
	}

	@Override
	public void setCharacterStream(String s, Reader reader, int i) throws SQLException {
		delegate.setCharacterStream(s, reader, i);
	}

	@Override
	public void setDate(String s, Date date, Calendar calendar) throws SQLException {
		delegate.setDate(s, date, calendar);
	}

	@Override
	public void setTime(String s, Time time, Calendar calendar) throws SQLException {
		delegate.setTime(s, time, calendar);
	}

	@Override
	public void setTimestamp(String s, Timestamp timestamp, Calendar calendar) throws SQLException {
		delegate.setTimestamp(s, timestamp, calendar);
	}

	@Override
	public void setNull(String s, int i, String s2) throws SQLException {
		delegate.setNull(s, i, s2);
	}

	@Override
	public String getString(String s) throws SQLException {
		return delegate.getString(s);
	}

	@Override
	public boolean getBoolean(String s) throws SQLException {
		return delegate.getBoolean(s);
	}

	@Override
	public byte getByte(String s) throws SQLException {
		return delegate.getByte(s);
	}

	@Override
	public short getShort(String s) throws SQLException {
		return delegate.getShort(s);
	}

	@Override
	public int getInt(String s) throws SQLException {
		return delegate.getInt(s);
	}

	@Override
	public long getLong(String s) throws SQLException {
		return delegate.getLong(s);
	}

	@Override
	public float getFloat(String s) throws SQLException {
		return delegate.getFloat(s);
	}

	@Override
	public double getDouble(String s) throws SQLException {
		return delegate.getDouble(s);
	}

	@Override
	public byte[] getBytes(String s) throws SQLException {
		return delegate.getBytes(s);
	}

	@Override
	public Date getDate(String s) throws SQLException {
		return delegate.getDate(s);
	}

	@Override
	public Time getTime(String s) throws SQLException {
		return delegate.getTime(s);
	}

	@Override
	public Timestamp getTimestamp(String s) throws SQLException {
		return delegate.getTimestamp(s);
	}

	@Override
	public Object getObject(String s) throws SQLException {
		return delegate.getObject(s);
	}

	@Override
	public BigDecimal getBigDecimal(String s) throws SQLException {
		return delegate.getBigDecimal(s);
	}

	@Override
	public Object getObject(String s, Map<String, Class<?>> stringClassMap) throws SQLException {
		return delegate.getObject(s, stringClassMap);
	}

	@Override
	public Ref getRef(String s) throws SQLException {
		return delegate.getRef(s);
	}

	@Override
	public Blob getBlob(String s) throws SQLException {
		return delegate.getBlob(s);
	}

	@Override
	public Clob getClob(String s) throws SQLException {
		return delegate.getClob(s);
	}

	@Override
	public Array getArray(String s) throws SQLException {
		return delegate.getArray(s);
	}

	@Override
	public Date getDate(String s, Calendar calendar) throws SQLException {
		return delegate.getDate(s, calendar);
	}

	@Override
	public Time getTime(String s, Calendar calendar) throws SQLException {
		return delegate.getTime(s, calendar);
	}

	@Override
	public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
		return delegate.getTimestamp(s, calendar);
	}

	@Override
	public URL getURL(String s) throws SQLException {
		return delegate.getURL(s);
	}

	@Override
	public RowId getRowId(int i) throws SQLException {
		return delegate.getRowId(i);
	}

	@Override
	public RowId getRowId(String s) throws SQLException {
		return delegate.getRowId(s);
	}

	@Override
	public void setRowId(String s, RowId rowId) throws SQLException {
		delegate.setRowId(s, rowId);
	}

	@Override
	public void setNString(String s, String s2) throws SQLException {
		delegate.setNString(s, s2);
	}

	@Override
	public void setNCharacterStream(String s, Reader reader, long l) throws SQLException {
		delegate.setNCharacterStream(s, reader, l);
	}

	@Override
	public void setNClob(String s, NClob nClob) throws SQLException {
		delegate.setNClob(s, nClob);
	}

	@Override
	public void setClob(String s, Reader reader, long l) throws SQLException {
		delegate.setClob(s, reader, l);
	}

	@Override
	public void setBlob(String s, InputStream inputStream, long l) throws SQLException {
		delegate.setBlob(s, inputStream, l);
	}

	@Override
	public void setNClob(String s, Reader reader, long l) throws SQLException {
		delegate.setNClob(s, reader, l);
	}

	@Override
	public NClob getNClob(int i) throws SQLException {
		return delegate.getNClob(i);
	}

	@Override
	public NClob getNClob(String s) throws SQLException {
		return delegate.getNClob(s);
	}

	@Override
	public void setSQLXML(String s, SQLXML sqlxml) throws SQLException {
		delegate.setSQLXML(s, sqlxml);
	}

	@Override
	public SQLXML getSQLXML(int i) throws SQLException {
		return delegate.getSQLXML(i);
	}

	@Override
	public SQLXML getSQLXML(String s) throws SQLException {
		return delegate.getSQLXML(s);
	}

	@Override
	public String getNString(int i) throws SQLException {
		return delegate.getNString(i);
	}

	@Override
	public String getNString(String s) throws SQLException {
		return delegate.getNString(s);
	}

	@Override
	public Reader getNCharacterStream(int i) throws SQLException {
		return delegate.getNCharacterStream(i);
	}

	@Override
	public Reader getNCharacterStream(String s) throws SQLException {
		return delegate.getNCharacterStream(s);
	}

	@Override
	public Reader getCharacterStream(int i) throws SQLException {
		return delegate.getCharacterStream(i);
	}

	@Override
	public Reader getCharacterStream(String s) throws SQLException {
		return delegate.getCharacterStream(s);
	}

	@Override
	public void setBlob(String s, Blob blob) throws SQLException {
		delegate.setBlob(s, blob);
	}

	@Override
	public void setClob(String s, Clob clob) throws SQLException {
		delegate.setClob(s, clob);
	}

	@Override
	public void setAsciiStream(String s, InputStream inputStream, long l) throws SQLException {
		delegate.setAsciiStream(s, inputStream, l);
	}

	@Override
	public void setBinaryStream(String s, InputStream inputStream, long l) throws SQLException {
		delegate.setBinaryStream(s, inputStream, l);
	}

	@Override
	public void setCharacterStream(String s, Reader reader, long l) throws SQLException {
		delegate.setCharacterStream(s, reader, l);
	}

	@Override
	public void setAsciiStream(String s, InputStream inputStream) throws SQLException {
		delegate.setAsciiStream(s, inputStream);
	}

	@Override
	public void setBinaryStream(String s, InputStream inputStream) throws SQLException {
		delegate.setBinaryStream(s, inputStream);
	}

	@Override
	public void setCharacterStream(String s, Reader reader) throws SQLException {
		delegate.setCharacterStream(s, reader);
	}

	@Override
	public void setNCharacterStream(String s, Reader reader) throws SQLException {
		delegate.setNCharacterStream(s, reader);
	}

	@Override
	public void setClob(String s, Reader reader) throws SQLException {
		delegate.setClob(s, reader);
	}

	@Override
	public void setBlob(String s, InputStream inputStream) throws SQLException {
		delegate.setBlob(s, inputStream);
	}

	@Override
	public void setNClob(String s, Reader reader) throws SQLException {
		delegate.setNClob(s, reader);
	}

	@Override
	public <T> T getObject(int i, Class<T> tClass) throws SQLException {
		return delegate.getObject(i, tClass);
	}

	@Override
	public <T> T getObject(String s, Class<T> tClass) throws SQLException {
		return delegate.getObject(s, tClass);
	}
}
