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

package org.geomajas.plugin.geocoder.puregwt.example.client;

import org.geomajas.gwt.example.base.client.ExampleBase;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point and main class for core widget example application.
 * @author Pieter De Graef
 */
public class GeocoderExample implements EntryPoint {

	public void onModuleLoad() {
		RootLayoutPanel.get().add(ExampleBase.getLayout());
	}
}