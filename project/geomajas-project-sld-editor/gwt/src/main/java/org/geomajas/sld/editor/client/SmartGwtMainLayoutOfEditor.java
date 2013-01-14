/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.client;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;

/**
 * @author An Buyle
 *
 */
public final class SmartGwtMainLayoutOfEditor {

	private static Layout mainLayout;

	private SmartGwtMainLayoutOfEditor() {
		// Exists only to defeat instantiation.
	}

	public static Layout getLayout() {
		if (null == mainLayout) {
			mainLayout = new HLayout();

			mainLayout.setWidth("95%"); // when setting to 100% the panels resize annoyingly ('dancing pannels')
										// when the mouse pointer is moved   
			mainLayout.setHeight("95%");
		}
		return mainLayout;
	}
}
