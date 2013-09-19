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
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.shape.CoordinatePath;

/**
 * Anchor line between a slave object and a master object.
 * Slave will contain this role, master is considered as the anchor.
 * 
 * @author Jan Venstermans
 * 
 */
public interface AnchoredTo extends AnchorLineStyle {

	RoleType<AnchoredTo> TYPE = new RoleType<AnchoredTo>("AnchoredTo");

	void setSlavePosition(Coordinate position);

	Coordinate getSlavePosition();
	
	//this will (or may) not be used?
	void setMasterPosition(Coordinate position);

	Coordinate getMasterPosition();
	
	// for showing clone of anchorline only when draggable and dragging
	AnchoredTo cloneObject();
	
	Object getMasterObject();
	
	void updateAnchorLine();
	
	CoordinatePath getAnchorLineClone();
	
	void setRelativeAnchoringPositionAtMasterObject(RelativeAnchorPosition relativeAnchorPositionAtMaster);
	
	/**
	 * Enum of connection points of an anchor line, relative to a Resizable object.
	 * 
	 * @author Jan Venstermans
	 * 
	 */
	public enum RelativeAnchorPosition {
		CENTER, FIRST_POINT, CLOSEST_POINT
	}
}
