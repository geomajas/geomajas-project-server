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
package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

import com.google.gwt.user.client.Window;

/**
 * Window related utility methods.
 * 
 * @author Jan De Moerloose
 * @since 0.0.0
 */
@Api(allMethods = true)
public final class WindowUtil {

	private WindowUtil() {
		// do not allow instantiation.
	}

	/**
	 * Change the current window location to a new location.
	 * @param location new location
	 */
	public static void setLocation(String location) {
		// avoid messages from outstanding requests !
		GwtCommandDispatcher.getInstance().setShowError(false);
		Window.Location.assign(location);
	}
}
