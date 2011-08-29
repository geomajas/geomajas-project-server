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

import java.awt.Font;
import java.util.StringTokenizer;

/**
 * Serializes java.awt.Font to JSON and back.
 *
 * @author Jan De Moerloose
 */
public class FontSerializer extends AbstractSerializer {

	private final Logger log = LoggerFactory.getLogger(GeometrySerializer.class);

	private static final long serialVersionUID = -1265127912351766084L;

	private static final Class[] SERIALIZABLE_CLASSES = new Class[] {Font.class};

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
		if (o instanceof Font) {
			return marshal((Font) o);
		}
		return null;
	}

	private Font unmarshal(String s) {
		StringTokenizer st = new StringTokenizer(s, ",");
		if (st.countTokens() < 2) {
			throw new IllegalArgumentException("Not enough tokens (<3) in font " + s);
		}
		int count = st.countTokens();

		String name = st.nextToken();
		int styleIndex = Font.PLAIN;
		if (count > 2) {
			String style = st.nextToken();
			if (style.equalsIgnoreCase("bold")) {
				styleIndex = Font.BOLD;
			} else if (style.equalsIgnoreCase("italic")) {
				styleIndex = Font.ITALIC;
			}
		}
		int size = Integer.parseInt(st.nextToken());
		return new Font(name, styleIndex, size);
	}

	private String marshal(Font font) {
		String style = null;
		if (font.getStyle() == Font.BOLD) {
			style = "bold";
		} else if (font.getStyle() == Font.ITALIC) {
			style = "italic";
		}
		return font.getName() + (style == null ? "" : "," + style) + "," + font.getSize();
	}

}
