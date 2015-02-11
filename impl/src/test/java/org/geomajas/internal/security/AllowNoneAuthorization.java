/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.security;

import org.geomajas.security.BaseAuthorization;

/**
 * Authorization class which allows nothing.
 *
 * @author Joachim Van der Auwera
 */
public class AllowNoneAuthorization implements BaseAuthorization {

	public String getId() {
		return "AllowNone";
	}

	public boolean isToolAuthorized(String toolId) {
		return false;
	}

	public boolean isCommandAuthorized(String commandName) {
		return false;
	}

	public boolean isLayerVisible(String layerId) {
		return false;
	}

	public boolean isLayerUpdateAuthorized(String layerId) {
		return false;
	}

	public boolean isLayerCreateAuthorized(String layerId) {
		return false;
	}

	public boolean isLayerDeleteAuthorized(String layerId) {
		return false;
	}
}