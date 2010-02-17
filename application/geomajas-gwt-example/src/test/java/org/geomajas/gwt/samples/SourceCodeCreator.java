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

package org.geomajas.gwt.samples;

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

	private static final String SAMPLE_FOLDER = "src/main/java/org/geomajas/gwt/client/samples/";

	private static final String DESTINATION_FOLDER = "src/main/resources/org/geomajas/gwt/client/samples/";

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
	
	private void scanFolder (File folder) {
		for (File file : folder.listFiles()) {
			if (!file.isDirectory() && file.getName().endsWith(".java")) {
				createFile(file, DESTINATION_FOLDER + folder.getName() + "/");
			}
		}
	}
	
	private void createFile(File file, String folder){
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
