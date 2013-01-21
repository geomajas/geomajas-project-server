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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk;

import org.geomajas.annotation.Api;

/**
 * @author Oliver May
 * 
 * @since 1.0.0
 */
@Api
public final class GeodeskLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF
	
	/**
	 * Version of the application.
	 */
	public static String version = "";
	
	/**
	 * Build number of the application.
	 */
	public static String build = "";
	
	/**
	 * Z index for the loading screen.
	 */
	public static int loadingZindex = 300000;

	// CHECKSTYLE VISIBILITY MODIFIER: ON
	
	public static final String CLIENT_WIDGET_INFO_RIBBON = "ribbon-bar";

	public static final String MAPMAIN_ID = "mainMap";

	public static final String MAPOVERVIEW_ID = "overviewMap";

	public static final String STYLE_RIBBONPANELREL = "msRibbonPanelRel";

	public static final String STYLE_RIBBONPANELRELTITLE = "msRibbonPanelRelTitle";

	public static final String STYLE_RIBBONPANELRELTEXT = "msRibbonPanelRelText";

	public static final String STYLE_RIBBONPANEL_SUBPANELBORDERS = "msRibbonPanelSubPanelBorders";

	public static final int EXCEPTIONCODE_LOKETINACTIVE = 10000001;

	private GeodeskLayout() { }


}
