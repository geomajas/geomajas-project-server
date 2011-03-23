/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client;

/**
 * General utility class for the Geomajas Pure GWT face.
 * 
 * @author Pieter De Graef
 */
public final class Geomajas {

	private Geomajas() {
		// Utility class: Hide constructor.
	}

	/**
	 * Is the user currently running Internet Explorer?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isIE() {
		return getUserAgent().contains("microsoft");
	}

	/**
	 * Is the user currently using FireFox?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isFireFox() {
		return getUserAgent().contains("firefox");
	}

	/**
	 * Is the user currently using Chrome?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isChrome() {
		return getUserAgent().contains("chrome");
	}

	/**
	 * Is the user currently using Safari?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isSafari() {
		return getUserAgent().contains("safari");
	}

	/**
	 * Is the user currently using a Webkit based browser (such as Chrome or Safari)?
	 * 
	 * @return true or false - yes or no.
	 */
	public static boolean isWebkit() {
		return getUserAgent().contains("webkit");
	}

	private static native String getUserAgent()
	/*-{
	 return navigator.userAgent.toLowerCase();
	}-*/;

}