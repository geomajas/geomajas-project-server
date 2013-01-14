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
 * 
 * @author Oliver May
 *
 * @since 1.0.0
 */
@Api
public final class CommonLayout {

	
	public static final String FILEDOWNLOAD_URL = "/fileDownload";

	public static final String FILEDOWNLOAD_ID = "id";
	
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

	public static String loadingLogo = WidgetLayout.loadingScreenLogo;

	// CHECKSTYLE VISIBILITY MODIFIER: ON
	
	private CommonLayout() {}
}
