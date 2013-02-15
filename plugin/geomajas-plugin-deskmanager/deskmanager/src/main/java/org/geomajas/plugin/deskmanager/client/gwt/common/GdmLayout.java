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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.util.WidgetLayout;


/**
 * Utility class to change default behaviour of the deskmanager layouts.
 * 
 * @author Oliver May
 *
 * @since 1.0.0
 */
@Api
public final class GdmLayout {

	
	public static final String FILEDOWNLOAD_URL = "/fileDownload";

	public static final String FILEDOWNLOAD_ID = "id";
	
	public static final String MAPMAIN_ID = "mainMap";

	public static final String MAPOVERVIEW_ID = "overviewMap";

	public static final int EXCEPTIONCODE_GEODESKINACTIVE = 10000001;

	/**
	 * Attention! Update from rule in urlrewrite.xml when changing. 
	 * Must end with a /!
	 */
	public static final String GEODESK_PREFIX = "desk/";
	

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/**
	 * Z index for the role select.
	 */
	public static int roleSelectZindex = Integer.MAX_VALUE;

	/**
	 * Default loading logo.
	 */
	public static String loadingLogo = WidgetLayout.loadingScreenLogo;

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
	
	private GdmLayout() {}
}
