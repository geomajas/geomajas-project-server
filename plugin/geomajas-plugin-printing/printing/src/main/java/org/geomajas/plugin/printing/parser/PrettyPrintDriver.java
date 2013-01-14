/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
