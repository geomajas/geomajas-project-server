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

package org.geomajas.plugin.staticsecurity.gwt.example.client;

/**
 * Client side security context.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start ClientSecurityContext, Example of a client security context
public final class ClientSecurityContext {

	private static boolean blablaButtonAllowed;

	private ClientSecurityContext() {
		// utility class, hide constructor
	}

	/**
	 * Is it allowed to push the "blabla" button?
	 *
	 * @return true when allowed
	 */
	public static boolean isBlablaButtonAllowed() {
		return blablaButtonAllowed;
	}

	/**
	 * Set whether using the "blabla" button is allowed.
	 *
	 * @param blablaButtonAllowed allowed?
	 */
	public static void setBlablaButtonAllowed(boolean blablaButtonAllowed) {
		ClientSecurityContext.blablaButtonAllowed = blablaButtonAllowed;
	}
}
// @extract-end
