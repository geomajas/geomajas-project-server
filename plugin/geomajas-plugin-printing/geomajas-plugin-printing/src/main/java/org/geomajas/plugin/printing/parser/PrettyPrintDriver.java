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
package org.geomajas.plugin.printing.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppReader;

/**
 * XStream stream driver that produces pretty printed XML.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrettyPrintDriver implements HierarchicalStreamDriver {

	public HierarchicalStreamReader createReader(Reader in) {
		return new XppReader(in);
	}

	public HierarchicalStreamReader createReader(InputStream in) {
		try {
			return createReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new StreamException(e);
		}
	}

	public HierarchicalStreamWriter createWriter(Writer out) {
		return new PrettyPrintWriter(out);
	}

	public HierarchicalStreamWriter createWriter(OutputStream out) {
		try {
			return createWriter(new OutputStreamWriter(out, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new StreamException(e);
		}
	}

}
