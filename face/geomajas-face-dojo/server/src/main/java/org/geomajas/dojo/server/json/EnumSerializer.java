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

/**
 * Json serializer for enum objects.
 *
 * @author Jan De Moerloose
 */
public class EnumSerializer extends AbstractSerializer {

	private static final long serialVersionUID = 1;

	private static Class[] SERIALIZABLE_CLASSES = new Class[] {Enum.class};

	private static Class[] JSON_CLASSES = new Class[] {String.class};

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

	public Object toEnum(Class clazz, Object jso)
			throws IllegalArgumentException {
		return Enum.valueOf(clazz, (String) jso);
	}

	public Object unmarshall(SerializerState state, Class clazz, Object jso)
			throws UnmarshallException {
		try {
			if (jso == null || "".equals(jso)) {
				return null;
			}
			return toEnum(clazz, jso);
		} catch (IllegalArgumentException nfe) {
			throw new UnmarshallException("cannot convert object " + jso
					+ " to type " + clazz.getName());
		}
	}

	public Object marshall(SerializerState state, Object o)
			throws MarshallException {
		return ((Enum) o).toString();
	}

}
