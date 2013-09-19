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
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.shape.FixedScreenSizeRectangle;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.dom.client.Style;

/**
 * Graphics rectangle.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GTextAreaHtml extends ResizableGraphicsObject implements Labeled, Fillable, Strokable {

	public GTextAreaHtml(double userX, double userY, int defaultPixelWidth, int defaultPixelHeight, String text,
			BboxPosition screenUpperLeftPositionInUserSpace) {
		this(new FixedScreenSizeRectangle(userX, userY, defaultPixelWidth, defaultPixelHeight,
				screenUpperLeftPositionInUserSpace), text, screenUpperLeftPositionInUserSpace);
	}

	public GTextAreaHtml(FixedScreenSizeRectangle rectangle, String text,
			BboxPosition screenUpperLeftPositionInUserSpace) {
		this(new TextPopup(rectangle, text, screenUpperLeftPositionInUserSpace));
	}
		
	public GTextAreaHtml(TextPopup rectangle) {
		super(rectangle);
		getRectangle().setHeadClass(this);
		addRole(Fillable.TYPE, this);
		addRole(Strokable.TYPE, this);
	}

	@Override
	public GraphicsObject cloneObject() {
		TextPopup rectangle = getRectangle();
		GTextAreaHtml clone = new GTextAreaHtml((TextPopup) rectangle.cloneObject());
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

	private TextPopup getRectangle() {
		return (TextPopup) getResizable();
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
	static class TextPopup implements Resizable, Fillable, Strokable, Labeled, Draggable {
		
		private DraggableDecoratedPopupPanel panel;
		
		private BboxPosition userUlPosition;

		private FixedScreenSizeRectangle rectangle;
		
		private String text;
		
		private GTextAreaHtml headClass;

		public TextPopup(FixedScreenSizeRectangle rectangle, String text) {
			this(rectangle, text, BboxPosition.CORNER_LL);
		}

		public TextPopup(FixedScreenSizeRectangle rectangle, String text,
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
			this.rectangle = rectangle;
			rectangle.setFillColor("green");
			panel = new DraggableDecoratedPopupPanel(text);
			panel.setWidth(rectangle.getWidth() + "px");
			panel.setHeight(rectangle.getHeight() + "px");
			panel.show();
			panel.setPosition(new Coordinate(rectangle.getUserX(), rectangle.getUserY()));
			//panel.showRelativeTo(target)
			setFillColor("red");
		}

		@Override
		public void setPosition(Coordinate graphicsContainerPosition) {
			rectangle.setUserX(graphicsContainerPosition.getX());
			rectangle.setUserY(graphicsContainerPosition.getY());
			
//			panel.setPopupPosition((int)position.getX(), (int)position.getY());
		}

		@Override
		public Coordinate getPosition() {
			return new Coordinate(rectangle.getUserX(), rectangle.getUserY());
		}

		public Object cloneObject() {
			FixedScreenSizeRectangle mask = new FixedScreenSizeRectangle(rectangle.getUserX(), rectangle.getUserY(),
					rectangle.getPixelWidth(), rectangle.getPixelHeight(), userUlPosition);
			return new TextPopup(mask, text, userUlPosition);
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
			return panel.getElement().getStyle().getBackgroundColor();
		}

		public void setFillColor(String color) {
			panel.getElement().getStyle().setBackgroundColor(color);
		}

		public double getFillOpacity() {
			return 1.0;
		}

		public void setFillOpacity(double opacity) {
			// do nothing
		}

		public String getStrokeColor() {
			return panel.getElement().getStyle().getBorderColor();
		}

		public void setStrokeColor(String color) {
			panel.getElement().getStyle().setBorderColor(color);
		}

		public int getStrokeWidth() {
			return Integer.parseInt(panel.getElement().getStyle().getBorderWidth());
		}

		public void setStrokeWidth(int width) {
			panel.getElement().getStyle().setBorderWidth(width, Style.Unit.PX);
		}

		public double getStrokeOpacity() {
			return 1.0;
		}

		public void setStrokeOpacity(double opacity) {
			// do nothing
		}
		
		public void setText(String text) {
			this.text = text;
		}
		
		public void update() {
			headClass.update();
		}
		
		public void setHeadClass(GTextAreaHtml headClass) {
			this.headClass = headClass;
		}

		@Override
		public void setLabel(String text) {
			panel.setLabel(text);
		}

		@Override
		public String getLabel() {
			return panel.getLabel();
		}

		@Override
		public void setFontColor(String color) {
			panel.setFontColor(color);
		}

		@Override
		public String getFontColor() {
			return panel.getFontColor();
		}

		@Override
		public void setFontSize(int size) {
			panel.setFontSize(size);
		}

		@Override
		public int getFontSize() {
			return panel.getFontSize();
		}

		@Override
		public void setFontFamily(String font) {
			panel.setFontFamily(font);
		}

		@Override
		public String getFontFamily() {
			return panel.getFontFamily();
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

//	public class GraphicsContainerToHtmlPositionConverter {
//		
//	}
}
