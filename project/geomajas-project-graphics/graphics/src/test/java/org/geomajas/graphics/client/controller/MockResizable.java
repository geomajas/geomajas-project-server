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
package org.geomajas.graphics.client.controller;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.BaseGraphicsObject;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.util.FlipState;
import org.geomajas.graphics.client.util.GraphicsUtil;
import org.vaadin.gwtgraphics.client.VectorObject;

public class MockResizable extends BaseGraphicsObject implements Resizable {

	private Coordinate position;

	private Bbox bounds;

	private FlipState flipState = FlipState.NONE;

	public MockResizable(Coordinate position, Bbox bounds) {
		setPosition(position);
		setUserBounds(bounds);
		addRole(Resizable.TYPE, this);
		addRole(Draggable.TYPE, this);
	}

	@Override
	public void setPosition(Coordinate position) {
		this.position = (Coordinate) position.clone();
	}

	@Override
	public Coordinate getPosition() {
		return new Coordinate(position);
	}

	@Override
	public void setOpacity(double opacity) {
	}

	@Override
	public GraphicsObject cloneObject() {
		return null;
	}

	@Override
	public VectorObject asObject() {
		return null;
	}

	@Override
	public void flip(FlipState newState) {
		if (newState == FlipState.FLIP_X) {
			switch (flipState) {
				case FLIP_X:
					flipState = FlipState.NONE;
					break;
				case FLIP_XY:
					flipState = FlipState.FLIP_Y;
					break;
				case FLIP_Y:
					flipState = FlipState.FLIP_XY;
					break;
				case NONE:
					flipState = FlipState.FLIP_X;
					break;
			}
		} else if (newState == FlipState.FLIP_Y) {
			switch (flipState) {
				case FLIP_X:
					flipState = FlipState.FLIP_XY;
					break;
				case FLIP_XY:
					flipState = FlipState.FLIP_X;
					break;
				case FLIP_Y:
					flipState = FlipState.NONE;
					break;
				case NONE:
					flipState = FlipState.FLIP_Y;
					break;
			}
		} else if (newState == FlipState.FLIP_XY) {
			switch (flipState) {
				case FLIP_X:
					flipState = FlipState.FLIP_Y;
					break;
				case FLIP_XY:
					flipState = FlipState.NONE;
					break;
				case FLIP_Y:
					flipState = FlipState.FLIP_X;
					break;
				case NONE:
					flipState = FlipState.FLIP_XY;
					break;
			}
		}
	}

	public FlipState getFlipState() {
		return flipState;
	}

	@Override
	public void setUserBounds(Bbox bounds) {
		this.bounds = GraphicsUtil.clone(bounds);
	}

	@Override
	public Bbox getUserBounds() {
		return GraphicsUtil.clone(bounds);
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
	public Bbox getBounds() {
		return bounds;
	}

}
