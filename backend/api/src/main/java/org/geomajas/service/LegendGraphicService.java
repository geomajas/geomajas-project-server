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
package org.geomajas.service;

import java.awt.image.RenderedImage;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.legend.LegendGraphicMetadata;

/**
 * This service creates graphical symbols (typically icons) for use in a map legend. It should support most of the WMS
 * GetLegendGraphicRequest parameters.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public interface LegendGraphicService {

	/**
	 * Get the graphical symbol based on the specified legend metadata.
	 * 
	 * @param legendMetadata the legend metadata
	 * @return a rendered image for producing the final image
	 * @throws GeomajasException thrown when invalid metadata are passed
	 */
	RenderedImage getLegendGraphic(LegendGraphicMetadata legendMetadata) throws GeomajasException;
}
