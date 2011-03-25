/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
 * Json serializer for BigDecimal and BigInteger objects.
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
				return new BigInteger(Long.toString(l));
			}
		} else if (clazz == BigDecimal.class) {
			if (jso instanceof String) {
				return new BigDecimal((String) jso);
			} else {
				double l = ((Number) jso).doubleValue();
				return new BigDecimal(Double.toString(l));
			}
		}
		return null;
	}
}