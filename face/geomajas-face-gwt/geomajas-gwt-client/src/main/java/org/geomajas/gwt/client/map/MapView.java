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

package org.geomajas.gwt.client.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;

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

	/** The map's width in pixels. */
	private int width;

	/** The map's height in pixels. */
	private int height;

	/** A maximum scale level, that this MapView is not allowed to cross. */
	private double maximumScale = 10;

	/** The maximum bounding box available to this MapView. Never go outside it! */
	private Bbox maxBounds;

	/**
	 * A series of scale levels to which zooming in and out should snap. This is optional! If you which to use these
	 * fixed zooming steps, all you have to do, is define them.
	 */
	private List<Double> resolutions = new ArrayList<Double>();

	/** The current index in the resolutions array. That is, if the resolutions are actually used. */
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

	/** Return the world-to-view space transformation matrix. */
	public Matrix getWorldToViewTransformation() {
		if (viewState.getScale() > 0) {
			double dX = -(viewState.getX() * viewState.getScale()) + width / 2;
			double dY = viewState.getY() * viewState.getScale() + height / 2;
			return new Matrix(viewState.getScale(), 0, 0, -viewState.getScale(), dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/** Return the world-to-view space translation matrix. */
	public Matrix getWorldToViewTranslation() {
		if (viewState.getScale() > 0) {
			double dX = -(viewState.getX() * viewState.getScale()) + width / 2;
			double dY = viewState.getY() * viewState.getScale() + height / 2;
			return new Matrix(1, 0, 0, 1, dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/** Return the world-to-pan space translation matrix. */
	public Matrix getWorldToPanTransformation() {
		if (viewState.getScale() > 0) {
			double dX = -(viewState.getX() * viewState.getScale());
			double dY = viewState.getY() * viewState.getScale();
			return new Matrix(viewState.getScale(), 0, 0, -viewState.getScale(), dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/** Return the translation of coordinates relative to the pan origin to view coordinates. */
	public Matrix getPanToViewTranslation() {
		if (viewState.getScale() > 0) {
			double dX = -((viewState.getX() - viewState.getPanX()) * viewState.getScale())
					+ width / 2;
			double dY = (viewState.getY() - viewState.getPanY()) * viewState.getScale()
					+ height / 2;
			return new Matrix(1, 0, 0, 1, dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/** Return the translation of scaled world coordinates to coordinates relative to the pan origin. */
	public Matrix getWorldToPanTranslation() {
		if (viewState.getScale() > 0) {
			double dX = -(viewState.getPanX() * viewState.getScale());
			double dY = viewState.getPanY() * viewState.getScale();
			return new Matrix(1, 0, 0, 1, dX, dY);
		}
		return new Matrix(1, 0, 0, 1, 0, 0);
	}

	/** Return the world-to-view space translation matrix. */
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
	 *            zoom option, {@link org.geomajas.gwt.client.map.MapView.ZoomOption}
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
	 *            zoom option, {@link org.geomajas.gwt.client.map.MapView.ZoomOption}
	 * @param rescalePoint
	 *            After zooming, this point will still be on the same position in the view as before.
	 */
	public void setCurrentScale(final double newScale, final ZoomOption option, final Coordinate rescalePoint) {
		saveState();
		// calculate theoretical new bounds
		Bbox newBbox = new Bbox(0, 0, getWidth() / newScale, getHeight() / newScale);

		double factor = newScale / getCurrentScale();

		// Calculate translate vector to assure rescalePoint is on the same position as before.
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
	 *            zoom option, {@link org.geomajas.gwt.client.map.MapView.ZoomOption}
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
		Bbox oldbbox = getBounds();
		this.width = newWidth;
		this.height = newHeight;

		// Use the same center point for the new bounds, but don't zoom in or out.
		doSetOrigin(oldbbox.getCenterPoint());
		fireEvent(false, null);
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
	 * @param delta
	 *            Adjust the scale by factor "delta".
	 */
	public void scale(double delta, ZoomOption option) {
		setCurrentScale(viewState.getScale() * delta, option);
	}

	/**
	 * Adjust the current scale on the map by a new factor, keeping a coordinate in place.
	 * 
	 * @param delta
	 *            Adjust the scale by factor "delta".
	 * @param center
	 *            Keep this coordinate on the same position as before.
	 * 
	 */
	public void scale(double delta, ZoomOption option, Coordinate center) {
		setCurrentScale(viewState.getScale() * delta, option, center);
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	/** Return the current scale. */
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

	/** Get the list of predefined map resolutions (resolution = inverse of scale). */
	public List<Double> getResolutions() {
		return resolutions;
	}

	/** Return the transformer that is used to transform coordinate and geometries between world and screen space. */
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

	private boolean doSetScale(double scale, ZoomOption option) {
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
			boolean scaleChanged = false;
			if (!bounds.isEmpty()) {
				// find best scale
				double scale = getBestScale(bounds);
				// snap and limit
				scale = snapToResolution(scale, option);
				// set scale
				scaleChanged = doSetScale(scale, option);
			}
			doSetOrigin(bounds.getCenterPoint());
			if (bounds.isEmpty()) {
				fireEvent(false, null);
			} else {
				// set pan origin equal to origin
				viewState = viewState.copyAndSetPanOrigin(viewState.getX(), viewState.getY());
				fireEvent(scaleChanged, option);
			}
		}
	}

	private double getMinimumScale() {
		return Double.MIN_VALUE;
		// if (maxBounds != null) {
		// double wRatio = width / (maxBounds.getWidth() * 2);
		// double hRatio = height / (maxBounds.getHeight() * 2);
		// // return the maximum to fit outside
		// return wRatio > hRatio ? wRatio : hRatio;
		// } else {
		// return Double.MIN_VALUE;
		// }
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
	 * keeps a copy of the previous pan data so we can detect if we are panning.
	 * 
	 * @see #isPanning()
	 */
	private void saveState() {
		this.lastViewState = viewState;
	}

	/** Fire an event. */
	private void fireEvent(boolean resized, ZoomOption option) {
		boolean sameScale = lastViewState != null && viewState.isSameScale(lastViewState);
		handlerManager.fireEvent(new MapViewChangedEvent(getBounds(), getCurrentScale(), sameScale, viewState
				.isPanDragging(), resized, option));
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
			IndexRange indexes = getResolutionRange();
			if (option == ZoomOption.EXACT || !indexes.isValid()) {
				// should not or cannot snap to resolutions
				return allowedScale;
			} else {
				// find the new index
				int newResolutionIndex = 0;
				double screenResolution = 1.0 / allowedScale;
				if (screenResolution >= resolutions.get(indexes.getMin())) {
					newResolutionIndex = indexes.getMin();
				} else if (screenResolution <= resolutions.get(indexes.getMax())) {
					newResolutionIndex = indexes.getMax();
				} else {
					for (int i = indexes.getMin(); i < indexes.getMax(); i++) {
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
					if (scale > viewState.getScale() && newResolutionIndex < indexes.getMax()) {
						newResolutionIndex++;
					} else if (scale < viewState.getScale() && newResolutionIndex > indexes.getMin()) {
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
	 * @param worldCenter
	 * @return
	 */
	private Coordinate calcCenterFromPoint(final Coordinate worldCenter) {
		double xCenter = worldCenter.getX();
		double yCenter = worldCenter.getY();
		if (maxBounds != null) {
			double w = getViewSpaceWidth() / 2;
			double h = getViewSpaceHeight() / 2;
			Coordinate minCoordinate = maxBounds.getOrigin();
			Coordinate maxCoordinate = maxBounds.getEndPoint();

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
		}
		return new Coordinate(xCenter, yCenter);
	}

	/**
	 * A range of indexes.
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
