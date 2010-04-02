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

package org.geomajas.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Maven plugin to extract excerpts from source files for inclusion in docbook documents.
 * <p/>
 * It scans all files in the given directory tree looking for the begin pattern and extracts the content starting from
 * the next line, until (excluding) a line that contains the end comment.
 * <p/>
 * Some sanitization is done on the source, like replacing all tab characters by four spaces and removing the left
 * margin.
 *
 * @author Joachim Van der Auwera
 * @goal extract
 * @description extract source excepts for xi:include in docbook
 * @inherited false
 */
public class ExtractSourcePlugin extends AbstractMojo {

	private static final String START_JAVA_ANNOTATION = "// @extract-start";
	private static final String END_JAVA_ANNOTATION = "// @extract-end";
	private static final String START_XML_ANNOTATION = "<!-- @extract-start";
	private static final String END_XML_ANNOTATION = "<!-- @extract-end";

	/**
	 * @parameter expression="${basedir}/src/main"
	 * @required
	 */
	private String sourceDirectory;

	/**
	 * @parameter expression="${basedir}/target/docbook"
	 * @required
	 */
	private String destinationDirectory;

	public void execute() throws MojoExecutionException {
		try {
			File source = new File(sourceDirectory);
			File destination = new File(destinationDirectory);
			scanDirectory(source, destination);
		}
		catch (Throwable ex) {
			throw new MojoExecutionException("problems while running extract source plugin\n" + ex.getMessage(), ex);
		}
	}

	/**
	 * Recursively scan a directory to find extract source excerpts.
	 * <p/>
	 * This will not check files with a name starting with a dot or ending in a tilde ("~") or ".bak".
	 *
	 * @param source directory to scan
	 * @param destination directory for result
	 * @throws IOException read or write problem
	 */
	public void scanDirectory(File source, File destination) throws IOException {
		for (File file : source.listFiles()) {
			String name = file.getName();
			if (!name.startsWith(".") && !name.endsWith("~") && !name.endsWith(".bak")) {
				if (file.isDirectory()) {
					scanDirectory(file, destination);
				} else {
					extractAnnotatedCode(file, destination);
				}
			}
		}
	}

	public void extractAnnotatedCode(File file, File destination) throws IOException {

		String startAnnotation = START_XML_ANNOTATION;
		String endAnnotation = END_XML_ANNOTATION;

		if (file.getName().toLowerCase().endsWith(".java")) {
			startAnnotation = START_JAVA_ANNOTATION;
			endAnnotation = END_JAVA_ANNOTATION;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		List<String> lines = new ArrayList<String>();
		String declaration = null;

		while ((line = reader.readLine()) != null) {
			if (null != declaration) {
				if (line.contains(endAnnotation)) {
					createFile(declaration, lines, destination);
					declaration = null;
					lines.clear();
				} else {
					lines.add(line);
				}
			} else {
				if (line.contains(startAnnotation)) {
					declaration = line.substring(startAnnotation.length() + line.indexOf(startAnnotation)).trim();
				}
			}
		}

		if (null != declaration) {
			createFile(declaration, lines, destination);
		}
	}

	public void createFile(String declaration, List<String> lines, File destinationDir) throws IOException {
		// @todo	
	}

	public BufferedWriter createXiIncludeFile(String file, File destination) throws IOException {

		File targetFile = new File(destination, file + ".xml");

		BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));

		// write
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.newLine();
		writer.write("<para>");
		writer.newLine();
		writer.write("<programlisting><![CDATA[");
		writer.newLine();
		return writer;
	}

	public void closeFile(BufferedWriter writer) throws IOException {
		writer.write("...]]>");
		writer.newLine();
		writer.write("</programlisting>");
		writer.newLine();
		writer.write("</para>");
		writer.flush();
		writer.close();
	}

}
