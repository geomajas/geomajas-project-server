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

package org.geomajas.puregwt.client.widget;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;

/**
 * Geomajas logo that's automatically displayed in the bottom right corner of each map. This widget is meant to be added
 * to the map's widget pane (see {@link MapPresenter#getWidgetPane()}).
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class Watermark extends AbstractMapWidget {

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param mapPresenter
	 *            The map presenter.
	 */
	public Watermark(MapPresenter mapPresenter) {
		super(mapPresenter);
		Image image = new Image(GWT.getModuleBaseURL() + "geomajas/images/mapgadget/powered_by_geomajas.gif");
		image.getElement().getStyle().setBackgroundColor("#FFFFFF");
		image.getElement().getStyle().setPosition(Position.ABSOLUTE);
		image.getElement().getStyle().setBottom(0, Unit.PX);
		image.getElement().getStyle().setRight(0, Unit.PX);
		image.setSize("125px", "12px");
		initWidget(image);
	}
}