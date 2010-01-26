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
			File profile = new File(layerDir, getLayerName()+".js");
			StringBuffer sb = new StringBuffer();
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
