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
import org.geomajas.graphics.client.shape.AnchoredImage;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Graphics icon (fixed size image).
 * 
 * @author Jan De Moerloose
 * 
 */
public class GIcon extends ResizableGraphicsObject {

	private DraggableImage dragImage;
	
	public GIcon(double userX, double userY, int width, int height, String href) {
		this(new AnchoredImage(userX, userY, width, height, href, 0.5, 0.5));
	}

	public GIcon(AnchoredImage anchoredImage) {
		this(new DraggableImage(anchoredImage));
	}
	
	public GIcon(DraggableImage draggableImage) {
		super(draggableImage);
		this.dragImage = draggableImage;
		removeRole(Resizable.TYPE);
	}

	@Override
	public GraphicsObject cloneObject() {
		GIcon clone = new GIcon((DraggableImage) dragImage.cloneObject());
		copyTo(clone);
		return clone;
	}

	@Override
	public void setOpacity(double opacity) {
		dragImage.setOpacity(opacity);
	}

	/**
	 *
	 */
	static class DraggableImage implements Draggable, Resizable {

		private AnchoredImage anchoredImage;

		public DraggableImage(AnchoredImage anchoredImage) {
			this.anchoredImage = anchoredImage;
		}

		@Override
		public VectorObject asObject() {
			return anchoredImage;
		}

		@Override
		public void setPosition(Coordinate imageAnchorPosition) {
			anchoredImage.setUserX(imageAnchorPosition.getX());
			anchoredImage.setUserY(imageAnchorPosition.getY());
		}

		@Override
		public Coordinate getPosition() {
			return new Coordinate(anchoredImage.getUserX(), anchoredImage.getUserY());
		}

		public AnchoredImage createCopy() {
			AnchoredImage mask = new AnchoredImage(anchoredImage.getUserX(), anchoredImage.getUserY(),
					anchoredImage.getWidth(), anchoredImage.getHeight(), anchoredImage.getHref(),
					anchoredImage.getAnchorX(), anchoredImage.getAnchorY());
			return mask;
		}

		public void setOpacity(double opacity) {
			try {
				anchoredImage.getElement().getStyle().setOpacity(opacity);
			} catch (Exception e) {
				// do nothing
			}
		}

		@Override
		public Bbox getUserBounds() {
			return anchoredImage.getUserBounds();
		}

		@Override
		public void flip(FlipState state) {
			// do nothing
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			// can't do this
		}

		@Override
		public Bbox getBounds() {
			return anchoredImage.getBounds();
		}

		@Override
		public boolean isPreserveRatio() {
			return true;
		}

		@Override
		public boolean isAutoHeight() {
			return false;
		}
		
		public Object cloneObject() {
			return new DraggableImage(createCopy());
		}

	}

}
