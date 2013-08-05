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

package org.geomajas.smartgwt.client.map.feature;

/**
 * <p>
 * General interface for functions to be executed in a <code>Feature</code>
 * object. These are used in the many stores.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface FeatureFunction {

	/**
	 * Execute this function!
	 * @param feature to execute
	 */
	void execute(Feature feature);
}