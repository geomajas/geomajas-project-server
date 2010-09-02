/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.global.Api;


/**
 * Constraints for the default layout.
 *
 * @author Jan De Moerloose
 */
@Api(allMethods = true)
public class LayoutConstraintInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	/**
	 * width (0 means auto-size)
	 */
	private float width;

	/**
	 * height (0 means auto-size)
	 */
	private float height;

	/**
	 * Margin in x-direction or x-position if alignmentX = ABSOLUTE
	 */
	private float marginX;

	/**
	 * Margin in y-direction or y-position if alignmentY = ABSOLUTE
	 */
	private float marginY;

	/**
	 * alignment in x-direction (LEFT, CENTER, RIGHT, JUSTIFIED, ABSOLUTE)
	 */
	private int alignmentX = LEFT;

	/**
	 * alignment in y-direction (BOTTOM, CENTER, TOP, JUSTIFIED, ABSOLUTE)
	 */
	private int alignmentY = BOTTOM;

	/**
	 * flow direction (for containers)
	 */
	private int flowDirection = FLOW_NONE;

	public static final int LEFT = 0;

	public static final int BOTTOM = 1;

	public static final int CENTER = 2;

	public static final int RIGHT = 3;

	public static final int TOP = 4;

	public static final int JUSTIFIED = 5;

	public static final int ABSOLUTE = 6;

	public static final int FLOW_X = 0;

	public static final int FLOW_Y = 1;

	public static final int FLOW_NONE = 2;

	public LayoutConstraintInfo() {
	}

	public LayoutConstraintInfo(int alignmentX, int alignmentY, int flowDirection, float width, float height,
			float marginX, float marginY) {
		this.alignmentX = alignmentX;
		this.alignmentY = alignmentY;
		this.flowDirection = flowDirection;
		this.height = height;
		this.marginX = marginX;
		this.marginY = marginY;
		this.width = width;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getMarginX() {
		return marginX;
	}

	public void setMarginX(float marginX) {
		this.marginX = marginX;
	}

	public float getMarginY() {
		return marginY;
	}

	public void setMarginY(float marginY) {
		this.marginY = marginY;
	}

	public int getAlignmentX() {
		return alignmentX;
	}

	public void setAlignmentX(int alignmentX) {
		this.alignmentX = alignmentX;
	}

	public int getAlignmentY() {
		return alignmentY;
	}

	public void setAlignmentY(int alignmentY) {
		this.alignmentY = alignmentY;
	}

	public int getFlowDirection() {
		return flowDirection;
	}

	public void setFlowDirection(int flowDirection) {
		this.flowDirection = flowDirection;
	}

}
