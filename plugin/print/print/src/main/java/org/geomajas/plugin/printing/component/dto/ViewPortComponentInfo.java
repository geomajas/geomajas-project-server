/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * DTO object for ViewPortComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.ViewPortComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class ViewPortComponentInfo extends MapComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	/**
	 * Ratio of the view port scale and the map scale
	 */
	private float zoomScale;

	/**
	 * X coordinate of view port in user coordinates
	 */
	private float userX = -1;

	/**
	 * Y coordinate of view port in user coordinates
	 */
	private float userY = -1;

	/**
	 * Get zoom scale.
	 *
	 * @return zoom scale
	 */
	public float getZoomScale() {
		return zoomScale;
	}

	/**
	 * Set zoom scale.
	 *
	 * @param zoomScale zoom scale
	 */
	public void setZoomScale(float zoomScale) {
		this.zoomScale = zoomScale;
	}

	/**
	 * Get x user ordinate.
	 *
	 * @return user x ordinate
	 */
	public float getUserX() {
		return userX;
	}

	/**
	 * Set x user ordinate.
	 *
	 * @param userX user x ordinate
	 */
	public void setUserX(float userX) {
		this.userX = userX;
	}

	/**
	 * Get user y ordinate.
	 *
	 * @return y ordinate
	 */
	public float getUserY() {
		return userY;
	}

	/**
	 * Set user y ordinate.
	 *
	 * @param userY user y ordinate
	 */
	public void setUserY(float userY) {
		this.userY = userY;
	}

}
