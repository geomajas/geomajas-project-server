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
package org.geomajas;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages the Dojo build environment: setup, clean, clean all.
 *
 * @author Jan De Moerloose
 */
public class DojoBuildEnvironment {

	private File dojoDirectory;
	private File releaseDirectory;
	private Artifact dojoArtifact;
	private ArchiverManager archiverManager;
	private Log log;
	private List<String> moduleRequires = new ArrayList<String>();
	private List<String> moduleNames = new ArrayList<String>();
	private String localeList = "en";
	private String layerOptimize = "none";

	private static final String BUILD_DIR = "target";
	private static final String RELEASE_DIR = "release";
	private String layerName;

	public DojoBuildEnvironment(Artifact dojoArtifact, Log log, ArchiverManager archiverManager, String layerName) {
		this(new File(dojoArtifact.getFile().getParent() + File.separator + BUILD_DIR), dojoArtifact, log,
				archiverManager, layerName);
	}

	public DojoBuildEnvironment(File dojoDirectory, Artifact dojoArtifact, Log log, ArchiverManager archiverManager,
			String layerName) {
		this.dojoDirectory = dojoDirectory;
		this.releaseDirectory = new File(dojoDirectory, RELEASE_DIR);
		this.dojoArtifact = dojoArtifact;
		this.log = log;
		this.archiverManager = archiverManager;
		this.layerName = layerName;
	}

	public void installAndClean() throws MojoExecutionException {
		// clean directory and unpack dojo
		if (!isInstalled()) {
			try {
				if (dojoDirectory.exists()) {
					log.info("Deleting directory " + dojoDirectory.getAbsolutePath());
					FileUtils.deleteDirectory(dojoDirectory);
				}
				log.info("Creating directory " + dojoDirectory.getAbsolutePath());
				FileUtils.mkdir(dojoDirectory.getAbsolutePath());
			} catch (IOException e) {
				log.error("Failed to install dojo build environment", e);
				throw new MojoExecutionException("Failed to install dojo build environment");
			}
			unpackArchive(dojoArtifact.getFile(), dojoDirectory);
		}
		cleanPrevious();
	}

	public void cleanAll() throws MojoExecutionException {
		if (dojoDirectory.exists()) {
			try {
				log.info("Deleting directory " + dojoDirectory.getAbsolutePath());
				FileUtils.deleteDirectory(dojoDirectory);
			} catch (IOException e) {
				log.error("Failed to clean dojo build environment", e);
				throw new MojoExecutionException("Failed to clean dojo build environment");
			}
		}
	}

	public void addModule(Module module) throws MojoExecutionException {
		unpackArchive(module.getArtifact().getFile(), dojoDirectory);
		moduleRequires.add(module.getRequires());
	}

	public void build() throws MojoExecutionException {
		collectModuleNames();
		File profile = createProfile();
		DojoBuilder builder = new DojoBuilder();
		String version = dojoArtifact.getVersion();
		if (version.startsWith("1.1")) {
			builder.addToClasspath("../shrinksafe/custom_rhino.jar");
		} else {
			builder.addToClasspath("../shrinksafe/js.jar");
			builder.addToClasspath("../shrinksafe/shrinksafe.jar");
		}
		builder.setLog(log);
		builder.setDojoDirectory(dojoDirectory.getAbsolutePath());
		builder.setProfilePath("../../" + profile.getName());
		builder.setLocaleList(localeList);
		builder.setLayerOptimize(layerOptimize);
		builder.build();
		// hack, layerOptimize none has a bug in it and we can't skip optimization
		if ("none".equals(layerOptimize)) {
			File layerDirectory = new File(releaseDirectory, layerName);
			File compressedLayer = new File(layerDirectory, layerName + ".js");
			log.info("Deleting " + compressedLayer.getAbsolutePath());
			compressedLayer.delete();
			File uncompressedLayer = new File(layerDirectory, layerName + ".js.uncompressed.js");
			log.info("Renaming " + uncompressedLayer.getAbsolutePath() + " to " + compressedLayer.getAbsolutePath());
			uncompressedLayer.renameTo(compressedLayer);
		}
		archive();
	}

	public File getTargetJar() {
		File target = new File(dojoDirectory, RELEASE_DIR + ".jar");
		return target;
	}

	public void setLocaleList(String localeList) {
		this.localeList = localeList;
	}

	public void setLayerOptimize(String layerOptimize) {
		this.layerOptimize = layerOptimize;
	}

	private boolean isInstalled() {
		return dojoDirectory.exists() && dojoDirectory.lastModified() >= dojoArtifact.getFile().lastModified();
	}

	private void cleanPrevious() throws MojoExecutionException {
		// remove all non-dojo directories/files in target
		for (File file : dojoDirectory.listFiles(new NonDojoFilter())) {
			if (file.isDirectory()) {
				try {
					log.info("Deleting directory " + file.getAbsolutePath());
					FileUtils.deleteDirectory(file);
				} catch (IOException e) {
					log.error("Failed to clean dojo build environment", e);
					throw new MojoExecutionException("Failed to clean dojo build environment");
				}
			} else {
				log.info("Deleting file " + file.getAbsolutePath());
				if (!file.delete()) {
					throw new MojoExecutionException("Cannot delete " + file.getAbsolutePath());
				}
			}
		}
	}

	private void collectModuleNames() {
		for (File file : dojoDirectory.listFiles(new ModuleFilter())) {
			moduleNames.add(file.getName());
		}
	}

	private void archive() throws MojoExecutionException {
		// jar the result
		try {
			JarArchiver archiver = new JarArchiver();
			archiver.addDirectory(releaseDirectory, new String[]{"*/**"}, new String[]{});
			archiver.setDestFile(new File(dojoDirectory, RELEASE_DIR + ".jar"));
			archiver.createArchive();
		} catch (Exception e) {
			log.error("Failed to archive dojo build", e);
			throw new MojoExecutionException("Failed to archive dojo build");
		}
	}

	private File createProfile() throws MojoExecutionException {
		File profile = new File(dojoDirectory, layerName + ".profile.js");
		StringBuilder sb = new StringBuilder();
		sb.append("dependencies ={").append("\n");
		sb.append("  layers:  [").append("\n");
		sb.append("    {").append("\n");
		sb.append("      name: '../" + layerName + "/" + layerName + ".js',").append("\n");
		sb.append("      dependencies:" + getModulesRequiresArray()).append("\n");
		sb.append("    }").append("\n");
		sb.append("  ],").append("\n");
		sb.append("  prefixes: " + getModulePrefixesArray()).append("\n");
		sb.append("};").append("\n");
		try {
			log.info("Writing profile " + profile.getAbsolutePath());
			FileUtils.fileWrite(profile.getAbsolutePath(), sb.toString());
			return profile;
		} catch (IOException e) {
			log.error("Failed to write profile file", e);
			throw new MojoExecutionException("Failed to write profile file");
		}
	}

	private String getModulePrefixesArray() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String name : moduleNames) {
			sb.append("['" + name + "','../" + name + "'],");
		}
		sb.append("['" + layerName + "','../" + layerName + "'],");
		sb.setLength(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	private String getModulesRequiresArray() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String require : moduleRequires) {
			sb.append("'" + require + "',");
		}
		// remove last comma
		if (moduleRequires.size() > 0) {
			sb.setLength(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Unpack the libraries in the repository so that Hosted mode browser can be executed without requirement to install
	 * GWT on computer.
	 *
	 * @param archive jar file to unpack
	 * @param destinationDirectory destination directory
	 * @throws MojoExecutionException some error occurred
	 */
	private void unpackArchive(File archive, File destinationDirectory) throws MojoExecutionException {
		try {
			UnArchiver unArchiver = archiverManager.getUnArchiver(archive);
			unArchiver.setSourceFile(archive);
			unArchiver.setDestDirectory(destinationDirectory);
			unArchiver.setOverwrite(false);
			unArchiver.extract();
			log.info("Unpacked artifact archive " + archive.getName());
		} catch (Exception e) {
			log.error("Failed to unpack artifact archive " + archive.getName(), e);
			throw new MojoExecutionException("Failed to unpack artifact archive " + archive.getName());
		}
	}

	/**
	 * Filters out non-dojo folders.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	private static class NonDojoFilter implements FilenameFilter {

		private static Set<String> DOJO_NAMES = new HashSet<String>();

		static {
			DOJO_NAMES.add("dojo");
			DOJO_NAMES.add("dojox");
			DOJO_NAMES.add("dijit");
			DOJO_NAMES.add("util");
		}

		public boolean accept(File file, String name) {
			return !DOJO_NAMES.contains(name);
		}

	}

	/**
	 * Filters out dojo modules.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	private static class ModuleFilter implements FilenameFilter {

		private static Set<String> NON_MODULE_NAMES = new HashSet<String>();

		static {
			NON_MODULE_NAMES.add("dojo");
			NON_MODULE_NAMES.add("META-INF");
			NON_MODULE_NAMES.add("tests");
			NON_MODULE_NAMES.add("util");
		}

		public boolean accept(File file, String name) {
			return !NON_MODULE_NAMES.contains(name) && file.isDirectory();
		}

	}

}
