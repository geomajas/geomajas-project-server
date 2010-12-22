package org.geomajas.puregwt.client.map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.puregwt.client.spatial.Bbox;

/**
 * ...
 * 
 * @author Pieter De Graef
 * @author Oliver May
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
public interface ViewPort {

	TransformationService getTransformationService();

	// -------------------------------------------------------------------------
	// Methods that retrieve what is visible on the map:
	// -------------------------------------------------------------------------

	double getScale();

	Bbox getBounds();

	// -------------------------------------------------------------------------
	// Methods that manipulate what is visible on the map:
	// -------------------------------------------------------------------------

	/**
	 * Re-centers the map to a new position.
	 * 
	 * @param coordinate
	 *            the new center position
	 */
	public void applyPosition(Coordinate coordinate);

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this MapView, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param scale
	 *            The preferred new scale.
	 * @param option
	 *            zoom option, {@link org.geomajas.puregwt.client.map.ZoomOption}
	 */
	public void applyScale(double scale, ZoomOption option);

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this MapView, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param scale
	 *            The preferred new scale.
	 * @param option
	 *            zoom option, {@link org.geomajas.puregwt.client.map.ZoomOption}
	 * @param rescalePoint
	 *            After zooming, this point will still be on the same position in the view as before. Makes for easy
	 *            double clicking on the map without it moving away.
	 */
	public void applyScale(double scale, ZoomOption option, Coordinate rescalePoint);

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
	 * @param option
	 *            zoom option, {@link org.geomajas.puregwt.client.map.ZoomOption}
	 */
	public void applyBounds(Bbox bounds, ZoomOption option);

	/**
	 * Move the view on the map. This happens by translating the camera in turn.
	 * 
	 * @param x
	 *            Translation factor along the X-axis in world space.
	 * @param y
	 *            Translation factor along the Y-axis in world space.
	 */
	public void translate(double x, double y);

	/**
	 * Adjust the current scale on the map by a new factor.
	 * 
	 * @param delta
	 *            Adjust the scale by factor "delta".
	 * @param option
	 *            The zooming option to use when applying the scaling transformation.
	 */
	public void scale(double delta, ZoomOption option);

	/**
	 * Adjust the current scale on the map by a new factor, keeping a coordinate in place.
	 * 
	 * @param delta
	 *            Adjust the scale by factor "delta".
	 * @param option
	 *            The zooming option to use when applying the scaling transformation.
	 * @param center
	 *            Keep this coordinate on the same position as before.
	 * 
	 */
	public void scale(double delta, ZoomOption option, Coordinate center);

	/**
	 * Drag the view on the map, without firing definitive ViewPortChanged events. This is used while dragging the map.
	 * Other than the events, it behaves the same as a translate.
	 * 
	 * @param x
	 *            Translation factor along the X-axis in world space.
	 * @param y
	 *            Translation factor along the Y-axis in world space.
	 */
	public void drag(double x, double y);
}
