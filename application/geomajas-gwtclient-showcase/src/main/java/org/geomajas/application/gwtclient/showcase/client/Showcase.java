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
package org.geomajas.application.gwtclient.showcase.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.geomajas.puregwt.example.base.client.ExampleBase;

/**
 * Entry point of Geomajas GWT showcase application.
 *
 * @author Dosi Bingov
 */
public class Showcase implements EntryPoint {

	public void onModuleLoad() {
		RootLayoutPanel.get().add(ExampleBase.getLayout());
	}
}
