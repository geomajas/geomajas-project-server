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

package org.geomajas.service.resource;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Configuration class to dynamically extend the list of root paths of the {@link org.geomajas.service.ResourceService}.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public class ResourceInfo {

	@NotNull
	private String rootPath;

	/**
	 * Get the root path to be added to the {@link org.geomajas.service.ResourceService}.
	 * 
	 * @return a base path to access resources (e.g. 'classpath:/org/geomajas', 'file:C:/' or 'WEB-INF/images')
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * Set the root path to be added to the {@link org.geomajas.service.ResourceService}.
	 * 
	 * @param rootPath a base path to access resources (e.g. 'classpath:/org/geomajas', 'file:C:/' or 'WEB-INF/images')
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

}
