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
package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import org.geomajas.plugin.deskmanager.client.gwt.common.CommonLayout;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.util.CodeServer;

import com.google.gwt.core.client.GWT;


/**
 * Helper class that provides url's for the geodesk previews.
 * 
 * @author Oliver May
 *
 */
public final class GeodeskUrlUtil {
	
	private GeodeskUrlUtil() { }
	
	public static String createUrl(String baseUrl, String loketId) {
		return baseUrl + CommonLayout.GEODESK_PREFIX + loketId + "/" + CodeServer.getCodeServer();
	}

	public static String createPreviewUrl(String loketId) {
		return createUrl(GWT.getModuleBaseURL() + "../", loketId);
	}

}
