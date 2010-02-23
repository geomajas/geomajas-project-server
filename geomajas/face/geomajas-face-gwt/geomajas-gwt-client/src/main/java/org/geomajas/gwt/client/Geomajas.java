/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Global settings for the Geomajas GWT face.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class Geomajas {

	private Geomajas() {
	}

	/** Returns the current version of Geomajas as a string. */
	public static String getVersion() {
		return "1.5.4";
	}

	/**
	 * Returns a list of locales that can be used in this version of Geomajas. The default is english, and 'native'
	 * means that your browsers locale should be used (if supported - default otherwise).
	 */
	public static Map<String, String> getSupportedLocales() {
		Map<String, String> locales = new HashMap<String, String>();
		locales.put("default", "English");
		locales.put("nl", "Nederlands");
		return locales;
	}

	/**
	 * Return the base directory for the web application.
	 * 
	 * @return
	 */
	public static native String getIsomorphicDir()
	/*-{
		return $wnd.isomorphicDir;
	}-*/;
}