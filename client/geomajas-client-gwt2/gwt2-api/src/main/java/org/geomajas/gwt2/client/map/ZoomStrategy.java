/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map;

import org.geomajas.annotation.Api;

/**
 * Strategy that determines all scale levels. The {@link ViewPort} uses such a <code>ZoomStrategy</code> to ask for
 * scale levels when zooming in or out. Therefore different implementations will result in different zoom steps being
 * used on the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ZoomStrategy {

	/**
	 * Zoom options. These express the different ways to zoom in and out on a map.
	 * 
	 * @author Jan De Moerloose
	 * @since 1.0.0
	 */
	public enum ZoomOption {

		/** Zoom to a scale level that is as close as possible to the requested scale. */
		LEVEL_CLOSEST,

		/** Zoom to a scale level that makes the requested scale fit inside our view. */
		LEVEL_FIT
	}

	/**
	 * Set the map's width and height in pixels. Often, <code>ZoomStrategy</code> implementations need this to calculate
	 * certain variables, such as maximum scale.
	 * 
	 * @param mapWidth
	 *            The current map width in pixels.
	 * @param mapHeight
	 *            The current map height in pixels.
	 */
	void setMapSize(int mapWidth, int mapHeight);

	/**
	 * Return the minimum allowed scale. This means the maximum zoom out.
	 * 
	 * @return The minimum allowed scale.
	 */
	double getMinimumScale();

	/**
	 * Return the maximum allowed scale. This means the maximum zoom in.
	 * 
	 * @return The maximum allowed scale.
	 */
	double getMaximumScale();

	/**
	 * Given a certain scale, check it's value and adjust it if necessary. This is the most crucial method within this
	 * <code>ZoomStrategy</code> interface. All scaling methods in the {@link ViewPort} must make use of this method to
	 * determine allowed scaling levels.
	 * 
	 * @param scale
	 *            The requested scale by the {@link ViewPort}.
	 * @param option
	 *            An extra zooming option that may influence the result.
	 * @return Returns the allowed scale as per this <code>ZoomStrategy</code>. Some implementation may allow any scale,
	 *         while others may only allow fixed zooming steps.
	 */
	double checkScale(double scale, ZoomOption option);

	/**
	 * Get the total number of zooming steps that this <code>ZoomStrategy</code> allows for. Note that there is no
	 * guarantee that the total number of zoom steps never changes. Some strategies may have this number varying.
	 * 
	 * @return The total number of zooming steps.
	 */
	int getZoomStepCount();

	/**
	 * Get the scale level associated with a certain zoom step. Index=0 will return the maximum scale (= maximum zoom
	 * in) and subsequent index values will zoom out.
	 * 
	 * @param index
	 *            The zoom step index. Must be a value between 0 and <code>getZoomStepCount() - 1</code>.
	 * @return The scale level associated with a certain zoom step.
	 */
	double getZoomStepScale(int index);

	/**
	 * Get the zoom step index associated with a certain scale.
	 * 
	 * @param scale
	 *            The scale to request a zoom step index for.
	 * @return Returns the zoom step index associated with the given scale. Must be a value between 0 and
	 *         <code>getZoomStepCount() - 1</code>.
	 */
	int getZoomStepIndex(double scale);
}