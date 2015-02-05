/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component;

import org.geomajas.plugin.printing.component.dto.LayoutConstraintInfo;

/**
 * Constraints for the default layout.
 *
 * @author Jan De Moerloose
 */
public class LayoutConstraint {

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

	public LayoutConstraint() {
	}

	public LayoutConstraint(int alignmentX, int alignmentY, int flowDirection, float width, float height,
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

	public void fromDto(LayoutConstraintInfo layoutConstraint) {
		setAlignmentX(layoutConstraint.getAlignmentX());
		setAlignmentY(layoutConstraint.getAlignmentY());
		setFlowDirection(layoutConstraint.getFlowDirection());
		setWidth(layoutConstraint.getWidth());
		setHeight(layoutConstraint.getHeight());
		setMarginX(layoutConstraint.getMarginX());
		setMarginY(layoutConstraint.getMarginY());
	}

}
