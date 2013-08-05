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

package org.geomajas.smartgwt.client.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.BoundsLimitOption;
import org.geomajas.smartgwt.client.map.event.MapViewChangedEvent;
import org.geomajas.smartgwt.client.map.event.MapViewChangedHandler;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.spatial.Matrix;
import org.geomajas.smartgwt.client.spatial.WorldViewTransformer;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * This class represents the viewing controller behind a <code>MapWidget</code>. It knows the map's width, height, but
 * it also controls what is visible on the map through a <code>Camera</code> object. This camera hangs over the map at a
 * certain height (represented by the scale), and together with the width and height, this MapView can determine the
 * boundaries of the visible area on the map.
 * </p>
 * <p>
 * But it's more then that. This MapView can also calculate necessary transformation matrices to go from world to view
 * space an back. It can also snap the scale-levels to fixed resolutions (in case these are actually defined).
 * </p>
 * 
 * @author Pieter De Graef
 * @author Oliver May
 * @since 1.6.0
 */
@Api
public class MapView {

	private static final double MAX_RESOLUTION = Float.MAX_VALUE;

	/** Zoom options. */
	public enum ZoomOption {

		/** Zoom exactly to the new scale. */
		EXACT,
		/**
		 * Zoom to a scale level that is different from the current (lower or higher according to the new scale, only if
		 * allowed of course).
		 */
		LEVEL_CHANGE,
		/** Zoom to a scale level that is as close as possible to the new scale. */
		LEVEL_CLOSEST,
		/** Zoom to a scale level that makes the bounds fit inside our view. */
		LEVEL_FIT
	}
	
	/** 
	 * The currently configured option for limiting the mapview's bounds when applying the maxBounds limitation. 
	 */

	private BoundsLimitOption viewBoundsLimitOption = BoundsLimitOption.COMPLETELY_WITHIN_MAX_BOUNDS;

	/** The map's width in pixels. */
	private int width;

	/** The map's height in pixels. */
	private int height;

	/** A maximum scale level, that this MapView is not allowed to cross. */
	private double maximumScale = 10;

	/** The maximum bounding box available to this MapView. Applied on map view according to current 
	 * ViewBoundsOption  */
	private Bbox maxBounds;

	/**
	 * A series of scale levels to which zooming in and out should snap. This is optional! If you which to use these
	 * fixed zooming steps, all you have to do, is define them.
	 */
	private List<Double> resolutions = new ArrayList<Double>();

	/**
	 * The current index in the resolutions array. That is, if the resolutions are actually used.
	 */
	private int resolutionIndex = -1;

	/**
	 * The current view state.
	 */
	private MapViewState viewState = new MapViewState();

	/**
	 * The previous view state.
	 */
	private MapViewState lastViewState;

	private HandlerManager handlerManager;

	private WorldViewTransformer worldViewTransformer;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/** Default constructor that initializes all it's fields. */
	public MapView() {
		handlerManager = new HandlerManager(this);
	}

	/**
	 * Adds this handler to the view.
	 * 
	 * @param handler
	 *            the handler
	 * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove the handler
	 */
	public final HandlerRegistration addMapViewChangedHandler(final MapViewChangedHandler handler) {
		return handlerManager.addHandler(MapViewChangedEvent.getType(), handler);
	}

	
	// -------------------------------------------------------------------------
	// Retrieval of transformation matrices:
	// -------------------------------------------------------------------------

	/**
	 * Return the world-to-view space transformation matrix.
	 *
	 * @return transformation matrix
	 */
	public Matrix getWorldToViewTransformation() {
		if (viewState.getScale() > 0) {
			double dX = -(viewState.getX() * viewState.getScale()) + width / 2;
			double dY = viewState.getY() * viewState.getScale() + height / 2;
			return new Matrix(viewState.getScale(), 0, 0, -viewState.getScale(), dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/**
	 * Return the world-to-view space translation matrix.
	 *
	 * @return transformation matrix
	 */
	public Matrix getWorldToViewTranslation() {
		if (viewState.getScale() > 0) {
			double dX = -(viewState.getX() * viewState.getScale()) + width / 2;
			double dY = viewState.getY() * viewState.getScale() + height / 2;
			return new Matrix(1, 0, 0, 1, dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/**
	 * Return the world-to-pan space translation matrix.
	 *
	 * @return transformation matrix
	 */
	public Matrix getWorldToPanTransformation() {
		if (viewState.getScale() > 0) {
			double dX = -(viewState.getPanX() * viewState.getScale());
			double dY = viewState.getPanY() * viewState.getScale();
			return new Matrix(viewState.getScale(), 0, 0, -viewState.getScale(), dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/**
	 * Return the translation of coordinates relative to the pan origin to view coordinates.
	 *
	 * @return transformation matrix
	 */
	public Matrix getPanToViewTranslation() {
		if (viewState.getScale() > 0) {
			double dX = -((viewState.getX() - viewState.getPanX()) * viewState.getScale()) + width / 2;
			double dY = (viewState.getY() - viewState.getPanY()) * viewState.getScale() + height / 2;
			return new Matrix(1, 0, 0, 1, dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/**
	 * Return the translation of scaled world coordinates to coordinates relative to the pan origin.
	 *
	 * @return transformation matrix
	 */
	public Matrix getWorldToPanTranslation() {
		if (viewState.getScale() > 0) {
			double dX = -(viewState.getPanX() * viewState.getScale());
			double dY = viewState.getPanY() * viewState.getScale();
			return new Matrix(1, 0, 0, 1, dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/**
	 * Return the world-to-view space translation matrix.
	 *
	 * @return transformation matrix
	 */
	public Matrix getWorldToViewScaling() {
		if (viewState.getScale() > 0) {
			return new Matrix(viewState.getScale(), 0, 0, -viewState.getScale(), 0, 0);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	// -------------------------------------------------------------------------
	// Functions that manipulate or retrieve what is visible on the map:
	// -------------------------------------------------------------------------

	/**
	 * Re-centers the map to a new position.
	 * 
	 * @param coordinate
	 *            the new center position
	 */
	public void setCenterPosition(Coordinate coordinate) {
		saveState();
		doSetOrigin(coordinate);
		fireEvent(false, null);
	}

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this MapView, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param newScale
	 *            The preferred new scale.
	 * @param option
	 *            zoom option, {@link MapView.ZoomOption}
	 */
	public void setCurrentScale(final double newScale, final ZoomOption option) {
		setCurrentScale(newScale, option, new Coordinate(viewState.getX(), viewState.getY()));
	}

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this MapView, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param newScale
	 *            The preferred new scale.
	 * @param option
	 *            zoom option, {@link MapView.ZoomOption}
	 * @param rescalePoint
	 *            After zooming, this point will still be on the same position in the view as before.
	 */
	public void setCurrentScale(final double newScale, final ZoomOption option, final Coordinate rescalePoint) {
		saveState();
		// calculate theoretical new bounds
		Bbox newBbox = new Bbox(0, 0, getWidth() / newScale, getHeight() / newScale);

		double factor = newScale / getCurrentScale();

		// Calculate translate vector to assure rescalePoint is on the same
		// position as before.
		double dX = (rescalePoint.getX() - viewState.getX()) * (1 - 1 / factor);
		double dY = (rescalePoint.getY() - viewState.getY()) * (1 - 1 / factor);

		newBbox.setCenterPoint(new Coordinate(viewState.getX(), viewState.getY()));
		newBbox.translate(dX, dY);
		// and apply...
		doApplyBounds(newBbox, option);
	}

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
	 *            zoom option, {@link MapView.ZoomOption}
	 */
	public void applyBounds(final Bbox bounds, final ZoomOption option) {
		saveState();
		doApplyBounds(bounds, option);
	}

	/**
	 * Set the size of the map in pixels.
	 * 
	 * @param newWidth
	 *            The map's width.
	 * @param newHeight
	 *            The map's height.
	 */
	public void setSize(int newWidth, int newHeight) {
		saveState();
		Bbox bbox = getBounds();
		if (width != newWidth || height != newHeight) {
			this.width = newWidth;
			this.height = newHeight;
			if (viewState.getScale() < getMinimumScale()) {
				// The new scale is too low, re-apply old values:
				double scale = getBestScale(bbox);
				doSetScale(snapToResolution(scale, ZoomOption.LEVEL_FIT));
			}
			// Use the same center point for the new bounds
			doSetOrigin(bbox.getCenterPoint());
			fireEvent(true, null);
		}
	}

	/**
	 * Move the view on the map. This happens by translating the camera in turn.
	 * 
	 * @param x
	 *            Translation factor along the X-axis in world space.
	 * @param y
	 *            Translation factor along the Y-axis in world space.
	 */
	public void translate(double x, double y) {
		saveState();
		doSetOrigin(new Coordinate(viewState.getX() + x, viewState.getY() + y));
		fireEvent(false, null);
	}

	/**
	 * Adjust the current scale on the map by a new factor.
	 * 
	 * @param delta Adjust the scale by factor "delta".
	 * @param option zoom option
	 */
	public void scale(double delta, ZoomOption option) {
		setCurrentScale(viewState.getScale() * delta, option);
	}

	/**
	 * Adjust the current scale on the map by a new factor, keeping a coordinate in place.
	 * 
	 * @param delta Adjust the scale by factor "delta".
	 * @param option zoom option
	 * @param center Keep this coordinate on the same position as before.
	 */
	public void scale(double delta, ZoomOption option, Coordinate center) {
		setCurrentScale(viewState.getScale() * delta, option, center);
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	/**
	 * Return the current scale.
	 *
	 * @return current scale
	 */
	public double getCurrentScale() {
		return viewState.getScale();
	}

	/**
	 * Given the information in this MapView object, what is the currently visible area?
	 * 
	 * @return Notice that at this moment an Axis Aligned Bounding Box is returned! This means that rotating is not yet
	 *         possible.
	 */
	public Bbox getBounds() {
		double w = getViewSpaceWidth();
		double h = getViewSpaceHeight();
		double x = viewState.getX() - w / 2;
		double y = viewState.getY() - h / 2;
		return new Bbox(x, y, w, h);
	}

	/**
	 * Set the list of predefined map resolutions (resolution = inverse of scale).
	 * 
	 * @param resolutions
	 *            the list of predefined resolutions (expressed in map unit/pixel)
	 */
	public void setResolutions(List<Double> resolutions) {
		this.resolutions.clear();
		this.resolutions.addAll(resolutions);
		Collections.sort(this.resolutions, Collections.reverseOrder());
	}

	/**
	 * Get the list of predefined map resolutions (resolution = inverse of scale).
	 *
	 * @return list of resolutions
	 */
	public List<Double> getResolutions() {
		return resolutions;
	}

	/**
	 * Is the given resolution available (given the maximum bounds, and current size of the map) or not?
	 * 
	 * @param resolution
	 *            The resolution to calculate availability for.
	 * @return Returns true or false. If false, the given resolution cannot be reached.
	 * @since 1.8.0
	 */
	public boolean isResolutionAvailable(double resolution) {
		double max = MAX_RESOLUTION;
		double minimumScale = getMinimumScale();
		if (minimumScale > 0) {
			max = 1.0 / getMinimumScale();
		}
		double min = 1.0 / maximumScale;
		return resolution >= min && resolution <= max;
	}

	/**
	 * Return the transformer that is used to transform coordinate and geometries between world and screen space.
	 *
	 * @return world view transformer
	 */
	public WorldViewTransformer getWorldViewTransformer() {
		if (null == worldViewTransformer) {
			worldViewTransformer = new WorldViewTransformer(this);
		}
		return worldViewTransformer;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setMaximumScale(double maximumScale) {
		if (maximumScale > 0) {
			this.maximumScale = maximumScale;
		}
	}

	/**
	 * @return ViewBoundsLimitOption
	 * 					The current value of the configuration item for limiting the mapview's bounds 
	 * 					when applying the maxBounds limitation.
	 */
	public BoundsLimitOption getViewBoundsLimitOption() {
		return viewBoundsLimitOption;
	}

	/**
	 * @param viewBoundsLimitOption 	
	 * 					The desired value of the configuration item for limiting the mapview's bounds 
	 * 					when applying the maxBounds limitation.
	 */
	public void setViewBoundsLimitOption(BoundsLimitOption viewBoundsLimitOption) {
		 if (null != viewBoundsLimitOption) {
			 this.viewBoundsLimitOption = viewBoundsLimitOption;
		 }
	}
	
	
	public Bbox getMaxBounds() {
		return maxBounds;
	}

	public void setMaxBounds(Bbox maxBounds) {
		this.maxBounds = maxBounds;
	}

	public void setPanDragging(boolean panDragging) {
		saveState();
		viewState = viewState.copyAndSetPanDragging(panDragging);
	}

	public MapViewState getViewState() {
		return viewState;
	}

	public Coordinate getPanOrigin() {
		return new Coordinate(viewState.getPanX(), viewState.getPanY());
	}

	public String toString() {
		return "VIEW: " + viewState.toString();
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	private boolean doSetScale(double scale) {
		boolean res = Math.abs(viewState.getScale() - scale) > .0000001;
		viewState = viewState.copyAndSetScale(scale);
		return res;
	}

	private void doSetOrigin(Coordinate coordinate) {
		Coordinate center = calcCenterFromPoint(coordinate);
		viewState = viewState.copyAndSetOrigin(center.getX(), center.getY());
	}

	private void doApplyBounds(Bbox bounds, ZoomOption option) {
		if (bounds != null) {
			// first set the scale, taking minimum and maximum scale into
			// account
			// boolean scaleChanged = false;
			if (!bounds.isEmpty()) {
				// find best scale
				double scale = getBestScale(bounds);
				// snap and limit
				scale = snapToResolution(scale, option);
				// set scale
				doSetScale(scale);
			}
			// now translate, taking maximum bounds into account
			doSetOrigin(bounds.getCenterPoint());
			if (bounds.isEmpty()) {
				fireEvent(false, null);
			} else {
				// find pan origin by rounding origin to 10000 x 10000 grid (to enable caching)
				// pan origin is given in world space but the rounding is done in pixels,
				// so convert to pixel, round, convert back to world
				double x = Math.round(viewState.getX() * viewState.getScale() / 10000) * 10000 / viewState.getScale();
				double y = Math.round(viewState.getY() * viewState.getScale() / 10000) * 10000 / viewState.getScale();
				// set pan origin
				viewState = viewState.copyAndSetPanOrigin(x, y);
				fireEvent(false, option);
			}
		}
	}

	private double getMinimumScale() {
		// the minimum scale is determined by the maximum bounds, viewBoundsLimitOption, the current center
		// and the pixel size of the map
		
		if (maxBounds != null) {
			double wRatio = width / maxBounds.getWidth();
			double hRatio = height / maxBounds.getHeight();

			if (BoundsLimitOption.COMPLETELY_WITHIN_MAX_BOUNDS.equals(viewBoundsLimitOption)) {
				// return the maximum to fit outside
				return wRatio > hRatio ? wRatio : hRatio;
			} 
//			else {
//				/* return the smallest of the 2 ratio's */
//				return wRatio > hRatio ? hRatio : wRatio;
//			}
		} 
		return Double.MIN_VALUE;
	}

	private double getBestScale(Bbox bounds) {
		double wRatio;
		double boundsWidth = bounds.getWidth();
		if (boundsWidth <= 0) {
			wRatio = getMinimumScale();
		} else {
			wRatio = width / boundsWidth;
		}
		double hRatio;
		double boundsHeight = bounds.getHeight();
		if (boundsHeight <= 0) {
			hRatio = getMinimumScale();
		} else {
			hRatio = height / boundsHeight;
		}
		// return the minimum to fit inside
		return wRatio < hRatio ? wRatio : hRatio;
	}

	private double limitScale(double scale) {
		double minimumScale = getMinimumScale();
		if (scale < minimumScale) {
			return minimumScale;
		} else if (scale > maximumScale) {
			return maximumScale;
		} else {
			return scale;
		}
	}

	private IndexRange getResolutionRange() {
		IndexRange range = new IndexRange();
		double max = MAX_RESOLUTION;
		double minimumScale = getMinimumScale();
		if (minimumScale > 0) {
			max = 1.0 / getMinimumScale();
		}
		double min = 1.0 / maximumScale;
		for (int i = 0; i < resolutions.size(); i++) {
			Double resolution = resolutions.get(i);
			if (resolution >= min && resolution <= max) {
				range.setMin(i);
				range.setMax(i);
			}
		}
		return range;
	}

	private double getViewSpaceWidth() {
		return width / viewState.getScale();
	}

	private double getViewSpaceHeight() {
		return height / viewState.getScale();
	}

	/**
	 * Keeps a copy of the previous pan data so we can detect if we are panning.
	 */
	private void saveState() {
		this.lastViewState = viewState;
	}

	/**
	 * Fire an event.
	 *
	 * @param resized resized or not
	 * @param option zoom option
	 */
	private void fireEvent(boolean resized, ZoomOption option) {
		if (width > 0) {
			// keep old semantics of sameScaleLevel for api compatibility !
			boolean sameScale = lastViewState != null && viewState.isPannableFrom(lastViewState);
			handlerManager.fireEvent(new MapViewChangedEvent(getBounds(), getCurrentScale(), sameScale, viewState
					.isPanDragging(), resized, option));
		}
	}

	/**
	 * Finds an optimal scale by snapping to resolutions.
	 * 
	 * @param scale
	 *            scale which needs to be snapped
	 * @param option
	 *            snapping option
	 * @return snapped scale
	 */
	private double snapToResolution(double scale, ZoomOption option) {
		// clip upper bounds
		double allowedScale = limitScale(scale);
		if (resolutions != null) {
			IndexRange indices = getResolutionRange();
			if (option == ZoomOption.EXACT || !indices.isValid()) {
				// should not or cannot snap to resolutions
				return allowedScale;
			} else {
				// find the new index
				int newResolutionIndex = 0;
				double screenResolution = 1.0 / allowedScale;
				if (screenResolution >= resolutions.get(indices.getMin())) {
					newResolutionIndex = indices.getMin();
				} else if (screenResolution <= resolutions.get(indices.getMax())) {
					newResolutionIndex = indices.getMax();
				} else {
					for (int i = indices.getMin(); i < indices.getMax(); i++) {
						double upper = resolutions.get(i);
						double lower = resolutions.get(i + 1);
						if (screenResolution <= upper && screenResolution > lower) {
							if (option == ZoomOption.LEVEL_FIT) {
								newResolutionIndex = i;
								break;
							} else {
								if ((upper / screenResolution) > (screenResolution / lower)) {
									newResolutionIndex = i + 1;
									break;
								} else {
									newResolutionIndex = i;
									break;
								}
							}
						}
					}
				}
				// check if we need to change level
				if (newResolutionIndex == resolutionIndex && option == ZoomOption.LEVEL_CHANGE) {
					if (scale > viewState.getScale() && newResolutionIndex < indices.getMax()) {
						newResolutionIndex++;
					} else if (scale < viewState.getScale() && newResolutionIndex > indices.getMin()) {
						newResolutionIndex--;
					}
				}
				resolutionIndex = newResolutionIndex;
				return 1.0 / resolutions.get(resolutionIndex);
			}
		} else {
			return scale;
		}
	}

	/**
	 * Adjusts the center point of the map, to an allowed center point. This method tries to make sure the whole map
	 * extent is inside the maximum allowed bounds.
	 * 
	 * @param worldCenter world center
	 * @return new center
	 */
	private Coordinate calcCenterFromPoint(final Coordinate worldCenter) {
		double xCenter = worldCenter.getX();
		double yCenter = worldCenter.getY();
		if (maxBounds != null) {
			Coordinate minCoordinate = maxBounds.getOrigin();
			Coordinate maxCoordinate = maxBounds.getEndPoint();

			if (BoundsLimitOption.COMPLETELY_WITHIN_MAX_BOUNDS.equals(viewBoundsLimitOption)) {
				/** View must lay completely within maxBounds. **/
				double w = getViewSpaceWidth() / 2;
				double h = getViewSpaceHeight() / 2;
	
				if ((w * 2) > maxBounds.getWidth()) {
					xCenter = maxBounds.getCenterPoint().getX();
				} else {
					if ((xCenter - w) < minCoordinate.getX()) {
						xCenter = minCoordinate.getX() + w;
					}
					if ((xCenter + w) > maxCoordinate.getX()) {
						xCenter = maxCoordinate.getX() - w;
					}
				}
				if ((h * 2) > maxBounds.getHeight()) {
					yCenter = maxBounds.getCenterPoint().getY();
				} else {
					if ((yCenter - h) < minCoordinate.getY()) {
						yCenter = minCoordinate.getY() + h;
					}
					if ((yCenter + h) > maxCoordinate.getY()) {
						yCenter = maxCoordinate.getY() - h;
					}
				}
			} else if (BoundsLimitOption.CENTER_WITHIN_MAX_BOUNDS.equals(viewBoundsLimitOption)) {
				/** Center of view must lay within maxBounds. **/
				Coordinate center = new Coordinate(xCenter, yCenter);
				if (!maxBounds.contains(center)) {
					if (xCenter < minCoordinate.getX()) {
						xCenter = minCoordinate.getX();
					} else if (xCenter > maxCoordinate.getX()) {
						xCenter = maxCoordinate.getX();
					}
					if (yCenter < minCoordinate.getY()) {
						yCenter = minCoordinate.getY();
					} else if (yCenter > maxCoordinate.getY()) {
						yCenter = maxCoordinate.getY();
					}
				}
			}
		}
		return new Coordinate(xCenter, yCenter);
	}

	/**
	 * A range of indices.
	 */
	private class IndexRange {

		private Integer min;

		private Integer max;

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			if (this.max == null || max > this.max) {
				this.max = max;
			}
		}

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			if (this.min == null || min < this.min) {
				this.min = min;
			}
		}

		public boolean isValid() {
			return min != null && max != null && min <= max;
		}
	}
}
