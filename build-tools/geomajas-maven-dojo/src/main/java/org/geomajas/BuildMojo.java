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
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Plugin that executes a Dojo custom build.
 *
 * @author Jan De Moerloose
 * @phase generate-resources
 * @goal build
 * @requiresDependencyResolution test
 */
public class BuildMojo extends CollectMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		super.execute();
		Artifact dojo = resolve("org.geomajas.dojo", "dojo", getDojoVersion(), "jar", null);
		DojoBuildEnvironment env = new DojoBuildEnvironment(dojo, getLog(), getArchiverManager(), getLayerName());
		env.installAndClean();
		resolveModules();
		for (Module module : getModules()) {
			env.addModule(module);
		}
		getLog().info("Using locale list : " + getLocaleList());
		env.setLocaleList(getLocaleList());
		env.setLayerOptimize(getLayerOptimize());
		env.build();
		try {
			UnArchiver ua = getArchiverManager().getUnArchiver(env.getTargetJar());
			File resourceDir = new File(getProject().getBuild().getDirectory(), "/generated-resources/dojo");
			FileUtils.forceMkdir(resourceDir);
			ua.setSourceFile(env.getTargetJar());
			ua.setDestDirectory(resourceDir);
			ua.setOverwrite(true);
			ua.extract();
		} catch (IOException e) {
			getLog().error("Failed to copy dojo release jar", e);
			throw new MojoExecutionException("Failed to copy dojo release jar");
		} catch (NoSuchArchiverException e) {
			getLog().error("Failed to unarchive dojo release jar", e);
			throw new MojoExecutionException("Failed to unarchive dojo release jar");
		} catch (ArchiverException e) {
			getLog().error("Failed to unarchive dojo release jar", e);
			throw new MojoExecutionException("Failed to unarchive dojo release jar");
		}
	}

}
