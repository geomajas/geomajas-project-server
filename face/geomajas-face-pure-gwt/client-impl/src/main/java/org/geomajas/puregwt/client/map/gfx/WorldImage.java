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
package org.geomajas.puregwt.client.map.gfx;

import org.geomajas.geometry.Coordinate;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * A fixed scale image.
 * 
 * @author Jan De Moerloose
 * 
 */
public class WorldImage implements WorldObject {

	private Image image;

	private Coordinate worldLocation;

	private int anchorX;

	private int anchorY;

	public WorldImage(double worldX, double worldY, int anchorX, int anchorY, int width, int height, String href) {
		image = new Image(0, 0, width, height, href);
		worldLocation = new Coordinate(worldX, worldY);
		this.anchorX = anchorX;
		this.anchorY = anchorY;
	}

	public VectorObject getVectorObject() {
		return image;
	}

	public void scaleToScreen(double scaleX, double scaleY) {

		image.setX((int) (worldLocation.getX() * scaleX) - anchorX);
		image.setY((int) (worldLocation.getY() * scaleY) - anchorY);
	}

	public Coordinate getWorldLocation() {
		return worldLocation;
	}

	public Image getImage() {
		return image;
	}

}
