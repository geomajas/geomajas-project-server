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

package org.geomajas.global;

import java.util.ArrayList;
import java.util.List;

/**
 * Metadata for a plugin, allowing dependencies to be declared, and plug-in information to be made available to the
 * back-end.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 */
@Api(allMethods = true)
public class PluginInfo {

	private PluginVersionInfo version;
	
	private String backendVersion;

	private List<PluginVersionInfo> dependencies;

	private List<CopyrightInfo> copyrightInfo;

	/**
	 * Get version information for this plug-in.
	 *
	 * @return plug-in version info
	 */
	public PluginVersionInfo getVersion() {
		return version;
	}

	/**
	 * Set plug-in information for this plug-in.
	 *
	 * @param version plg-in version info
	 */
	public void setVersion(PluginVersionInfo version) {
		this.version = version;
	}

	/**
	 * Get back-end / API minimum version which is required for this plug-in.
	 *
	 * @return back-end minimum version
	 */
	public String getBackendVersion() {
		return backendVersion;
	}

	/**
	 * Set back-end / API minimum version which is required for this plug-in.
	 *
	 * @param backendVersion back-end minimum version
	 */
	public void setBackendVersion(String backendVersion) {
		this.backendVersion = backendVersion;
	}

	/**
	 * Get dependency information for this plug-in.
	 *
	 * @return dependencies
	 */
	public List<PluginVersionInfo> getDependencies() {
		if (null == dependencies) {
			dependencies = new ArrayList<PluginVersionInfo>();
		}
		return dependencies;
	}

	/**
	 * Set dependencies for this plug-in.
	 *
	 * @param dependencies dependencies
	 */
	public void setDependencies(List<PluginVersionInfo> dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * Get copyright info for this plug-in and the dependencies.
	 *
	 * @return copyright info
	 */
	public List<CopyrightInfo> getCopyrightInfo() {
		if (null == copyrightInfo) {
			copyrightInfo = new ArrayList<CopyrightInfo>();
		}
		return copyrightInfo;
	}

	/**
	 * Set copyright info for this plug-in and the dependencies.
	 *
	 * @param copyrightInfo copyright info
	 */
	public void setCopyrightInfo(List<CopyrightInfo> copyrightInfo) {
		this.copyrightInfo = copyrightInfo;
	}
}
