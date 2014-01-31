/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.security;

import org.geomajas.security.BaseAuthorization;

/**
 * Simple authorization class, allows everything, without handling areas.
 *
 * @author Joachim Van der Auwera
 */
public class AllowBaseAuthorization implements BaseAuthorization {

	public String getId() {
		return "AllowBase";
	}

	public boolean isToolAuthorized(String toolId) {
		return true;
	}

	public boolean isCommandAuthorized(String commandName) {
		return true;
	}

	public boolean isLayerVisible(String layerId) {
		return true;
	}

	public boolean isLayerUpdateAuthorized(String layerId) {
		return true;
	}

	public boolean isLayerCreateAuthorized(String layerId) {
		return true;
	}

	public boolean isLayerDeleteAuthorized(String layerId) {
		return true;
	}

}
