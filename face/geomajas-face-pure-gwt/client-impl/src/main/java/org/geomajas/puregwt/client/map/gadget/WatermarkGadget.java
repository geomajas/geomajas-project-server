/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.gadget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Geomajas logo that's automatically displayed in the bottom right corner of each map.
 * 
 * @author Pieter De Graef
 */
public class WatermarkGadget extends AbstractMapGadget {

	private Image image;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public WatermarkGadget() {
		setHorizontalAlignment(Alignment.END);
		setVerticalAlignment(Alignment.END);
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public Widget asWidget() {
		if (image == null) {
			image = new Image(GWT.getModuleBaseURL() + "geomajas/images/mapgadget/powered_by_geomajas.gif");
			image.getElement().getStyle().setBackgroundColor("#FFFFFF");
			image.getElement().getStyle().setPosition(Position.ABSOLUTE);
			image.setSize("125px", "12px");
		}
		return image;
	}
}