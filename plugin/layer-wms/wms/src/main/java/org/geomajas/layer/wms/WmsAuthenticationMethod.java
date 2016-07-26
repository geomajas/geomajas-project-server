/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.wms;

/**
 * Different possible authentication methods for WMS requests.
 *
 * @author Joachim Van der Auwera
 * @deprecated use {@link LayerAuthenticationMethod}
 */
@Deprecated
public enum WmsAuthenticationMethod {
	/**
	 * HTTP BASIC authentication is used.
	 */
	BASIC,

	/**
	 * Username and password are added in the URL.
	 */
	URL
}
