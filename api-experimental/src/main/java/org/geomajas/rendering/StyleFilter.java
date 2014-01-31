/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.rendering;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FeatureStyleInfo;
import org.opengis.filter.Filter;

/**
 * <p>
 * Simple interface for defining a style filter. It couples a style-definition to an OpenGIS filter implementation.
 * </p>
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface StyleFilter {

	/**
	 * Return the opengis filter implementation coupled to a style.
	 *
	 * @return opengis filter
	 */
	Filter getFilter();

	/**
	 * A style definition object as described in the geomajas configuration.
	 *
	 * @return style info
	 */
	FeatureStyleInfo getStyleDefinition();
}
