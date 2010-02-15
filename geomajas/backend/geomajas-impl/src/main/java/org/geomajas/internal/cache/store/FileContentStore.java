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

package org.geomajas.internal.cache.store;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.store.ContentStore;
import org.geomajas.cache.store.RenderContent;
import org.geomajas.global.ExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This implementation of the <code>ContentStore</code> uses the file system to
 * store it's contents. It should be initialized with a basePath that indicates
 * where to put all the entries.
 * </p>
 *
 * @author Pieter De Graef
 */
public class FileContentStore implements ContentStore {

	private final Logger log = LoggerFactory.getLogger(FileContentStore.class);

	/**
	 * Since this store can be used be multiple threads at once, we lock entries
	 * while they are being touched. This constant indicates the retry time in
	 * milliseconds when one of the entries is actually locked.
	 */
	private static final int LOCK_RETRY_TIME = 50;

	/**
	 * Indicates how many bytes to read at once, while reading file from the
	 * file system.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * The base directory where all the entries are stored as separate files.
	 */
	private String basePath;

	private final Map<String, FileLock> locks;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * This store should be initialized with a base directory that indicates
	 * where to put all files. Throws an exception if this directory can not be
	 * used.
	 */
	public FileContentStore(File baseDirectory) throws CacheException {
		if (baseDirectory == null) {
			throw new CacheException(ExceptionCode.CACHE_NO_LOCATION);
		}
		this.basePath = baseDirectory.getAbsolutePath();

		if (!baseDirectory.exists() || !baseDirectory.isDirectory()
				|| !baseDirectory.canWrite()) {
			throw new CacheException(ExceptionCode.CACHE_NOT_WRITABLE, basePath);
		}
		locks = new HashMap<String, FileLock>();
	}

	// -------------------------------------------------------------------------
	// ContentStore implementation:
	// -------------------------------------------------------------------------

	/**
	 * Delete the file corresponding to the <code>RenderContent</code> object
	 * from the file system.
	 *
	 * @param renderContent
	 *            The <code>RenderContent</code> object to search in the store.
	 * @return If the file was not found, false is returned. If the file was
	 *         successfully deleted, true is returned.
	 * @exception org.geomajas.cache.CacheException
	 *                Throws a <code>CacheIOException</code> if the file was
	 *                found, but we were unable to delete it.
	 */
	public boolean delete(RenderContent renderContent) throws CacheException {
		File file = getFileHandle(renderContent);
		if (!file.exists()) {
			return false;
		}
		String path = file.getAbsolutePath();
		waitForLock(path);
		lock(path);
		if (!file.delete()) {
			unlock(path);
			throw new CacheException(ExceptionCode.CACHE_CANNOT_DELETE, path);
		}
		unlock(path);
		return true;
	}

	/**
	 * Read the file corresponding to the <code>RenderContent</code> object from
	 * the file system.
	 *
	 * @param renderContent
	 *            Entry we are looking for.
	 * @return If the file could not be found, NULL is returned. If the file was
	 *         successfully found and read, then it's byte array contents are
	 *         returned.
	 * @exception org.geomajas.cache.CacheException
	 *                Throws a <code>CacheIOException</code> if the file was
	 *                found, but we were unable to read from it.
	 */
	public byte[] read(RenderContent renderContent) throws CacheException {
		File file = getFileHandle(renderContent);
		String path = file.getAbsolutePath();
		waitForLock(path);
		lock(file.getAbsolutePath());
		FileInputStream in;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			unlock(path);
			return null;
		}

		byte[] content = null;
		try {
			content = read(in);
		} catch (IOException ioe) {
			unlock(path);
			throw new CacheException(ioe, ExceptionCode.CACHE_IO_EXCEPTION_ON_PATH, path);
		} finally {
			try {
				in.close();
				unlock(path);
			} catch (IOException ioe) {
				throw new CacheException(ioe, ExceptionCode.CACHE_IO_EXCEPTION_ON_PATH, path);
			}
		}

		return content;
	}

	/**
	 * Create a file corresponding to the <code>RenderContent</code> object.
	 *
	 * @param renderContent
	 *            The rendering content that we want to save as a file.
	 * @exception org.geomajas.cache.CacheException
	 *                Throws a <code>CacheIOException</code> if the file could
	 *                not be created, or if writing to the file failed, or if
	 *                the closing of the file failed.
	 */
	public void create(RenderContent renderContent) throws CacheException {
		File file = getFileHandle(renderContent);
		String path = file.getAbsolutePath();
		waitForLock(path);
		lock(path);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException ioe) {
			throw new CacheException(ioe, ExceptionCode.CACHE_IO_EXCEPTION_ON_PATH, path);
		} finally {
			unlock(path);
		}

		try {
			out.write(renderContent.getContent());
		} catch (IOException ioe) {
			throw new CacheException(ioe, ExceptionCode.CACHE_IO_EXCEPTION_ON_PATH, path);
		} finally {
			try {
				out.close();
				unlock(path);
			} catch (IOException ioe) {
				unlock(path);
				throw new CacheException(ioe, ExceptionCode.CACHE_IO_EXCEPTION_ON_PATH, path);
			}
		}
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Files are stored in sub directories: tileLevel/X/hashcode.png
	 */
	private File getFileHandle(RenderContent renderContent)
			throws CacheException {
		String id = renderContent.getId();
		MetaRenderContent meta = new MetaRenderContent(renderContent);

		String dirString = basePath + File.separator + meta.getTileLevel();
		ensureDirectory(dirString);
		dirString += File.separator + meta.getX();
		ensureDirectory(dirString);
		return new File(dirString + File.separator + id + ".png");
	}

	private void ensureDirectory(String dir) throws CacheException {
		File d = new File(dir);
		if (d.exists()) {
			return;
		}
		try {
			waitForLock(dir);
		} catch (CacheException e) {
			throw new CacheException(e, ExceptionCode.CACHE_CANNOT_CREATE_DIRECTORY, dir);
		}
		lock(dir);
		if (!d.mkdir()) {
			unlock(dir);
			throw new CacheException(ExceptionCode.CACHE_CANNOT_CREATE_DIRECTORY, dir);
		}
		unlock(dir);
	}

	private synchronized void lock(String file) {
		locks.put(file, new FileLock(file));
	}

	private synchronized FileLock unlock(String file) {
		return locks.remove(file);
	}

	private synchronized boolean isLocked(String file) {
		return locks.containsKey(file);
	}

	private void waitForLock(String file) throws CacheException {
		while (isLocked(file)) {
			String relative = file.substring(basePath.length());
			log.warn("File " + relative + " is LOCKED - waiting for it to unlock !!");
			try {
				Thread.sleep(LOCK_RETRY_TIME);
			} catch (InterruptedException e) {
				throw new CacheException(e, ExceptionCode.CACHE_INTERRUPTED_WHILE_WAITING_FOR_LOCK, file);
			}
		}
	}

	private byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(32768);
		byte[] buffer = new byte[BUFFER_SIZE];
		int count = in.read(buffer);
		while (count != -1) {
			if (count != 0) {
				out.write(buffer);
			}
			count = in.read(buffer);
		}
		in.close();
		return out.toByteArray();
	}

	/**
	 * File lock.
	 */
	private class FileLock {

		protected String name;

		// protected long time;

		public FileLock(String name) {
			this.name = name;
			// this.time = System.currentTimeMillis();
		}

		public boolean equals(Object obj) {
			if (obj instanceof FileLock && name != null) {
				return name.equalsIgnoreCase(((FileLock) obj).name);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return name != null ? name.hashCode() : 0;
		}
	}
}