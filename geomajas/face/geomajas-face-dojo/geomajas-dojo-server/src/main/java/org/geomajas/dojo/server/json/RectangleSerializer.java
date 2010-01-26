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

import com.lowagie.text.Rectangle;
import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rectangle lacks a default constructor, this class takes care of this and
 * delegates to the bean serializer.
 *
 * @author Jan De Moerloose
 */
public class RectangleSerializer extends AbstractSerializer {

	private final Logger log = LoggerFactory.getLogger(RectangleSerializer.class);

	private static final long serialVersionUID = -1265127912351766084L;

	private static Class[] SERIALIZABLE_CLASSES = new Class[] {Rectangle.class};

	private static Class[] JSON_CLASSES = new Class[] {JSONObject.class};

	public Class[] getSerializableClasses() {
		return SERIALIZABLE_CLASSES;
	}

	public Class[] getJSONClasses() {
		return JSON_CLASSES;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		double x = jso.getDouble("x");
		double y = jso.getDouble("y");
		double width = jso.getDouble("width");
		double height = jso.getDouble("height");
		return ObjectMatch.OKAY;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		String javaClass = jso.getString("javaClass");
		float x = (float) jso.getDouble("x");
		float y = (float) jso.getDouble("y");
		float width = (float) jso.getDouble("width");
		float height = (float) jso.getDouble("height");
		Rectangle rect = new Rectangle(x, y, x + width, y + height);
		return rect;
	}

	public Object marshall(SerializerState state, Object o) throws MarshallException {
		Rectangle rect = (Rectangle) o;

		JSONObject obj = new JSONObject();
		if (ser.getMarshallClassHints()) {
			obj.put("javaClass", o.getClass().getName());
		}
		obj.put("x", rect.getLeft());
		obj.put("y", rect.getBottom());
		obj.put("width", rect.getWidth());
		obj.put("height", rect.getHeight());
		return obj;
	}

}
