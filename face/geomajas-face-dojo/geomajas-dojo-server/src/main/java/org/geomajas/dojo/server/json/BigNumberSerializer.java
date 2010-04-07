/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.dojo.server.json;

import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Json serializer vor BigDecimal and BigInteger objects.
 * 
 * @author Pieter De Graef
 */
public class BigNumberSerializer extends AbstractSerializer {

	private final Logger log = LoggerFactory.getLogger(GeometrySerializer.class);

	private static final long serialVersionUID = -1265127912351766084L;

	private static Class[] SERIALIZABLE_CLASSES = new Class[] {BigDecimal.class, BigInteger.class};

	private static Class[] JSON_CLASSES = new Class[] {JSONObject.class};

	public Class[] getSerializableClasses() {
		return SERIALIZABLE_CLASSES;
	}

	public Class[] getJSONClasses() {
		return JSON_CLASSES;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object jso)
			throws UnmarshallException {
		try {
			toBigNumber(clazz, jso);
		} catch (NumberFormatException e) {
			throw new UnmarshallException("not a number");
		}
		return ObjectMatch.OKAY;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object jso) throws UnmarshallException {
		try {
			if (jso == null || "".equals(jso)) {
				return null;
			}
			return toBigNumber(clazz, jso);
		} catch (NumberFormatException nfe) {
			throw new UnmarshallException("cannot convert object " + jso + " to type " + clazz.getName());
		}
	}

	public Object marshall(SerializerState state, Object o) throws MarshallException {
		log.debug("\nMarshalling object in the BigNumberSerializer\n");
		if (o instanceof BigInteger) {
			return ((BigInteger) o).longValue();
		} else if (o instanceof BigDecimal) {
			return ((BigDecimal) o).doubleValue();
		}
		return null;
	}

	private Object toBigNumber(Class clazz, Object jso) throws NumberFormatException {
		if (clazz == BigInteger.class) {
			if (jso instanceof String) {
				return new BigInteger((String) jso);
			} else {
				long l = ((Number) jso).longValue();
				return new BigInteger(new Long(l).toString());
			}
		} else if (clazz == BigDecimal.class) {
			if (jso instanceof String) {
				return new BigDecimal((String) jso);
			} else {
				double l = ((Number) jso).doubleValue();
				return new BigDecimal(new Double(l).toString());
			}
		}
		return null;
	}
}