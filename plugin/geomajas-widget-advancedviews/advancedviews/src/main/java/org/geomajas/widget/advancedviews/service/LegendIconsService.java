/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews.service;

import java.awt.Image;

import org.geomajas.global.Api;
import org.geomajas.widget.advancedviews.AdvancedviewsException;

/**
 * Service for creating legend icons.
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface LegendIconsService {
	
	/**
	 * Create the icon for the given widget/layer/style/index.
	 * Parameters use ids so icon easily be requested with a simple get-url.
	 * @param widgetId
	 * @param layerId
	 * @param styleName
	 * @param featureStyleId
	 * @return
	 * @throws AdvancedviewsException
	 */
	Image createLegendIcon(String widgetId, String layerId, String styleName, String featureStyleId)
			throws AdvancedviewsException;

}
