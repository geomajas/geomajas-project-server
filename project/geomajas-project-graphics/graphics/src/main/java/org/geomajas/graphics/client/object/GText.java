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
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.object.role.Textable;
import org.geomajas.graphics.client.shape.AnchoredText;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Graphics text.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GText extends ResizableGraphicsObject implements Textable, Fillable, Strokable {

	public GText(double userX, double userY, String text) {
		this(new AnchoredText(userX, userY, text, 0.5, 0.5));
	}

	public GText(AnchoredText anchoredText) {
		this(new DraggableText(anchoredText));
	}

	public GText(DraggableText text) {
		super(text);
		addRole(Fillable.TYPE, this);
		addRole(Strokable.TYPE, this);
		addRole(Textable.TYPE, this);
		removeRole(Resizable.TYPE);
		addRole(getText().getResizableBorderer());
	}
	
	@Override
	public GraphicsObject cloneObject() {
		GText clone = new GText((DraggableText) getText().cloneObject());
		copyTo(clone);
		return clone;
	}

	@Override
	public void setLabel(String label) {
		getText().setLabel(label);
		update();
	}

	@Override
	public String getLabel() {
		return getText().getLabel();
	}

	public String getFillColor() {
		return getText().getFillColor();
	}

	public void setFillColor(String color) {
		getText().setFillColor(color);
	}

	public double getFillOpacity() {
		return getText().getFillOpacity();
	}

	public void setFillOpacity(double opacity) {
		getText().setFillOpacity(opacity);
	}

	public String getStrokeColor() {
		return getText().getStrokeColor();
	}

	public void setStrokeColor(String color) {
		getText().setStrokeColor(color);
	}

	public int getStrokeWidth() {
		return getText().getStrokeWidth();
	}

	public void setStrokeWidth(int width) {
		getText().setStrokeWidth(width);
		update();
	}

	public double getStrokeOpacity() {
		return getText().getStrokeOpacity();
	}

	public void setStrokeOpacity(double opacity) {
		getText().setStrokeOpacity(opacity);
	}

	private DraggableText getText() {
		return (DraggableText) getResizable();
	}
	
	@Override
	public void setFontSize(int size) {
		getText().setFontSize(size);		
	}

	@Override
	public int getFontSize() {
		return getText().getFontSize();
	}

	@Override
	public void setFontFamily(String font) {
		getText().setFontFamily(font);
	}

	@Override
	public String getFontFamily() {
		return getText().getFontFamily();
	}
	
	@Override
	public void setFontColor(String color) {
		getText().setFontColor(color);
	}

	@Override
	public String getFontColor() {
		return getText().getFontColor();
	}

	/**
	 *
	 */
	static class DraggableText implements Draggable, Strokable, Fillable, Textable, Resizable {

		// centered around userX, userY
		private AnchoredText text;
		
		// a rectangle arround the text.
		private ResizableBorderer borderer ;
		
		public AnchoredText getAnchor() {
			return text;
		}

		public DraggableText(AnchoredText text) {
			this(text, true);
			
		}
		
		public DraggableText(AnchoredText text, boolean setDefaultFontStyle) {
			this.text = text;
			borderer = new ResizableBorderer();
			borderer.setFixedSize(true);
			if (setDefaultFontStyle) {
				text.setStrokeWidth(0);
				text.setStrokeColor(text.getFillColor());
				text.setStrokeOpacity(text.getFillOpacity());
				text.setFillColor("black");
				text.setFillOpacity(1.0);
			}
		}

		@Override
		public VectorObject asObject() {
			return text;
		}

		@Override
		public void setPosition(Coordinate position) {
			text.setUserX(position.getX());
			text.setUserY(position.getY());
		}

		@Override
		public Coordinate getPosition() {
			double userX = text.getUserX();
			double userY = text.getUserY();
			return new Coordinate(userX, userY);
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
			return borderer.getStrokeWidth();
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

		public Object cloneObject() {
			AnchoredText clone = new AnchoredText(text.getUserX(), text.getUserY(), text.getText(), text.getAnchorX(),
					text.getAnchorY());
			clone.setStrokeWidth(text.getStrokeWidth());
			clone.setFillColor(text.getFillColor()); // this is font color
			clone.setFontFamily(text.getFontFamily());
			clone.setFontSize(text.getFontSize());
			return new DraggableText(clone, false);
		}

		@Override
		public void setLabel(String label) {
			text.setText(label);
		}

		@Override
		public String getLabel() {
			return text.getText();
		}

		public double getUserX() {
			return text.getUserX();
		}

		public double getUserY() {
			return text.getUserY();
		}

		@Override
		public void flip(FlipState state) {
			// no-op
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			// no-op
		}

		@Override
		public Bbox getUserBounds() {
			double userX = text.getUserX() - text.getAnchorX() * text.getUserWidth();
			double userY = text.getUserY() - text.getAnchorY() * text.getUserHeight();
			double userWidth = text.getUserWidth();
			double userHeight = text.getUserHeight();
			Bbox box = new Bbox(userX, userY, userWidth, userHeight);
			return box;
		}

		@Override
		public Bbox getBounds() {
			// y is lower-left !!!
			return new Bbox(text.getX(), text.getY() - text.getTextHeight(), text.getTextWidth(), text.getTextHeight());
		}

		@Override
		public boolean isPreserveRatio() {
			return false;
		}

		@Override
		public boolean isAutoHeight() {
			return false;
		}
		
		@Override
		public void setFontColor(String color) {
			text.setFillColor(color);
			text.setStrokeColor(color);
		}

		@Override
		public String getFontColor() {
			return text.getFillColor();
		}

		@Override
		public void setFontSize(int size) {
			text.setFontSize(size);
		}

		@Override
		public int getFontSize() {
			return text.getFontSize();
		}

		@Override
		public void setFontFamily(String font) {
			text.setFontFamily(font);
		}

		@Override
		public String getFontFamily() {
			return text.getFontFamily();
		}

		public ResizableBorderer getResizableBorderer() {
			return borderer;
		}

	}

	
}
