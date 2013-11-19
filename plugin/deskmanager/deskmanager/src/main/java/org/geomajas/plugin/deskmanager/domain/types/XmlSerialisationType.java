/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.domain.types;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.SerializationUtils;
import org.geomajas.geometry.Bbox;
import org.hibernate.Hibernate;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type for storing serializable objects in the database. Used for ClientWidgetInfo configurations.
 * 
 * @author Oliver May
 * 
 */
public class XmlSerialisationType implements UserType {

	private static final Logger LOG = LoggerFactory.getLogger(XmlSerialisationType.class);

	static {
		try {
			BeanInfo bi = Introspector.getBeanInfo(Bbox.class);
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (("maxX").equals(pd.getName()) | ("maxY").equals(pd.getName())) {
					pd.setValue("transient", Boolean.TRUE);
				}
			}
		} catch (IntrospectionException e) {
			LOG.warn(e.getLocalizedMessage());
		}
	}

	private static final int[] TYPES = { Types.CLOB };

	private static final String ENCODING = "UTF-8";

	public int[] sqlTypes() {
		return TYPES;
	}

	public Class<?> returnedClass() {
		return Serializable.class;
	}

	public boolean equals(Object x, Object y) {
		if (x == null && y == null) {
			return true;
		}
		if (x == null || y == null) {
			return false;
		}
		return x.equals(y);
	}

	public int hashCode(Object x) {
		if (x == null) {
			return 0;
		}
		return x.hashCode();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws SQLException {
		String xmlString = (String) Hibernate.STRING.nullSafeGet(rs, names);
		return fromXmlString(xmlString);

	}

	private Object fromXmlString(String xmlString) {
		if (xmlString == null) {
			return null;
		}
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xmlString.getBytes(ENCODING));
			XMLDecoder decoder = new XMLDecoder(is);
			return decoder.readObject();
		} catch (UnsupportedEncodingException e) {
			LOG.warn(e.getLocalizedMessage());
			IllegalArgumentException ex = new IllegalArgumentException("cannot disassemble the object");
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws SQLException {
		String xmlString = toXmlString(value);
		Hibernate.TEXT.nullSafeSet(st, xmlString, index);
	}

	private String toXmlString(Object value) {
		if (value == null) {
			return null;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLEncoder encoder = new XMLEncoder(baos);
			encoder.writeObject(value);
			encoder.close();
			String result = baos.toString(ENCODING);
			baos.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			IllegalArgumentException ex = new IllegalArgumentException("cannot disassemble the object", e);
			throw ex;
		}
	}

	public Object deepCopy(Object value) {
		if (value == null) {
			return value;
		} else if (value instanceof Serializable) {
			return SerializationUtils.clone((Serializable) value);
		} else {
			LOG.warn("Could not deepCopy object, not serializable. " + value);
			return null;
		}
	}

	public boolean isMutable() {
		return true;
	}

	public Serializable disassemble(Object value) {
		return (Serializable) deepCopy(value);
	}

	public Object assemble(Serializable cached, Object owner) {
		return deepCopy(cached);
	}

	public Object replace(Object original, Object target, Object owner) {
		return deepCopy(original);
	}

}
