/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.spatial;

import org.geomajas.global.FutureApi;

/**
 * <p>
 * A very simple matrix definition. Used for transformations on geometries, HTML objects or vector objects.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface Matrix {

	/**
	 * The identity matrix.
	 * 
	 * @author Pieter De Graef
	 */
	Matrix IDENTITY = new Matrix() {

		public double getXx() {
			return 1;
		}

		public double getXy() {
			return 0;
		}

		public double getYx() {
			return 0;
		}

		public double getYy() {
			return 1;
		}

		public double getDx() {
			return 0;
		}

		public double getDy() {
			return 0;
		}
	};

	/**
	 * Get the scale factor along the X-axis.
	 * 
	 * @return The scale factor along the X-axis.
	 */
	double getXx();

	double getXy();

	double getYx();

	/**
	 * Get the scale factor along the Y-axis.
	 * 
	 * @return The scale factor along the Y-axis.
	 */
	double getYy();

	/**
	 * Get the translation parameter along the X axis.
	 * 
	 * @return The translation parameter along the X axis.
	 */
	double getDx();

	/**
	 * Get the translation parameter along the Y axis.
	 * 
	 * @return The translation parameter along the Y axis.
	 */
	double getDy();
}