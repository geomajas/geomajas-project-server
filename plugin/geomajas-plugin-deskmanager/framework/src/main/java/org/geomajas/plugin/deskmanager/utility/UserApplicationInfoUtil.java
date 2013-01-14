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
package org.geomajas.plugin.deskmanager.utility;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.GeodeskLayout;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;


/**
 * Utility class to get maps from user application configuration.
 * 
 * @author Oliver May
 *
 */
public final class UserApplicationInfoUtil {

	private UserApplicationInfoUtil() {}
	
	/**
	 * Retrieve the main mapinfo from the user application, return null if it's not found.
	 * 
	 * @param applicationInfo
	 * @return the main mapinfo.
	 */
	public static ClientMapInfo getMainMapInfo(UserApplicationInfo applicationInfo) {
		for (ClientMapInfo map : applicationInfo.getApplicationInfo().getMaps()) {
			if (GeodeskLayout.MAPMAIN_ID.equals(map.getId())) {
				return map;
			}
		}
		return null;
	}
	
	/**
	 * Retrieve the overview mapinfo from the user application, return null if it's not found.
	 * 
	 * @param applicationInfo the application info
	 * @return the overview mapinfo.
	 */
	public static ClientMapInfo getOverviewMapInfo(UserApplicationInfo applicationInfo) {
		for (ClientMapInfo map : applicationInfo.getApplicationInfo().getMaps()) {
			if (GeodeskLayout.MAPOVERVIEW_ID.equals(map.getId())) {
				return map;
			}
		}
		return null;
	}
}
