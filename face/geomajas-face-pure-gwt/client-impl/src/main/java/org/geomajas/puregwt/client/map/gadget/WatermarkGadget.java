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

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.core.client.GWT;

/**
 * Geomajas logo that's automatically displayed in the bottom right corner of each map.
 * 
 * @author Pieter De Graef
 */
public class WatermarkGadget implements MapGadget {

	private ViewPort viewPort;

	private Rectangle background;

	private Image image;

	public void onDraw(ViewPort viewPort, VectorContainer container) {
		this.viewPort = viewPort;

		background = new Rectangle(viewPort.getMapWidth() - 125, viewPort.getMapHeight() - 12, 125, 12);
		background.setStrokeOpacity(0);
		background.setFillOpacity(0.65);
		container.add(background);

		image = new Image(viewPort.getMapWidth() - 125, viewPort.getMapHeight() - 12, 125, 12, GWT.getModuleBaseURL()
				+ "geomajas/images/mapgadget/powered_by_geomajas.gif");
		container.add(image);
	}

	public void onTranslate() {
	}

	public void onScale() {
	}

	public void onResize() {
		background.setX(viewPort.getMapWidth() - 125);
		background.setY(viewPort.getMapHeight() - 12);
		image.setX(viewPort.getMapWidth() - 125);
		image.setY(viewPort.getMapHeight() - 12);
	}

	public void onDestroy() {
	}
}