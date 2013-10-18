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
package org.geomajas.graphics.client.object;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Graphics image.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GImage extends ResizableGraphicsObject {

	public GImage(int x, int y, int width, int height, String href, String text) {
		this(new Image(x, y, width, height, href), text);
	}

	public GImage(Image image, String text) {
		this(new ResizableImage(image), text);
	}

	public GImage(ResizableImage image) {
		super(image);
	}

	public GImage(ResizableImage image, String text) {
		super(image, text);
	}

	@Override
	public GraphicsObject cloneObject() {
		ResizableImage image = getImage();
		GImage clone = new GImage((ResizableImage) image.cloneObject());
		copyTo(clone);
		return clone;
	}

	public ResizableImage getImage() {
		return (ResizableImage) getResizable();
	}

	/**
	 * Resizable implementation for image.
	 */
	static class ResizableImage implements Resizable {

		private Image image;

		public ResizableImage(Image image) {
			this.image = image;
		}

		@Override
		public void setPosition(Coordinate position) {
			image.setUserX(position.getX());
			image.setUserY(position.getY());
		}

		@Override
		public Coordinate getPosition() {
			return new Coordinate(image.getUserX(), image.getUserY());
		}

		public Object cloneObject() {
			Image mask = new Image(image.getUserX(), image.getUserY(), image.getUserWidth(), image.getUserHeight(),
					image.getHref());
			return new ResizableImage(mask);
		}

		@Override
		public void flip(FlipState state) {
			// symmetric
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			image.setUserX(bounds.getX());
			image.setUserY(bounds.getY());
			image.setUserWidth(bounds.getWidth());
			image.setUserHeight(bounds.getHeight());
		}

		@Override
		public boolean isPreserveRatio() {
			return true;
		}

		@Override
		public boolean isAutoHeight() {
			return false;
		}

		@Override
		public Bbox getUserBounds() {
			return new Bbox(image.getUserX(), image.getUserY(), image.getUserWidth(), image.getUserHeight());
		}

		@Override
		public Bbox getBounds() {
			return new Bbox(image.getX(), image.getY(), image.getWidth(), image.getHeight());
		}

		@Override
		public VectorObject asObject() {
			return image;
		}

	}
}