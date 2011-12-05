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
public class FitSetting {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF
	
	/** Weither to show a message when no results are fount on the location. */
	public static final boolean TOOLTIP_SHOW_EMPTY_RESULTS_MESSAGE = false;

	/** Weither to include raster layers when requesting feature info. */
	public static final boolean FEATUREINFO_INCLUDE_RASTERLAYERS = false;

	/** Amount of pixels the mouse has to move before a new mouse over request is done. */
	public static int TOOLTIP_MIN_PIXEL_MOVE = 5;
	
	/** Amount of time the mouse must retain it's position before it triggers a request to the server. */
	public static int TOOLTIP_SHOW_DELAY = 250;
	
	/** Maximum amount of results to show in the tooltip. */
	public static int TOOLTIP_MAX_LABEL_COUNT = 15;
	
	/** Pixel tolerance on tooltips. */
	public static int TOOLTIP_PIXEL_TOLERANCE = 5;
	
	/** Pixel tolerance on featureinfo requests. */
	public static int FEATUREINFO_PIXEL_TOLERANCE = TOOLTIP_PIXEL_TOLERANCE;
	
	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private FitSetting() {
		// do not allow instantiation.
	}

}
