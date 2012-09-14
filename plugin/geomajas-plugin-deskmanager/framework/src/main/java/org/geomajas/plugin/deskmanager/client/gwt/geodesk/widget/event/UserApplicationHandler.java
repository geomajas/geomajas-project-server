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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.event;

import org.geomajas.annotation.FutureApi;

import com.google.gwt.event.shared.EventHandler;

/**
 * Throws onUserApplicationLoad(UserApplicationEvent) when the user application is loaded.
 * 
 * @author Oliver May
 * 
 */
@FutureApi
public interface UserApplicationHandler extends EventHandler {

	/**
	 * Called when mapWidget is set or changed.
	 * 
	 * @param event
	 */
	void onUserApplicationLoad(UserApplicationEvent event);
}
