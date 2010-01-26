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
		DojoBuildEnvironment env = new DojoBuildEnvironment(dojo, getLog(), getArchiverManager(),getLayerName());
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
