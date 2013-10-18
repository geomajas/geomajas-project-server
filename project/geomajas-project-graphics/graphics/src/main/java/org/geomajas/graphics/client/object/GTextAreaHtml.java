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
import org.geomajas.graphics.client.object.role.HtmlRenderable;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.object.role.Textable;
import org.geomajas.graphics.client.shape.FixedScreenSizeRectangle;
import org.geomajas.graphics.client.util.FlipState;
import org.geomajas.graphics.client.widget.TextAreaWidget;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.user.client.ui.Widget;

/**
 * Graphics rectangle.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GTextAreaHtml extends ResizableGraphicsObject implements Textable, Fillable, Strokable {

	public GTextAreaHtml(double userX, double userY, int width, int height, String text) {
		this(new FixedScreenSizeRectangle(userX, userY, width, height, 0.5, 0.5), text);
	}

	public GTextAreaHtml(FixedScreenSizeRectangle rectangle, String text) {
		this(new ResizableTextArea(rectangle, text));
		// don't show the svg part !!!
		rectangle.setFillOpacity(0);
		rectangle.setStrokeOpacity(0);
	}

	public GTextAreaHtml(ResizableTextArea textArea) {
		super(textArea);
		addRole(Fillable.TYPE, this);
		addRole(Strokable.TYPE, this);
		addRole(Textable.TYPE, this);
		addRole(HtmlRenderable.TYPE, getRectangle());
	}

	@Override
	public GraphicsObject cloneObject() {
		ResizableTextArea rectangle = getRectangle();
		GTextAreaHtml clone = new GTextAreaHtml((ResizableTextArea) rectangle.cloneObject());
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

	private ResizableTextArea getRectangle() {
		return (ResizableTextArea) getResizable();
	}

	@Override
	public void setLabel(String label) {
		getRectangle().setLabel(label);
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
	static class ResizableTextArea implements Resizable, Fillable, Strokable, Textable, Draggable, HtmlRenderable {

		private FixedScreenSizeRectangle rectangle;

		private String text;
		
		private TextAreaWidget textDiv;

		public ResizableTextArea(FixedScreenSizeRectangle rectangle, String text) {
			this.rectangle = rectangle;
			this.text = text;
			this.textDiv = new TextAreaWidget();
			updateTextDiv();
		}

		@Override
		public Widget asWidget() {
			return textDiv.asWidget();
		}

		@Override
		public void setPosition(Coordinate graphicsContainerPosition) {
			rectangle.setUserX(graphicsContainerPosition.getX());
			rectangle.setUserY(graphicsContainerPosition.getY());
			updateTextDiv();
		}

		@Override
		public Coordinate getPosition() {
			return new Coordinate(rectangle.getUserX(), rectangle.getUserY());
		}

		public Object cloneObject() {
			FixedScreenSizeRectangle copy = new FixedScreenSizeRectangle(rectangle.getUserX(), rectangle.getUserY(),
					rectangle.getWidth(), rectangle.getHeight(), rectangle.getAnchorX(), rectangle.getAnchorY());
			return new ResizableTextArea(copy, text);
		}

		@Override
		public void flip(FlipState state) {
			// symmetric
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			rectangle.setUserBounds(bounds);
			updateTextDiv();
		}

		private void updateTextDiv() {
			textDiv.setScreenBounds(getBounds());
			textDiv.setLabel(text);
		}

		@Override
		public boolean isPreserveRatio() {
			return false;
		}

		@Override
		public boolean isAutoHeight() {
			return true;
		}

		// bbox in user lengths
		@Override
		public Bbox getUserBounds() {
			return rectangle.getUserBounds();
		}

		// bbox in pixel lengths
		@Override
		public Bbox getBounds() {
			return rectangle.getBounds();
		}

		@Override
		public VectorObject asObject() {
			return rectangle;
		}

		public String getFillColor() {
			return textDiv.getFillColor();
		}

		public void setFillColor(String color) {
			textDiv.setFillColor(color);
		}

		public double getFillOpacity() {
			return textDiv.getFillOpacity();
		}

		public void setFillOpacity(double opacity) {
			textDiv.setFillOpacity(opacity);
		}

		public String getStrokeColor() {
			return textDiv.getStrokeColor();
		}

		public void setStrokeColor(String color) {
			textDiv.setStrokeColor(color);
		}

		public int getStrokeWidth() {
			return textDiv.getStrokeWidth();
		}

		public void setStrokeWidth(int width) {
			textDiv.setStrokeWidth(width);
		}

		public double getStrokeOpacity() {
			return textDiv.getStrokeOpacity();
		}

		public void setStrokeOpacity(double opacity) {
			textDiv.setStrokeOpacity(opacity);
		}

		@Override
		public void setLabel(String text) {
			this.text = text;
			updateTextDiv();
		}

		@Override
		public String getLabel() {
			return text;
		}

		@Override
		public void setFontColor(String fontColor) {
			textDiv.setFontColor(fontColor);
		}

		@Override
		public String getFontColor() {
			return textDiv.getFontColor();
		}

		@Override
		public void setFontSize(int fontSize) {
			textDiv.setFontSize(fontSize);
		}

		@Override
		public int getFontSize() {
			return textDiv.getFontSize();
		}

		@Override
		public void setFontFamily(String fontFamily) {
			textDiv.setFontFamily(fontFamily);
		}

		@Override
		public String getFontFamily() {
			return textDiv.getFontFamily();
		}

	}

}
