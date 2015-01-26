/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.global;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Metadata for a plugin, allowing dependencies to be declared, and plug-in information to be made available to the
 * back-end.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 */
@Api(allMethods = true)
public class PluginInfo {

	@NotNull
	private PluginVersionInfo version;

	@NotNull
	private String backendVersion;

	private List<PluginVersionInfo> dependencies;

	private List<PluginVersionInfo> optionalDependencies;

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
	 * Get the minimum version for optional dependencies.
	 *
	 * @return optional dependencies
	 * @since 1.9.0
	 */
	public List<PluginVersionInfo> getOptionalDependencies() {
		return optionalDependencies;
	}

	/**
	 * Set the minimum version for optional dependencies.
	 *
	 * @param optionalDependencies optional dependencies
	 * @since 1.9.0
	 */
	public void setOptionalDependencies(List<PluginVersionInfo> optionalDependencies) {
		this.optionalDependencies = optionalDependencies;
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
	
	/**
	 * {@inheritDoc}
	 * @since 1.10.0
	 */
	public String toString() {
		return "PluginInfo(version=" + this.getVersion() + ", backendVersion=" + this.getBackendVersion() + ")";
	}
}
