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
package org.geomajas.plugin.deskmanager.client.gwt.common.util;

import org.geomajas.plugin.deskmanager.client.gwt.common.CommonLayout;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.util.CodeServer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;


/**
 * Helper class that provides url's for the geodesk previews.
 * 
 * @author Oliver May
 *
 */
public final class GeodeskUrlUtil {
	
	private GeodeskUrlUtil() { }
	
	public static String createUrl(String baseUrl, String geodeskId) {
		return baseUrl + CommonLayout.GEODESK_PREFIX + geodeskId + "/" + CodeServer.getCodeServer();
	}

	public static String createPreviewUrl(String geodeskId) {
		return createUrl(GWT.getModuleBaseURL() + "../", geodeskId);
	}
	
	public static String getGeodeskId() {

		String geodeskId = Window.Location.getHref();
		if (!geodeskId.contains(CommonLayout.GEODESK_PREFIX)) {
			return null;
		}
		geodeskId = geodeskId.substring(geodeskId.indexOf(CommonLayout.GEODESK_PREFIX) 
				+ CommonLayout.GEODESK_PREFIX.length()); 
		geodeskId = geodeskId.substring(0, geodeskId.indexOf('/'));
		
		return geodeskId;
		
	}

}
