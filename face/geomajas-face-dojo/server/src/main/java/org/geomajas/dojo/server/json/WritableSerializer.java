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

import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

/**
 * Mechanism for assuring custom types can be JSON serialized.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class WritableSerializer {

	private static final long serialVersionUID = 1L;	

	private static Class[] SERIALIZABLE_CLASSES = new Class[] {Writable.class};

	private static Class[] JSON_CLASSES = new Class[] {Writable.class};

	public Class[] getSerializableClasses() {
		return SERIALIZABLE_CLASSES;
	}

	public Class[] getJSONClasses() {
		return JSON_CLASSES;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object jso)
			throws UnmarshallException {
		return ObjectMatch.OKAY;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object jso) throws UnmarshallException {
		throw new UnmarshallException("Cannot unmarshall Writable");
	}

	public Object marshall(SerializerState state, Object o) throws MarshallException {
		return o;
	}

}
