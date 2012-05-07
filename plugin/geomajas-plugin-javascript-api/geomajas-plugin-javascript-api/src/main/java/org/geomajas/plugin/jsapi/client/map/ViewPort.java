/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.jsapi.client.map;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable facade for a map's ViewPort. The Central view port definition that determines and influences
 * the position and current view of the map.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ViewPort extends Exportable {

	/** World render space. */
	String RENDER_SPACE_WORLD = "world";

	/** Screen render space. */
	String RENDER_SPACE_SCREEN = "screen";

	/**
	 * Get the current center position expressed in world space.
	 * 
	 * @return The current center position expressed in world space.
	 */
	Coordinate getPosition();

	/**
	 * Return the current scale on the map.
	 */
	double getScale();

	/**
	 * Return the currently visible bounds on the map. These bounds are expressed in the CRS of the map.
	 * 
	 * @return Returns the maps bounding box.
	 */
	Bbox getBounds();

	/**
	 * Re-centers the map to a new position.
	 * 
	 * @param coordinate
	 *            the new center position
	 */
	void applyPosition(Coordinate coordinate);

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this MapView, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param newScale
	 *            The preferred new scale.
	 */
	void applyScale(double newScale);

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this MapView, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param scale
	 *            The preferred new scale.
	 * @param rescalePoint
	 *            After zooming, this point will still be on the same position in the view as before. Makes for easy
	 *            double clicking on the map without it moving away.
	 */
	void applyScale(double scale, Coordinate rescalePoint);

	/**
	 * <p>
	 * Change the view on the map by applying a bounding box (world coordinates!). Since the width/height ratio of the
	 * bounding box may differ from that of the map, the fit is "as good as possible".
	 * </p>
	 * <p>
	 * Also this function will almost certainly change the scale on the map, so if there have been resolutions defined,
	 * it will snap to them.
	 * </p>
	 * 
	 * @param bounds
	 *            A bounding box in world coordinates that determines the view from now on.
	 */
	void applyBounds(Bbox bounds);

	/**
	 * Get the maximum zooming extent that is allowed on this view port. These bounds are determined by the map
	 * configuration.
	 * 
	 * @return The maximum zooming extent that is allowed on this view port.
	 */
	Bbox getMaximumBounds();

	// ------------------------------------------------------------------------
	// ViewPort transformation methods:
	// ------------------------------------------------------------------------

	/**
	 * Transform the given coordinate from a certain rendering space to another.
	 * 
	 * @param coordinate
	 *            The coordinate to transform. The X and Y ordinates are expected to be expressed in the 'from'
	 *            rendering space.
	 * @param from
	 *            The rendering space that expresses the X and Y ordinates of the given coordinate.
	 * @param to
	 *            The rendering space where to the coordinate should be transformed.
	 * @return The transformed coordinate.
	 */
	Coordinate transform(Coordinate coordinate, String from, String to);
}