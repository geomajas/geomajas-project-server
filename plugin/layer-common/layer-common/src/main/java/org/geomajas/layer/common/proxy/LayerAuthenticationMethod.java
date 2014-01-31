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

package org.geomajas.layer.common.proxy;

/**
 * Different possible authentication methods for Layer requests.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
public enum LayerAuthenticationMethod {
	/**
	 * HTTP BASIC authentication is used.
	 */
	BASIC,
	
	/**
	 * HTTP DIGEST authentication is used.
	 */
	DIGEST,

	/**
	 * Username and password are added in the URL.
	 */
	URL
}
