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
package org.geomajas.service.legend;

import org.geomajas.annotation.Api;
import org.geomajas.sld.NamedStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;

/**
 * Metadata for creating a legend graphic.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public interface LegendGraphicMetadata {

	/**
	 * Default width of the legend graphic.
	 */
	int DEFAULT_WIDTH = 18;

	/**
	 * Default height of the legend graphic.
	 */
	int DEFAULT_HEIGHT = 18;

	/**
	 * Get layer id, optional.
	 * 
	 * @return layer id
	 */
	String getLayerId();

	/**
	 * Get the user style, optional.
	 * 
	 * @return user style
	 */
	UserStyleInfo getUserStyle();

	/**
	 * Get the named style, optional.
	 * 
	 * @return named style
	 */
	NamedStyleInfo getNamedStyle();

	/**
	 * Get the rule, optional.
	 * 
	 * @return the rule
	 */
	RuleInfo getRule();

	/**
	 * Get the scale, optional. This scale will be used to determine the applicable rules.
	 * 
	 * @return the scale
	 */
	double getScale();

	/**
	 * Get the suggested width of the graphic, optional.
	 * 
	 * @return the width
	 */
	int getWidth();

	/**
	 * Get the suggested height of the graphic, optional.
	 * 
	 * @return the height
	 */
	int getHeight();

}
