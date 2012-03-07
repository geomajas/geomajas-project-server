/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.featureinfo.client.util;

import org.geomajas.annotation.Api;


/**
 * Class which helps to provide consistent look and feel for the FeatureInfo plugin, see
 * {@link org.geomajas.gwt.client.util.WidgetLayout}.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing editing these defaults.
 *
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public final class FitSetting {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF
	
	/** Whether to show a message when no results are found on the location. */
	public static boolean tooltipShowEmptyResultMessage;

	/** Whether to include raster layers when requesting feature info. */
	public static boolean featureinfoIncludeRasterLayer;

	/** Amount of pixels the mouse has to move before a new mouse over request is done. */
	public static int tooltipMinimalPixelMove = 5;
	
	/** Amount of time the mouse must retain it's position before it triggers a request to the server. */
	public static int tooltipShowDelay = 250;
	
	/** Maximum amount of results to show in the tooltip. */
	public static int tooltipMaxLabelCount = 15;
	
	/** Pixel tolerance on tooltips. */
	public static int tooltipPixelTolerance = 5;
	
	/** Pixel tolerance on featureinfo requests. */
	public static int featureInfoPixelTolerance = tooltipPixelTolerance;
	
	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private FitSetting() {
		// do not allow instantiation.
	}

}
