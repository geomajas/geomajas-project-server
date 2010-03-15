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

	private static Class[] SERIALIZABLE_CLASSES = new Class[] {Font.class};

	private static Class[] JSON_CLASSES = new Class[] {String.class};

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
