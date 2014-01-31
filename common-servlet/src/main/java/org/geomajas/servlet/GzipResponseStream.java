/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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

	private final ByteArrayOutputStream byteStream;

	private final GZIPOutputStream gzipStream;

	private boolean closed;

	private final HttpServletResponse response;

	private final ServletOutputStream servletStream;

	public GzipResponseStream(HttpServletResponse response) throws IOException {
		super();
		closed = false;
		this.response = response;
		this.servletStream = response.getOutputStream();
		byteStream = new ByteArrayOutputStream();
		gzipStream = new GZIPOutputStream(byteStream);
	}
	
	public boolean isClosed() {
		return closed;
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
