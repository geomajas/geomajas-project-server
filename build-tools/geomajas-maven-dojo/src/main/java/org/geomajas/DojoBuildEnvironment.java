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
		return new File(dojoDirectory, RELEASE_DIR + ".jar");
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
		} catch (Exception e) { // NOSONAR
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
		sb.append("      name: '../");
		sb.append(layerName);
		sb.append("/");
		sb.append(layerName);
		sb.append(".js',");
		sb.append("\n");
		sb.append("      dependencies:");
		sb.append(getModulesRequiresArray());
		sb.append("\n");
		sb.append("    }").append("\n");
		sb.append("  ],").append("\n");
		sb.append("  prefixes: ");
		sb.append(getModulePrefixesArray());
		sb.append("\n");
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
			sb.append("['");
			sb.append(name);
			sb.append("','../");
			sb.append(name);
			sb.append("'],");
		}
		sb.append("['");
		sb.append(layerName);
		sb.append("','../");
		sb.append(layerName);
		sb.append("'],");
		sb.setLength(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	private String getModulesRequiresArray() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String require : moduleRequires) {
			sb.append("'");
			sb.append(require);
			sb.append("',");
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
		} catch (Exception e) { // NOSONAR
			log.error("Failed to unpack artifact archive " + archive.getName(), e);
			throw new MojoExecutionException("Failed to unpack artifact archive " + archive.getName());
		}
	}

	/**
	 * Filters out non-dojo folders.
	 * 
	 * @author Jan De Moerloose
	 */
	private static class NonDojoFilter implements FilenameFilter {

		private static final Set<String> DOJO_NAMES = new HashSet<String>();

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
	 */
	private static class ModuleFilter implements FilenameFilter {

		private static final Set<String> NON_MODULE_NAMES = new HashSet<String>();

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
