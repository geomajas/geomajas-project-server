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

/**
 * Plugin that cleans up the Dojo build environment.
 *
 * @author Jan De Moerloose
 * @phase package
 * @goal clean
 */
public class CleanMojo extends DojoMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		Artifact dojo = resolve("org.geomajas.dojo", "dojo", getDojoVersion(), "jar", null);
		DojoBuildEnvironment env = new DojoBuildEnvironment(dojo, getLog(), getArchiverManager(), getLayerName());
		getLog().info("Cleaning dojo build directory");
		env.cleanAll();
	}

}
