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
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableAwareRole;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.shape.CoordinatePath;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Standard interface for objects anchored to a {@link Resizable} object.
 * 
 * @author Jan Venstermans
 * 
 */
public class AnchoredToResizable implements AnchoredTo, ResizableAwareRole<AnchoredTo> {

	private Resizable masterObject;
	
	private RelativeAnchorPosition relativeAnchorPositionAtMaster = RelativeAnchorPosition.FIRST_POINT;
	
	protected Resizable slaveObject;
	
	protected CoordinatePath anchorPath;
	
	private AnchorLineDashStyle lineDashStyle = AnchorLineDashStyle.STRAIGHT;
	
	public AnchoredToResizable(Resizable masterObject) {
		this.masterObject = masterObject;
		anchorPath = new CoordinatePath(false);
	}

	@Override
	public void onUpdate() {
		updateAnchorLine();		
	}

	@Override
	public void setResizable(Resizable resizable) {
		this.slaveObject = resizable;
	}

	@Override
	public AnchoredTo asRole() {
		return this;
	}

	@Override
	public RoleType<AnchoredTo> getType() {
		return AnchoredTo.TYPE;
	}

	@Override
	public VectorObject asObject() {
		return anchorPath;
	}

	@Override
	public ResizableAwareRole<AnchoredTo> cloneRole(Resizable resizable) {
		AnchoredToResizable clone = new AnchoredToResizable(masterObject);
		return addProperties(clone);
	}
	
	protected AnchoredToResizable addProperties(AnchoredToResizable anchoredTo) {
		anchoredTo.setResizable(slaveObject);
		anchoredTo.setAnchorLineColor(getAnchorLineColor());
		anchoredTo.setAnchorLineOpacity(getAnchorLineOpacity());
		anchoredTo.setAnchorLineWidth(getAnchorLineWidth());
		anchoredTo.setAnchorLineDashStyle(lineDashStyle);
		return anchoredTo;
	}

	@Override
	public AnchoredTo cloneObject() {
		AnchoredToResizable anchoredTo = new AnchoredToResizable(masterObject);
		anchoredTo.setResizable(slaveObject);
		anchoredTo.setAnchorPath(getAnchorLineClone());
		return anchoredTo;
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
	public void setSlavePosition(Coordinate position) {
		slaveObject.setPosition(position);
		updateAnchorLine();
	}

	@Override
	public Coordinate getSlavePosition() {
		return slaveObject.getPosition();
	}

	@Override
	public void setMasterPosition(Coordinate position) {
		masterObject.setPosition(position);
		updateAnchorLine();
	}

	@Override
	public Coordinate getMasterPosition() {
		return masterObject.getPosition();
	}
	
	public void setAnchorPath(CoordinatePath anchorPath) {
		this.anchorPath = anchorPath;
	}
	
	@Override
	public void updateAnchorLine() {
		anchorPath.setCoordinates(new Coordinate[] { getSlavePosition(), getMasterAnchorCoordinate()});
	}
	
	public Coordinate getMasterAnchorCoordinate() {
		Coordinate masterCoord;
		switch (relativeAnchorPositionAtMaster) {
			case CENTER:
				masterCoord = BboxService.getCenterPoint(masterObject.getUserBounds());
				break;
			case CLOSEST_POINT:
			case FIRST_POINT:
				default:
				masterCoord = getMasterPosition();
				break;
		}
		return masterCoord;
	}

	@Override
	public Resizable getMasterObject() {
		return masterObject;
	}

	@Override
	public CoordinatePath getAnchorLineClone() {
		CoordinatePath clone = new CoordinatePath(false);
		clone.setCoordinates(new Coordinate[] {
				new Coordinate(getSlavePosition()), new Coordinate(getMasterAnchorCoordinate())});
		clone.setStrokeColor(anchorPath.getStrokeColor());
		clone.setStrokeOpacity(anchorPath.getStrokeOpacity());
		clone.setStrokeWidth(anchorPath.getStrokeWidth());
		clone.setDashArray(lineDashStyle.toString());
		return clone;
	}

	@Override
	public void setRelativeAnchoringPositionAtMasterObject(RelativeAnchorPosition relativeAnchorPositionAtMaster) {
		this.relativeAnchorPositionAtMaster	= relativeAnchorPositionAtMaster;
	}

	@Override
	public void setAnchorLineDashStyle(AnchorLineDashStyle dashStyle) {
		this.lineDashStyle = dashStyle;
		anchorPath.setDashArray(dashStyle.toString());
	}
	
}
