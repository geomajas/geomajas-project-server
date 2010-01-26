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

import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

/**
 * ???
 *
 * @author check subversion
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
