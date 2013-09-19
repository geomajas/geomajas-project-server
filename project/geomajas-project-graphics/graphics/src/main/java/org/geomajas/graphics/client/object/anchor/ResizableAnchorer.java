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
package org.geomajas.graphics.client.object.anchor;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.object.Cloneable;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableAwareRole;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.shape.AnchoredRectangle;
import org.geomajas.graphics.client.shape.CoordinatePath;
import org.geomajas.graphics.client.shape.MarkerShape;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Implementation of {@link Anchored} role for {@link Resizable} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizableAnchorer implements Anchored, ResizableAwareRole<Anchored> {

	private Resizable resizable;

	private CoordinatePath anchorPath;

	private Shape anchor;
	
	private MarkerShape markerShape = MarkerShape.SQUARE;

	private Group anchorGroup;

	private Coordinate anchorPosition;

	public ResizableAnchorer() {
		this(null, null);
	}
	
	public ResizableAnchorer(Coordinate anchorPosition, MarkerShape markerShape) {
		this.anchorPosition = (anchorPosition != null) ? anchorPosition : new Coordinate();
		this.markerShape = (markerShape != null) ? markerShape : MarkerShape.SQUARE;
		anchor = createAnchor(this.markerShape.getMarkerShape());
		anchorPath = new CoordinatePath(false);
		anchorGroup = new Group();
		anchorGroup.add(anchorPath);
		anchorGroup.add(anchor);
	}
	
	public CoordinatePath getAnchorPath() {
		return anchorPath;
	}
	
	public void setAnchorPath(CoordinatePath anchorPath) {
		this.anchorPath = anchorPath;
	}
	
	protected Shape createAnchor(Shape shape) {
		if (shape != null && shape instanceof Cloneable) {
			anchor = (Shape) ((Cloneable) shape).cloneObject();
		} else {
			// standard marker shape: rectangle
			anchor = new AnchoredRectangle(0, 0, 8, 8, 4, 4);
		}
		anchor.setFixedSize(true);
		anchor.setFillColor("#FF6600");
		anchor.setStrokeColor("#FF6600");
		anchor.setFillOpacity(0.7);
		return anchor;
	}
	
	public void setAnchor(Shape anchor) {
		this.anchor = anchor;
	}

	@Override
	public VectorObject asObject() {
		return anchorGroup;
	}

	@Override
	public void onUpdate() {
		updateAnchor();
	}

	@Override
	public void setAnchorPosition(Coordinate position) {
		anchorPosition = (Coordinate) position.clone();
		updateAnchor();
	}

	@Override
	public Coordinate getAnchorPosition() {
		return anchorPosition;
	}

	@Override
	public void setResizable(Resizable resizable) {
		this.resizable = resizable;
	}

	@Override
	public Anchored asRole() {
		return this;
	}

	@Override
	public RoleType<Anchored> getType() {
		return Anchored.TYPE;
	}

	@Override
	public ResizableAwareRole<Anchored> cloneRole(Resizable resizable) {
		ResizableAnchorer clone = new ResizableAnchorer(getAnchorPosition(), markerShape);
		clone.setResizable(resizable);
		clone.setAnchorLineColor(getAnchorLineColor());
		clone.setAnchorLineOpacity(getAnchorLineOpacity());
		clone.setAnchorLineWidth(getAnchorLineWidth());
		clone.setAnchorPointColor(getAnchorPointColor());
		clone.setAnchorPointOpacity(getAnchorPointOpacity());
		return clone;
	}

	private void updateAnchor() {
		anchor.setUserX(anchorPosition.getX());
		anchor.setUserY(anchorPosition.getY());
		Coordinate anchorEnd = BboxService.getCenterPoint(resizable.getUserBounds());
		anchorPath.setCoordinates(new Coordinate[] { anchorPosition, anchorEnd });
	}

	@Override
	public void setAnchorLineWidth(int width) {
		anchorPath.setStrokeWidth(width);
	}

	@Override
	public int getAnchorLineWidth() {
		return anchorPath.getStrokeWidth();
	}

	@Override
	public void setAnchorLineColor(String color) {
		anchorPath.setStrokeColor(color);
	}

	@Override
	public String getAnchorLineColor() {
		return anchorPath.getStrokeColor();
	}

	@Override
	public void setAnchorLineOpacity(double opacity) {
		anchorPath.setStrokeOpacity(opacity);
	}

	@Override
	public double getAnchorLineOpacity() {
		return anchorPath.getStrokeOpacity();
	}

	@Override
	public void setAnchorPointColor(String color) {
		anchor.setFillColor(color);
		anchor.setStrokeColor(color);
	}

	@Override
	public String getAnchorPointColor() {
		return anchor.getFillColor();
	}

	@Override
	public void setAnchorPointOpacity(double opacity) {
		anchor.setFillOpacity(opacity);
	}

	@Override
	public double getAnchorPointOpacity() {
		return anchor.getFillOpacity();
	}

	@Override
	public Anchored cloneObject() {
		ResizableAnchorer resAnchorer = new ResizableAnchorer(anchorPosition, markerShape);
		resAnchorer.setAnchor(anchor);
		resAnchorer.setResizable(resizable);
		resAnchorer.setAnchorPath(anchorPath);
		return resAnchorer;
	}

	@Override
	public Shape getAnchorPointShape() {
		return anchor;
	}

	@Override
	public void setAnchorPointShape(Shape shape) {
		if (shape != null) {
			anchorGroup.remove(anchor);
			createAnchor(shape);
			anchorGroup.add(anchor);
		}
	}

	@Override
	public MarkerShape getMarkerShape() {
		return markerShape;
	}

	@Override
	public void setAnchorLineDashStyle(AnchorLineDashStyle dashStyle) {
		anchorPath.setDashArray(dashStyle.toString());
	}

}
