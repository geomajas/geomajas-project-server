/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.global;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;

/**
 * Description object for configuring the transformable area for CRS conversions.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public class CrsTransformInfo {

	@NotNull
	private String source;

	@NotNull
	private String target;

	private Bbox transformableArea;

	/**
	 * Get source CRS code.
	 *
	 * @return crs code
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Set source CRS code.
	 *
	 * @param source source CRS code
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Get target CRS code.
	 *
	 * @return target CRS code
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Set target CRS code.
	 *
	 * @param target target CRS code
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Get bbox for transformable area.
	 *
	 * @return transformable area
	 */
	public Bbox getTransformableArea() {
		return transformableArea;
	}

	/**
	 * Set the bbox with the transformable area.
	 *
	 * @param transformableArea transformable area
	 */
	public void setTransformableArea(Bbox transformableArea) {
		this.transformableArea = transformableArea;
	}
}
