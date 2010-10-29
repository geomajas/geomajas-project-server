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
package org.geomajas.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Assure resources are GZIP compressed.
 * 
 * @author Jan De Moerloose
 */
public class GzipResponseStream extends ServletOutputStream {

	private ByteArrayOutputStream byteStream;

	private GZIPOutputStream gzipStream;

	private boolean closed;

	private HttpServletResponse response;

	private ServletOutputStream servletStream;

	public GzipResponseStream(HttpServletResponse response) throws IOException {
		super();
		closed = false;
		this.response = response;
		this.servletStream = response.getOutputStream();
		byteStream = new ByteArrayOutputStream();
		gzipStream = new GZIPOutputStream(byteStream);
	}

	public void close() throws IOException {
		if (closed) {
			throw new IOException("This output stream has already been closed");
		}
		gzipStream.finish();

		byte[] bytes = byteStream.toByteArray();

		response.setContentLength(bytes.length);
		response.addHeader("Content-Encoding", "gzip");
		servletStream.write(bytes);
		servletStream.flush();
		servletStream.close();
		closed = true;
	}

	public void flush() throws IOException {
		if (closed) {
			throw new IOException("Cannot flush a closed output stream");
		}
		gzipStream.flush();
	}

	public void write(int b) throws IOException {
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		gzipStream.write((byte) b);
	}

	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		gzipStream.write(b, off, len);
	}
}