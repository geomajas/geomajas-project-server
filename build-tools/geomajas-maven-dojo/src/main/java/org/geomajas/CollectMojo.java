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
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Plugin that collects dojo modules in a modules script for easy referencing in html pages.
 *
 * @author Jan De Moerloose
 * @goal collect
 * @phase generate-resources
 */
public class CollectMojo extends DojoMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		String resourceDir = getProject().getBuild().getDirectory() + "/generated-resources/dojo";
		try {
			FileUtils.forceMkdir(new File(resourceDir));
			createModulesScript(resourceDir);
			ArrayList<String> includes = new ArrayList<String>();
			includes.add("**/*");
			projectHelper.addResource(project, resourceDir, includes, Collections.EMPTY_LIST);
		} catch (IOException e) {
			getLog().error("Could not create directory " + resourceDir, e);
			throw new MojoExecutionException("Could not created directory " + resourceDir);
		}
	}

	private void createModulesScript(String resourceDir) throws MojoExecutionException {

		try {
			File layerDir = new File(resourceDir, getLayerName());
			FileUtils.forceMkdir(layerDir);
			File profile = new File(layerDir, getLayerName() + ".js");
			StringBuilder sb = new StringBuilder();
			for (Module module : getModules()) {
				String[] requires = module.getRequires().split(",");
				for (String require : requires) {
					sb.append("dojo.require('" + require.trim() + "');\n");
				}
			}
			getLog().info("Writing modules script " + profile.getAbsolutePath());
			FileUtils.fileWrite(profile.getAbsolutePath(), sb.toString());
		} catch (IOException e) {
			getLog().error("Failed to write modules script", e);
			throw new MojoExecutionException("Failed to write modules script");
		}
	}

}
