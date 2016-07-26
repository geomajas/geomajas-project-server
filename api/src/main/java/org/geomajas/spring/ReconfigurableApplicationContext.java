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
package org.geomajas.spring;

import org.geomajas.global.GeomajasException;

/**
 * 
 * Interface for Spring application context that supports rollback.
 * 
 * @author Jan De Moerloose
 */
public interface ReconfigurableApplicationContext {

	/**
	 * Refresh this context with the specified configuration locations.
	 * 
	 * @param configLocations
	 *            list of configuration resources (see implementation for specifics)
	 * @throws GeomajasException
	 *             indicates a problem with the new location files (see cause)
	 */
	void refresh(String[] configLocations) throws GeomajasException;

	/**
	 * Roll back to the previous configuration.
	 * 
	 * @throws GeomajasException
	 *             indicates an unlikely problem with the rollback (see cause)
	 */
	void rollback() throws GeomajasException;
}
