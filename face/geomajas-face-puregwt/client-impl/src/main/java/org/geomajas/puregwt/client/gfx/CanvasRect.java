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
package org.geomajas.puregwt.client.gfx;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Matrix;

import com.google.gwt.canvas.client.Canvas;

/**
 * Draws a rectangle in canvas world space.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasRect implements CanvasShape {

	private Bbox box;
	
	private String strokeStyle;

	private String fillStyle;

	private double strokeWidthPixels;

	/**
	 * Constructs a rectangle with these bounds.
	 * 
	 * @param box the bounds
	 */
	public CanvasRect(Bbox box) {
		this.box = new Bbox(box.getX(), box.getY(), box.getWidth(), box.getHeight());
	}

	@Override
	public void paint(Canvas canvas, Matrix matrix) {
		canvas.getContext2d().save();
		canvas.getContext2d().setFillStyle(fillStyle);
		canvas.getContext2d().fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
		canvas.getContext2d().setStrokeStyle(strokeStyle);
		canvas.getContext2d().setLineWidth(strokeWidthPixels / matrix.getXx());
		canvas.getContext2d().strokeRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
		canvas.getContext2d().restore();
	}
	
	@Override
	public Bbox getBounds() {
		return box;
	}

	public void setStrokeStyle(String strokeStyle) {
		this.strokeStyle = strokeStyle;
	}

	public void setFillStyle(String fillStyle) {
		this.fillStyle = fillStyle;
	}

	public void setStrokeWidthPixels(double strokeWidthPixels) {
		this.strokeWidthPixels = strokeWidthPixels;
	}

}
