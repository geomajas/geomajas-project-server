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
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.geometry.service.GeometryService;

import com.google.gwt.canvas.client.Canvas;

/**
 * Canvas path element based on {@link Geometry}. Implemented for polygons only.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasPath implements CanvasShape {

	private Geometry geometry;

	private Bbox bounds;

	private String strokeStyle;

	private String fillStyle;

	private double strokeWidthPixels;

	public CanvasPath(Geometry geometry) {
		this.geometry = geometry;
		bounds = GeometryService.getBounds(geometry);
	}

	@Override
	public void paint(Canvas canvas, Matrix matrix) {
		if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
			canvas.getContext2d().save();
			canvas.getContext2d().setFillStyle(fillStyle);
			canvas.getContext2d().setStrokeStyle(strokeStyle);
			canvas.getContext2d().setLineWidth(strokeWidthPixels / matrix.getXx());
			canvas.getContext2d().beginPath();
			for (Geometry ring : geometry.getGeometries()) {
				Coordinate[] coords = ring.getCoordinates();
				canvas.getContext2d().moveTo(coords[0].getX(), coords[0].getY());
				for (int i = 1; i < coords.length - 1; i++) {
					canvas.getContext2d().lineTo(coords[i].getX(), coords[i].getY());
				}
			}
			canvas.getContext2d().closePath();
			canvas.getContext2d().fill();
			canvas.getContext2d().stroke();
			canvas.getContext2d().restore();
		}
	}

	@Override
	public Bbox getBounds() {
		return bounds;
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
