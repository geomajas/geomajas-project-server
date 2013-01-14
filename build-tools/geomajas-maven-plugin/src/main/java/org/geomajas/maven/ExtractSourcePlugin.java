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
	private static final String SKIP_START_ANNOTATION = "@extract-skip-start";
	private static final String SKIP_END_ANNOTATION = "@extract-skip-end";

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

			System.out.println("Extract source " + source.getAbsolutePath());
			System.out.println("Extract dest   " + destination.getAbsolutePath());
			scanDirectory(source, destination);
		} catch (Throwable ex) {
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
			if (!name.startsWith(".") &&
					!name.endsWith("~") && !name.endsWith(".bak") &&
					!name.endsWith(".exe") &&
					!name.endsWith(".jpg") && !name.endsWith(".jpeg") &&
					!name.endsWith(".png") &&
					!name.endsWith(".gif") && 
					!name.endsWith(".tif") && !name.endsWith(".tiff")) {
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

		String fileNameLower = file.getName().toLowerCase();
		if (fileNameLower.endsWith(".java") || fileNameLower.endsWith(".js")) {
			startAnnotation = START_JAVA_ANNOTATION;
			endAnnotation = END_JAVA_ANNOTATION;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		List<String> lines = new ArrayList<String>();
		String declaration = null;
		boolean skipping = false;

		while ((line = reader.readLine()) != null) {
			if (null != declaration) {
				if (line.contains(endAnnotation)) {
					prepareLines(lines);
					createFile(declaration, lines, destination, getLanguage(file));
					declaration = null;
					lines.clear();
				} else {
					if (line.contains(SKIP_START_ANNOTATION)) {
						skipping = true;
					}
					if (!skipping) {
						lines.add(line);
					}
					if (line.contains(SKIP_END_ANNOTATION)) {
						skipping = false;
					}
				}
			} else {
				if (line.contains(startAnnotation)) {
					declaration = line.substring(startAnnotation.length() + line.indexOf(startAnnotation)).trim();
				}
			}
		}
		
		reader.close();

		if (null != declaration) {
			prepareLines(lines);
			createFile(declaration, lines, destination, getLanguage(file));
		}
	}

	private String getLanguage(File file) {
		String fileName = file.getName();
		int pos = fileName.lastIndexOf('.');
		if (pos > 0) {
			String extension = fileName.substring(pos + 1);
			if ("java".equalsIgnoreCase(extension)) {
				return "java";
			}
			if ("xml".equalsIgnoreCase(extension)) {
				return "xml";
			}
			if ("js".equalsIgnoreCase(extension)) {
				return "javascript";
			}
		}
		return null;
	}

	/**
	 * Prepare lines for inclusion in XML. Includes removing the indentation from the entire group, replacing tabs and
	 * assuring no invalid characters are used.
	 *
	 * @param lines lines to prepare
	 */
	public void prepareLines(List<String> lines) {
		if (lines.size() > 0) {
			int indentation = Integer.MAX_VALUE;
			for (int i = 0 ; i < lines.size() ; i++) {
				String line = lines.get(i);
				line = line.replace("\t", "    ");
				indentation = Math.min(indentation, countIndent(line));
				lines.set(i, line);
			}
			for (int i = 0 ; i < lines.size() ; i++) {
				String line = lines.get(i);
				line = line.substring(indentation);
				lines.set(i, line);
			}
		}
	}

	/**
	 * Count number of characters indentation for the line.
	 *
	 * @param line line to find indentation for
	 * @return indentation in characters
	 */
	public int countIndent(String line) {
		for (int i = 0 ; i < line.length() ; i++) {
			if (!Character.isSpaceChar(line.charAt(i))) {
				return i;
			}
		}
		return line.length();
	}

	/**
	 * Create file for inclusion in docbook document.
	 * <p/>
	 * The filename and optional caption are extracted from the declaration (the first comment line).
	 * The file is created in the destination directory.
	 *
	 * @param declaration part of comment line after marker, contains filename and optional caption separated by comma
	 * @param lines lines to store in file
	 * @param destinationDir directory to put file
	 * @param language programming language for the input file
	 * @throws IOException oops while creating file
	 */
	public void createFile(String declaration, List<String> lines, File destinationDir, String language)
			throws IOException {
		if (!destinationDir.isDirectory()) {
			destinationDir.mkdirs();
		}
		String filename = declaration;
		String caption = "";
		int pos = declaration.indexOf(',');
		if (pos > 0) {
			filename = declaration.substring(0, pos).trim();
			caption = declaration.substring(pos + 1).trim();
		}
		if (caption.endsWith("-->")) {
			caption = caption.substring(0, caption.length() - 3).trim();
		}
		caption = caption.replace("&", "&amp;");
		caption = caption.replace("<", "&lt;");
		caption = caption.replace(">", "&gt;");

		File targetFile = new File(destinationDir, filename + ".xml");
		BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<example id=\"");
		writer.write(filename);
		writer.write("\">\n");
		writer.write("<title>");
		writer.write(caption);
		writer.write("</title>\n");
		writer.write("<programlisting");
		if (null != language) {
			writer.write(" language=\"");
			writer.write(language);
			writer.write("\"");
		}
		writer.write("><![CDATA[");
		boolean first = true;
		for (String line : lines) {
			if (!first) {
				writer.newLine();
			}
			first = false;
			writer.write(line);
		}
		writer.write("]]>\n");
		writer.write("</programlisting>\n");
		writer.write("</example>\n");
		writer.flush();
		writer.close();
	}

}
