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
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.annotation.Api;


/**
 * Constraints for the default layout.
 *
 * @author Jan De Moerloose
 * @since 2.0.0
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

	/** Align left. */
	public static final int LEFT = 0;
	/** Align bottom. */
	public static final int BOTTOM = 1;
	/** Align center. */
	public static final int CENTER = 2;
	/** Align right. */
	public static final int RIGHT = 3;
	/** Align top. */
	public static final int TOP = 4;
	/** Justified alignation. */
	public static final int JUSTIFIED = 5;
	/** Absolute alignation. */
	public static final int ABSOLUTE = 6;
	/** Flow x horizontal. */
	public static final int FLOW_X = 0;
	/** Flow vertical. */
	public static final int FLOW_Y = 1;
	/** No flow. */
	public static final int FLOW_NONE = 2;

	/** No-arguments constructor for GWT. */
	public LayoutConstraintInfo() {
	}

	/**
	 * Constructor.
	 *
	 * @param alignmentX x alignment
	 * @param alignmentY y alignment
	 * @param flowDirection flow direction
	 * @param width width
	 * @param height height
	 * @param marginX x margin
	 * @param marginY y margin
	 */
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

	/**
	 * Get width.
	 *
	 * @return width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Set width.
	 *
	 * @param width width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Get height.
	 *
	 * @return height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Set height.
	 *
	 * @param height height
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * Get horizontal margin.
	 *
	 * @return x margin
	 */
	public float getMarginX() {
		return marginX;
	}

	/**
	 * Set horizontal margin.
	 *
	 * @param marginX x margin
	 */
	public void setMarginX(float marginX) {
		this.marginX = marginX;
	}

	/**
	 * Get vertical margin.
	 *
	 * @return y margin
	 */
	public float getMarginY() {
		return marginY;
	}

	/**
	 * Set vertical margin.
	 *
	 * @param marginY y margin
	 */
	public void setMarginY(float marginY) {
		this.marginY = marginY;
	}

	/**
	 * Get horizontal alignment.
	 *
	 * @return x alignment
	 */
	public int getAlignmentX() {
		return alignmentX;
	}

	/**
	 * Set horizontal alignment.
	 *
	 * @param alignmentX x alignment
	 */
	public void setAlignmentX(int alignmentX) {
		this.alignmentX = alignmentX;
	}

	/**
	 * Get vertical alignment.
	 *
	 * @return x alignment
	 */
	public int getAlignmentY() {
		return alignmentY;
	}

	/**
	 * Set vertical alignment.
	 *
	 * @param alignmentY y alignment
	 */
	public void setAlignmentY(int alignmentY) {
		this.alignmentY = alignmentY;
	}

	/**
	 * Get flow direction. Possible values are {@link #FLOW_NONE}, {@link #FLOW_X}, {@link #FLOW_Y}.
	 *
	 * @return flow direction
	 */
	public int getFlowDirection() {
		return flowDirection;
	}

	/**
	 * Set flow direction. Possible values are {@link #FLOW_NONE}, {@link #FLOW_X}, {@link #FLOW_Y}.
	 *
	 * @param flowDirection flow direction
	 */
	public void setFlowDirection(int flowDirection) {
		this.flowDirection = flowDirection;
	}

}
