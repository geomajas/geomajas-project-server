/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.service;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;
import org.springframework.core.io.Resource;

/**
 * Service to locate resources in the classpath, the file system, the web context or a list of configurable root paths.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public interface ResourceService {

	/**
	 * Find a resource. If a non-null resource is returned, it is ensured to exist.
	 * 
	 * @param location the location
	 * @return the resource or null if the resource does not exist
	 * @throws GeomajasException oops
	 */
	Resource find(String location) throws GeomajasException;

	/**
	 * Returns the list of root paths used by this service. In case the resource cannot be resolved directly, the
	 * service will consecutively prepend each root path before the resource location in order to find an existing
	 * resource. Example root paths are 'classpath:/org/geomajas' or 'file:C:/' or 'WEB-INF/images'.
	 * 
	 * @return the list of root paths
	 */
	List<String> getRootPaths();

	/**
	 * Specifies the list of root paths to be used by this service. In case the resource cannot be resolved directly,
	 * the service will consecutively prepend each root path before the resource location in order to find an existing
	 * resource. Example root paths are 'classpath:/org/geomajas' or 'file:C:/' or 'WEB-INF/images'.
	 * 
	 * @param rootPaths list of path strings
	 */
	void setRootPaths(List<String> rootPaths);

}
