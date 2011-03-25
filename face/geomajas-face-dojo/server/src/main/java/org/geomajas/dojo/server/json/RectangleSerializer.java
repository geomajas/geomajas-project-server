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

import com.lowagie.text.Rectangle;
import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;
import org.json.JSONObject;

/**
 * Rectangle lacks a default constructor, this class takes care of this and
 * delegates to the bean serializer.
 *
 * @author Jan De Moerloose
 */
public class RectangleSerializer extends AbstractSerializer {

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
		jso.getDouble("x");
		jso.getDouble("y");
		jso.getDouble("width");
		jso.getDouble("height");
		return ObjectMatch.OKAY;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		jso.getString("javaClass");
		float x = (float) jso.getDouble("x");
		float y = (float) jso.getDouble("y");
		float width = (float) jso.getDouble("width");
		float height = (float) jso.getDouble("height");
		return new Rectangle(x, y, x + width, y + height);
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
