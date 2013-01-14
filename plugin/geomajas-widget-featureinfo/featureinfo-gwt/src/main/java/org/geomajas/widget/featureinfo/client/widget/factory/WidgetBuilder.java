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
package org.geomajas.widget.featureinfo.client.widget.factory;

import java.io.Serializable;

import com.smartgwt.client.widgets.Canvas;

/**
 * Simple interface to for creating new generic widgets.
 * @author Kristof Heirwegh
 *
 */
public interface WidgetBuilder extends Serializable {
	// TODO might want to defer this to more specific usecases, eg. featuredetailwidgetbuilder
	/**
	 * Create an instance of the widget.
	 */
	Canvas createWidget();

	/**
	 * Called for all parameters added through configuration.
	 * @param key
	 * @param value
	 */
	void configure(String key, String value);
}
