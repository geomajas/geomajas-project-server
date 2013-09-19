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
import org.geomajas.graphics.client.object.labeler.ResizableMultiLineLabeler;
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.shape.FixedScreenSizeRectangle;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Graphics rectangle.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GTextArea2 extends ResizableGraphicsObject implements Labeled, Fillable, Strokable {

	public GTextArea2(double userX, double userY, int defaultPixelWidth, int defaultPixelHeight, String text,
			BboxPosition screenUpperLeftPositionInUserSpace) {
		this(new FixedScreenSizeRectangle(userX, userY, defaultPixelWidth, defaultPixelHeight,
				screenUpperLeftPositionInUserSpace), text, screenUpperLeftPositionInUserSpace);
	}

	public GTextArea2(FixedScreenSizeRectangle rectangle, String text,
			BboxPosition screenUpperLeftPositionInUserSpace) {
		this(new ResizableRectangleWithMultiLineText(rectangle, text, screenUpperLeftPositionInUserSpace));
	}
	
	public GTextArea2(ResizableRectangleWithMultiLineText rectangle) {
		super(rectangle);
		getRectangle().setHeadClass(this);
		addRole(Fillable.TYPE, this);
		addRole(Strokable.TYPE, this);
		addRole(getRectangle().getBorderer());
		addRole(getRectangle().getLabeler());
	}

	@Override
	public GraphicsObject cloneObject() {
		ResizableRectangleWithMultiLineText rectangle = getRectangle();
		GTextArea2 clone = new GTextArea2((ResizableRectangleWithMultiLineText) rectangle.cloneObject());
		copyTo(clone);
		return clone;
	}

	public String getFillColor() {
		return getRectangle().getFillColor();
	}

	public void setFillColor(String color) {
		getRectangle().setFillColor(color);
	}

	public double getFillOpacity() {
		return getRectangle().getFillOpacity();
	}

	public void setFillOpacity(double opacity) {
		getRectangle().setFillOpacity(opacity);
	}

	public String getStrokeColor() {
		return getRectangle().getStrokeColor();
	}

	public void setStrokeColor(String color) {
		getRectangle().setStrokeColor(color);
	}

	public int getStrokeWidth() {
		return getRectangle().getStrokeWidth();
	}

	public void setStrokeWidth(int width) {
		getRectangle().setStrokeWidth(width);
	}

	public double getStrokeOpacity() {
		return getRectangle().getStrokeOpacity();
	}

	public void setStrokeOpacity(double opacity) {
		getRectangle().setStrokeOpacity(opacity);
	}

	private ResizableRectangleWithMultiLineText getRectangle() {
		return (ResizableRectangleWithMultiLineText) getResizable();
	}
	
	@Override
	public void setLabel(String label) {
		getRectangle().setText(label);
	}

	@Override
	public String getLabel() {
		return getRectangle().getLabel();
	}

	@Override
	public void setFontColor(String color) {
		getRectangle().setFontColor(color);
	}

	@Override
	public String getFontColor() {
		return getRectangle().getFontColor();
	}

	@Override
	public void setFontSize(int size) {
		getRectangle().setFontSize(size);
	}

	@Override
	public int getFontSize() {
		return getRectangle().getFontSize();
	}

	@Override
	public void setFontFamily(String font) {
		getRectangle().setFontFamily(font);
	}

	@Override
	public String getFontFamily() {
		return getRectangle().getFontFamily();
	}

	/**
	 * Resizable implementation for rectangle.
	 */
	static class ResizableRectangleWithMultiLineText implements Resizable, Fillable, Strokable, Labeled, Draggable {
		
		private BboxPosition userUlPosition;

		private FixedScreenSizeRectangle rectangle;
		
		private String text;
		
		// a rectangle arround the text.
		private ResizableBorderer borderer ;
		
		//multiline label
		private ResizableMultiLineLabeler labeler;
		
		private GTextArea2 headClass;

		public ResizableRectangleWithMultiLineText(FixedScreenSizeRectangle rectangle, String text) {
			this(rectangle, text, BboxPosition.CORNER_LL);
		}

		public ResizableRectangleWithMultiLineText(FixedScreenSizeRectangle rectangle, String text,
				BboxPosition screenUpperLeftPositionInUserSpace) {
			this.userUlPosition = screenUpperLeftPositionInUserSpace;

			// check for position of UL in userspace, change sign of height and width if necessary
			// standard: BboxPosition.CORNER_LL: width and height positive
			if (userUlPosition.equals(BboxPosition.CORNER_LR) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
				// switch width
				rectangle.setUserWidth(-rectangle.getUserWidth());
			}
			if (userUlPosition.equals(BboxPosition.CORNER_UL) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
				// switch height
				rectangle.setUserHeight(-rectangle.getUserHeight());
			}
			this.text = text;
			borderer = new ResizableBorderer();
			borderer.setFixedSize(true);
			labeler = new ResizableMultiLineLabeler(text, userUlPosition);
			rectangle.setFillOpacity(0);
			rectangle.setStrokeOpacity(0);
			this.rectangle = rectangle;
		}
		
		public ResizableBorderer getBorderer() {
			return borderer;
		}
		
		public ResizableMultiLineLabeler getLabeler() {
			return labeler;
		}

		@Override
		public void setPosition(Coordinate position) {
			rectangle.setUserX(position.getX());
			rectangle.setUserY(position.getY());
		}

		@Override
		public Coordinate getPosition() {
			return new Coordinate(rectangle.getUserX(), rectangle.getUserY());
		}

		public Object cloneObject() {
			FixedScreenSizeRectangle mask = new FixedScreenSizeRectangle(rectangle.getUserX(), rectangle.getUserY(),
					rectangle.getPixelWidth(), rectangle.getPixelHeight(), userUlPosition);
			return new ResizableRectangleWithMultiLineText(mask, text, userUlPosition);
		}

		@Override
		public void flip(FlipState state) {
			// symmetric
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			double x = bounds.getX();
			double y = bounds.getY();
			// Bbox always has positive height and width
			// change the given bounds, so the position matches userUlPosition
			if (userUlPosition.equals(BboxPosition.CORNER_LR) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
				// change x-axis values: x to other side
				x += bounds.getWidth();
			}
			if (userUlPosition.equals(BboxPosition.CORNER_UL) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
				// change y-axis values: y to other side
				y += bounds.getHeight();
			}
			rectangle.setUserX(x);
			rectangle.setUserY(y);
			update();
		}

		@Override
		public boolean isPreserveRatio() {
			return false;
		}

		//bbox in user lengths
		@Override
		public Bbox getUserBounds() {
			return rectangle.getUserBounds();
		}

		//bbox in pixel lengths
		@Override
		public Bbox getBounds() {
			return rectangle.getBounds();
		}

		@Override
		public VectorObject asObject() {
			return rectangle;
		}

		public String getFillColor() {
			return borderer.getFillColor();
		}

		public void setFillColor(String color) {
			borderer.setFillColor(color);
		}

		public double getFillOpacity() {
			return borderer.getFillOpacity();
		}

		public void setFillOpacity(double opacity) {
			borderer.setFillOpacity(opacity);
		}

		public String getStrokeColor() {
			return borderer.getStrokeColor();
		}

		public void setStrokeColor(String color) {
			borderer.setStrokeColor(color);
		}

		public int getStrokeWidth() {
			return rectangle.getStrokeWidth();
		}

		public void setStrokeWidth(int width) {
			borderer.setStrokeWidth(width);
		}

		public double getStrokeOpacity() {
			return borderer.getStrokeOpacity();
		}

		public void setStrokeOpacity(double opacity) {
			borderer.setStrokeOpacity(opacity);
		}
		
		public void setText(String text) {
			this.text = text;
		}
		
		public void update() {
			headClass.update();
		}

		
		public GTextArea2 getHeadClass() {
			return headClass;
		}

		
		public void setHeadClass(GTextArea2 headClass) {
			this.headClass = headClass;
		}

		@Override
		public void setLabel(String label) {
			labeler.setLabel(label);
		}

		@Override
		public String getLabel() {
			return labeler.getLabel();
		}

		@Override
		public void setFontColor(String color) {
			labeler.setFontColor(color);
		}

		@Override
		public String getFontColor() {
			return labeler.getFontColor();
		}

		@Override
		public void setFontSize(int size) {
			labeler.setFontSize(size);
		}

		@Override
		public int getFontSize() {
			return labeler.getFontSize();
		}

		@Override
		public void setFontFamily(String font) {
			labeler.setFontFamily(font);
		}

		@Override
		public String getFontFamily() {
			return labeler.getFontFamily();
		}

		@Override
		public void setLabelVisible(boolean visible) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public void setLabelVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	
}
