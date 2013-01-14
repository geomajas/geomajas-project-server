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

package org.geomajas.gwt.client.map.feature;

import java.util.List;

/**
 * Execution function that can be passed on to the LazyLoader to be executed when the lazy loading completes.
 * 
 * @author Joachim Van der Auwera
 */
public interface LazyLoadCallback {

	/**
	 * The actual execution function. When the lazy loading is finished, this will be executed.
	 * 
	 * @param response
	 *            updated features
	 */
	void execute(List<Feature> response);
}
