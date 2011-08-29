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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.StringTokenizer;

/**
 * Serializes java.awt.Color to JSON and back.
 *
 * @author Jan De Moerloose
 */
public class ColorSerializer extends AbstractSerializer {

	private final Logger log = LoggerFactory.getLogger(GeometrySerializer.class);

	private static final long serialVersionUID = -1265127912351766084L;

	private static final Class[] SERIALIZABLE_CLASSES = new Class[] {Color.class};

	private static final Class[] JSON_CLASSES = new Class[] {String.class};

	public Class[] getSerializableClasses() {
		return SERIALIZABLE_CLASSES;
	}

	public Class[] getJSONClasses() {
		return JSON_CLASSES;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object jso)
			throws UnmarshallException {
		try {
			unmarshal((String) jso);
		} catch (Exception e) {
			throw new UnmarshallException("cannot convert object " + jso + " to type " + clazz.getName());
		}
		return ObjectMatch.OKAY;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object jso) throws UnmarshallException {
		try {
			return unmarshal((String) jso);
		} catch (Exception e) {
			log.error("cannot convert object " + jso + " to type " + clazz.getName(), e);
			throw new UnmarshallException("cannot convert object " + jso + " to type " + clazz.getName());
		}
	}

	public Object marshall(SerializerState state, Object o) throws MarshallException {
		if (o instanceof Color) {
			return marshal((Color) o);
		}
		return null;
	}

	private Color unmarshal(String s) {
		StringTokenizer st = new StringTokenizer(s, "rgba,=");
		int r = 255;
		int g = 255;
		int b = 255;
		int a = 255;
		if (st.hasMoreTokens()) {
			r = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			g = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			b = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			a = Integer.parseInt(st.nextToken());
		}
		return new Color(r, g, b, a);
	}

	private String marshal(Color c) {
		return "r=" + c.getRed() + ",g=" + c.getGreen() + ",b=" + c.getBlue() + ",a=" + c.getAlpha();
	}

}
