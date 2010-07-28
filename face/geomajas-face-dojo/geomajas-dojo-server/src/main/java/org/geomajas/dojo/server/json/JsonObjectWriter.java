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

import com.metaparadigm.jsonrpc.JSONRPCResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.CharArrayWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

/**
 * Writes a JSON object directly to a Writer. Based on toString() code of JSON
 * package.
 *
 * @author Jan De Moerloose
 */
public class JsonObjectWriter
		extends FilterWriter {

	private int charCount;

	private int logLines;

	private StringBuilder logBuffer = new StringBuilder();

	public JsonObjectWriter(Writer arg0, int logLines) {
		super(arg0);
		this.logLines = logLines;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		if (charCount < logLines * 1000) {
			if (charCount + len < logLines * 1000) {
				logBuffer.append(cbuf);
				charCount += len;
			} else {
				logBuffer.append(cbuf, 0, logLines * 1000 - charCount);
				charCount = logLines * 1000;
			}
		}
		super.write(cbuf, off, len);
	}

	@Override
	public void write(int c) throws IOException {
		charCount += 1;
		if (charCount < logLines * 1000) {
			logBuffer.append((char) c);
		}
		super.write(c);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		if (charCount < logLines * 1000) {
			if (charCount + len < logLines * 1000) {
				logBuffer.append(str);
				charCount += len;
			} else {
				logBuffer.append(str, 0, logLines * 1000 - charCount);
				charCount = logLines * 1000;
			}
		}
		super.write(str, off, len);
	}

	public void write(JSONObject object) throws IOException {
		Iterator keys = object.keys();
		Object o = null;
		String s;

		write('{');
		while (keys.hasNext()) {
			if (o != null) {
				write(',');
			}
			s = (String) keys.next();
			o = object.get(s);
			if (o != null) {
				try {
					write(JSONObject.quote(s));
					write(':');
					writeObject(o);
				} catch (IOException ioe) {
					Logger log = LoggerFactory.getLogger(JsonObjectWriter.class);
					log.error("Problem writing " + s + " from " + object + " for value " + o + ", " + ioe.getMessage());
					throw ioe;
				} catch (ArithmeticException ae) {
					Logger log = LoggerFactory.getLogger(JsonObjectWriter.class);
					log.error("Problem writing " + s + ", " + ae.getMessage());
					throw ae;
				}
			}
		}
		write('}');
	}

	public void write(JSONRPCResult result) throws IOException {
		JSONObject o = new JSONObject();
		if (result.getErrorCode() == JSONRPCResult.CODE_SUCCESS) {
			o.put("id", result.getId());
			o.put("result", result.getResult());
		} else if (result.getErrorCode() == JSONRPCResult.CODE_REMOTE_EXCEPTION) {
			Throwable e = (Throwable) result.getResult();
			CharArrayWriter caw = new CharArrayWriter();
			e.printStackTrace(new PrintWriter(caw));
			JSONObject err = new JSONObject();
			err.put("code", new Integer(result.getErrorCode()));
			err.put("msg", e.getMessage());
			err.put("trace", caw.toString());
			o.put("id", result.getId());
			o.put("error", err);
		} else {
			JSONObject err = new JSONObject();
			err.put("code", new Integer(result.getErrorCode()));
			err.put("msg", result.getResult());
			o.put("id", result.getId());
			o.put("error", err);
		}
		write(o);
	}

	private void writeObject(Object o) throws IOException {
		if (o instanceof String) {
			write(JSONObject.quote((String) o));
		} else if (o instanceof Number) {
			write(JSONObject.numberToString((Number) o));
		} else if (o instanceof Writable) {
			((Writable) o).write(this);
		} else if (o instanceof JSONArray) {
			JSONArray array = (JSONArray) o;
			write("[");
			for (int i = 0; i < array.length(); i++) {
				if (i != 0) {
					write(",");
				}
				writeObject(array.get(i));
			}
			write("]");
		} else if (o instanceof JSONObject) {
			write((JSONObject) o);
		} else {
			write(o.toString());
		}
	}

}
