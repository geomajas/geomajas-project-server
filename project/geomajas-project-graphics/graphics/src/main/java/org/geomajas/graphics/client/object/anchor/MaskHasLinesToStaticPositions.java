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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableAwareRole;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.shape.CoordinatePath;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Implementation of {@link Anchored} role for {@link Resizable} objects.
 * 
 * @author Jan Venstermans
 * 
 */
public class MaskHasLinesToStaticPositions implements MaskHasLines, ResizableAwareRole<MaskHasLines> {

	private Resizable resizable;

	private Group linesGroup;
		
	private Map<CoordinatePath, Coordinate> pathsWithOriginalShift;
	
	public MaskHasLinesToStaticPositions() {
		this(null);
	}
	
	public MaskHasLinesToStaticPositions(List<CoordinatePath> paths) {
		this.pathsWithOriginalShift = new LinkedHashMap<CoordinatePath, Coordinate>();
		linesGroup = new Group();
		for (CoordinatePath path : paths) {
			pathsWithOriginalShift.put(path, path.getCoordinates()[0]);
			linesGroup.add(path);
		}
	}
	
	@Override
	public VectorObject asObject() {
		return linesGroup;
	}

	@Override
	public void onUpdate() {
		updateAnchor();
	}

	@Override
	public void setResizable(Resizable resizable) {
		this.resizable = resizable;
		//determine shift of anchor points of paths to the position of the resizable
		Coordinate resizablePosition = resizable.getPosition();
		for (CoordinatePath path : pathsWithOriginalShift.keySet()) {
			Coordinate anchorCoord = path.getCoordinates()[1];
			pathsWithOriginalShift.put(path,
					new Coordinate(anchorCoord.getX() - resizablePosition.getX(), anchorCoord.getY()
							- resizablePosition.getY()));
		}
	}

	@Override
	public MaskHasLines asRole() {
		return this;
	}

	@Override
	public RoleType<MaskHasLines> getType() {
		return MaskHasLines.TYPE;
	}

	@Override
	public ResizableAwareRole<MaskHasLines> cloneRole(Resizable resizable) {
		MaskHasLinesToStaticPositions clone = new MaskHasLinesToStaticPositions(new ArrayList<CoordinatePath>(
				pathsWithOriginalShift.keySet()));
		clone.setResizable(resizable);
		return clone;
	}

	private void updateAnchor() {
		Coordinate anchorEnd = resizable.getPosition();
		for (CoordinatePath path : pathsWithOriginalShift.keySet()) {
			Coordinate shift = pathsWithOriginalShift.get(path);
			path.setCoordinates(new Coordinate[] { path.getCoordinates()[0],
					new Coordinate(anchorEnd.getX() + shift.getX(), anchorEnd.getY() + shift.getY()) });
		}
	}

}
