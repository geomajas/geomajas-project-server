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

package org.geomajas.application.gwt.showcase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * <p>
 * This test-case creates text files containing the source of the individual samples.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class SourceCodeCreator {

	private static final String SAMPLE_FOLDER = "src/main/java/org/geomajas/example/gwt/client/samples/";

	private static final String DESTINATION_FOLDER = "src/main/resources/org/geomajas/example/gwt/client/samples/";

	private static final String SEARCH_STRING = "\tpublic Canvas getViewPanel";

	@Test
	public void createSampleSourceCode() {
		File folder = new File(SAMPLE_FOLDER);
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				scanFolder(file);
			}
		}
	}

	private void scanFolder(File folder) {
		for (File file : folder.listFiles()) {
			if (!file.isDirectory() && file.getName().endsWith(".java")) {
				createFile(file, DESTINATION_FOLDER + folder.getName() + "/");
			}
		}
	}

	private void createFile(File file, String folder) {
		try {
			// Get the Java source:
			String javaContent = new String(read(new FileInputStream(file)));
			int position = javaContent.indexOf(SEARCH_STRING);
			if (position > 0) {
				String blop = javaContent.substring(position);
				position = blop.indexOf("\n\t}");
				blop = blop.substring(0, position + 3);

				// Create or overwrite the HTML file:
				String fileName = file.getName().substring(0, file.getName().length() - 4) + "txt";
				File htmlFile = new File(folder + fileName);
				File folderFile = new File(folder);
				folderFile.mkdir();
				FileOutputStream out = new FileOutputStream(htmlFile);
				out.write(blop.getBytes());
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(32768);
		byte[] buffer = new byte[1024];
		int count = in.read(buffer);
		while (count != -1) {
			if (count != 0) {
				out.write(buffer, 0, count);
			}
			count = in.read(buffer);
		}
		in.close();
		return out.toByteArray();
	}
}
