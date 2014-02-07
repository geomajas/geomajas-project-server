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
package org.geomajas.plugin.deskmanager.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public final class FileUtils {

	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

	private FileUtils() {
	}

	public static File createTmpFolder() throws IOException {
		File root = org.apache.commons.io.FileUtils.getTempDirectory();
		File tmpFolder;
		do {
			tmpFolder = new File(root, UUID.randomUUID().toString());
		} while (tmpFolder.isDirectory());
		tmpFolder.mkdir();

		return tmpFolder;
	}

	/**
	 * Zip all files in a directory, only relative filenames are used, does not recurse.
	 * 
	 * @param sourceFolder
	 * @param targetFileName
	 * @return
	 * @throws IOException
	 */
	public static File zipDirectory(File sourceFolder, String targetFileName) throws IOException {
		File target = new File(sourceFolder, targetFileName + (targetFileName.endsWith(".zip") ? "" : ".zip"));
		if (target.isFile()) {
			throw new IOException("Bestand bestaat reeds! " + target.getAbsolutePath());
		}
		File[] files = sourceFolder.listFiles();

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target));
		WritableByteChannel out = Channels.newChannel(zos);
		FileInputStream is = null;
		try {
			for (File file : files) {
				zos.putNextEntry(new ZipEntry(file.getName()));

				is = new FileInputStream(file);
				FileChannel in = is.getChannel();
				in.transferTo(0, in.size(), out);

				is.close();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
			if (zos != null) {
				zos.close();
			}
		}

		return target;
	}

	public static String toSafeForFileName(String text) {
		return text.replaceAll("\\W", "_");
	}

	/**
	 * Deletes the given directory (including files).
	 * 
	 * @param path
	 * @return
	 */
	public static void deleteFolder(File folder) {
		try {
			if (folder != null && folder.isDirectory()) {
				org.apache.commons.io.FileUtils.deleteDirectory(folder);
			}
		} catch (Exception e) {
			LOG.warn("Failed deleting folder: " + folder.getAbsolutePath() + " -- " + e.getMessage());
		}
	}
}
