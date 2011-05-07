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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point and main class for pure GWT example application.
 * 
 * @author Pieter De Graef
 */
public class Showcase implements EntryPoint {

	// private final GeomajasGinjector geomajasInjector = GWT.create(GeomajasGinjector.class);

	// private int mapWidth = 640;

	// private int mapHeight = 480;

	public void onModuleLoad() {
		ShowcaseLayout layout = new ShowcaseLayout();
		RootPanel.get().add(layout);
	}
}