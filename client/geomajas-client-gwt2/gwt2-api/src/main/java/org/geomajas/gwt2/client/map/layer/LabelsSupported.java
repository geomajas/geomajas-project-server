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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.annotation.Api;

/**
 * Extension for layers that indicates whether or not labeling is supported. Of course, these labels can only be visible
 * if the layer itself is visible; but one can change the labels-setting nonetheless.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface LabelsSupported {

	/**
	 * Make the feature labels visible or invisible on the map.
	 * 
	 * @param labeled
	 *            Should the labels be shown or not?
	 */
	void setLabeled(boolean labeled);

	/**
	 * Are the feature labels currently visible or not?
	 * 
	 * @return Returns true or false.
	 */
	boolean isLabeled();
}