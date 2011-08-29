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

import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.layer.LayerType;

import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

/**
 * Json serializer for enum objects.
 * 
 * @author Jan De Moerloose
 */
public class EnumSerializer extends AbstractSerializer {

	private static final long serialVersionUID = 1;

	private static final Class[] SERIALIZABLE_CLASSES = new Class[] { Enum.class };

	private static final Class[] JSON_CLASSES = new Class[] { String.class };

	public Class[] getSerializableClasses() {
		return SERIALIZABLE_CLASSES;
	}

	public Class[] getJSONClasses() {
		return JSON_CLASSES;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object jso) throws UnmarshallException {
		try {
			toEnum(clazz, jso);
		} catch (IllegalArgumentException e) {
			throw new UnmarshallException("Not a valid enum string");
		}
		return ObjectMatch.OKAY;
	}

	@Override
	public boolean canSerialize(Class clazz, Class jsonClazz) {
		boolean canJava = false, canJSON = false;

		if (clazz.isEnum()) {
			canJava = true;
		}

		if (jsonClazz == null) {
			canJSON = true;
		} else {
			Class[] jsonClasses = getJSONClasses();
			for (Class jsonClass : jsonClasses) {
				if (jsonClazz == jsonClass) {
					canJSON = true;
				}
			}
		}

		return (canJava && canJSON);
	}

	public Object toEnum(Class clazz, Object jso) throws IllegalArgumentException {
		// System.out.println("EnumSerializer: in: " + jso.toString() + " - " + clazz.getName());
		if (PrimitiveType.class.equals(clazz)) {
			return PrimitiveType.valueOf((String) jso);
		} else if (AssociationType.class.equals(clazz)) {
			return AssociationType.valueOf((String) jso);
		} else {
			return Enum.valueOf(clazz, (String) jso);
		}
	}

	public Object unmarshall(SerializerState state, Class clazz, Object jso) throws UnmarshallException {
		try {
			if (jso == null || "".equals(jso)) {
				return null;
			}
			return toEnum(clazz, jso);
		} catch (IllegalArgumentException nfe) {
			throw new UnmarshallException("cannot convert object " + jso + " to type " + clazz.getName());
		}
	}

	public Object marshall(SerializerState state, Object o) throws MarshallException {
		// System.out.println("EnumSerializer: out: " + ((Enum) o).name() + " - " + o.getClass().getName());
		if (o instanceof LayerType) {
			return ((Enum) o).name(); // toString() gives int for LayerType !!
		} else {
			return o.toString();
		}
	}

}
