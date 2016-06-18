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

import java.awt.image.RenderedImage;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.legend.LegendGraphicMetadata;

/**
 * This service creates graphical symbols (typically icons) for use in a map legend. It should support most of the WMS
 * GetLegendGraphicRequest parameters.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
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
	
	/**
	 * Get the graphical symbols based on the specified legend metadata.
	 * 
	 * @param legendMetadata a list of legend metadata
	 * @return a rendered image that contains all legend symbols stacked vertically
	 * @throws GeomajasException thrown when invalid metadata are passed
	 * @since 1.13.0
	 */
	RenderedImage getLegendGraphics(List<LegendGraphicMetadata> legendMetadata) throws GeomajasException;
}
