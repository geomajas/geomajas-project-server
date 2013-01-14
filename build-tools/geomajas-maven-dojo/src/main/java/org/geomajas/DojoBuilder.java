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
package org.geomajas;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Dojo builder.
 *
 * @author Jan De Moerloose
 */
public class DojoBuilder {

	private String javaExec = "java";
	private String dojoDirectory;
	private String profilePath;
	private String mainClass = "org.mozilla.javascript.tools.shell.Main";
	private String buildFile = "build.js";
	private String layerOptimize = "none";
	private String localeList;
	private List<String> mainParams = new ArrayList<String>();
	private List<String> classpathArgs = new ArrayList<String>();
	private Log log;

	public void build() throws MojoExecutionException {
		mainParams.clear();
		mainParams.add("releaseDir=../../");
		mainParams.add("releaseName=release");
		mainParams.add("localeList=" + localeList);
		mainParams.add("internStrings=false");
		mainParams.add("action=release");
		mainParams.add("layerOptimize=" + layerOptimize);
		mainParams.add("profileFile=" + profilePath);
		Commandline commandLine = new Commandline();
		commandLine.setExecutable(javaExec);
		commandLine.setWorkingDirectory(dojoDirectory + "/util/buildscripts");
		List<String> commandArgs = new ArrayList<String>();
		// add the class path
		if (classpathArgs.size() > 0) {
			commandArgs.add("-classpath");
			String classpath = "";
			for (int i = 0; i < classpathArgs.size(); i++) {
				classpath += (i == 0 ? "" : File.pathSeparator);
				classpath += classpathArgs.get(i);
			}
			commandArgs.add(classpath);
		}
		// add the rest...
		commandArgs.add(mainClass);
		commandArgs.add(buildFile);
		for (String param : mainParams) {
			commandArgs.add(param);
		}
		commandLine.addArguments(commandArgs.toArray(new String[commandArgs.size()]));

		try {
			log.info("Executing " + commandLine.toString());
			int resultCode = CommandLineUtils.executeCommandLine(commandLine, new LogStream(), new LogStream());
			if (resultCode > 0) {
				throw new MojoExecutionException("Result of " + commandLine + " execution is: '" + resultCode + "'.");
			}
		} catch (CommandLineException e) {
			throw new MojoExecutionException("Command execution failed.", e);
		}
	}

	public void addToClasspath(String archiveOrFolder) {
		classpathArgs.add(archiveOrFolder);
	}

	public String getDojoDirectory() {
		return dojoDirectory;
	}

	public void setDojoDirectory(String dojoDirectory) {
		this.dojoDirectory = dojoDirectory;
	}

	public String getProfilePath() {
		return profilePath;
	}

	public void setProfilePath(String profilePath) {
		this.profilePath = profilePath;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	/**
	 * Logging stream consumer.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	class LogStream implements StreamConsumer {

		public void consumeLine(String line) {
			log.info(line);
		}

	}

	public void setLocaleList(String localeList) {
		this.localeList = localeList;
	}

	public String getLayerOptimize() {
		return layerOptimize;
	}

	public void setLayerOptimize(String layerOptimize) {
		this.layerOptimize = layerOptimize;
	}
}
