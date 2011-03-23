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

package org.geomajas.puregwt.client.map;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleConfigurationInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.map.event.EventBus;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortDraggedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.Geometry;
import org.geomajas.puregwt.client.spatial.GeometryFactory;
import org.geomajas.puregwt.client.spatial.GeometryFactoryImpl;
import org.geomajas.puregwt.client.spatial.MatrixImpl;

/**
 * Implementation of the ViewPort interface.
 * 
 * @author Pieter De Graef
 */
public class ViewPortImpl implements ViewPort {

	private static final double MAX_RESOLUTION = Float.MAX_VALUE;

	private GeometryFactory factory;

	/** The map's width in pixels. */
	private int width;

	/** The map's height in pixels. */
	private int height;

	/** A maximum scale level, that this MapView is not allowed to cross. */
	private double maximumScale;

	/** The maximum bounding box available to this MapView. Never go outside it! */
	private Bbox maxBounds;

	/**
	 * A series of scale levels to which zooming in and out should snap. This is optional! If you which to use these
	 * fixed zooming steps, all you have to do, is define them.
	 */
	private List<Double> resolutions;

	/** The current index in the resolutions array. That is, if the resolutions are actually used. */
	private int resolutionIndex = -1;

	/** The current view state. */
	private ViewPortState viewState = new ViewPortState();

	private ViewPortTransformationService transformationService;

	private EventBus eventBus;

	private String crs;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public ViewPortImpl(EventBus eventBus) {
		this.eventBus = eventBus;
		factory = new GeometryFactoryImpl();
		transformationService = new ViewPortTransformationService(this);
	}

	// -------------------------------------------------------------------------
	// ViewPort implementation:
	// -------------------------------------------------------------------------

	public void initialize(ClientMapInfo mapInfo) {
		crs = mapInfo.getCrs();

		// Acquire maximum scale:
		ScaleConfigurationInfo scaleConfigurationInfo = mapInfo.getScaleConfiguration();
		maximumScale = scaleConfigurationInfo.getMaximumScale().getPixelPerUnit();

		// Get the full set of resolutions:
		resolutions = new ArrayList<Double>();
		for (ScaleInfo scale : scaleConfigurationInfo.getZoomLevels()) {
			resolutions.add(1. / scale.getPixelPerUnit());
		}
		maxBounds = factory.createBbox(mapInfo.getMaxBounds());
		Bbox initialBounds = factory.createBbox(mapInfo.getInitialBounds());
		// if the max bounds was not configured, take the union of initial and layer bounds
		Bbox all = factory.createBbox(org.geomajas.geometry.Bbox.ALL);
		if (maxBounds.equals(all)) {
			for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
				maxBounds = factory.createBbox(initialBounds);
				maxBounds = maxBounds.union(factory.createBbox(layerInfo.getMaxExtent()));
			}
		}
	}

	public double getScale() {
		return viewState.getScale();
	}

	/**
	 * Given the information in this ViewPort object, what is the currently visible area? This value is expressed in
	 * world coordinates.
	 * 
	 * @return Returns the bounding box that covers the currently visible area on the map.
	 */
	public Bbox getBounds() {
		double w = getViewSpaceWidth();
		double h = getViewSpaceHeight();
		double x = viewState.getX() - w / 2;
		double y = viewState.getY() - h / 2;
		return factory.createBbox(x, y, w, h);
	}

	public void applyPosition(Coordinate coordinate) {
		doSetOrigin(coordinate);
		eventBus.fireEvent(new ViewPortTranslatedEvent(this));
	}

	public void applyScale(double scale, ZoomOption option) {
		applyScale(scale, option, new Coordinate(viewState.getX(), viewState.getY()));
	}

	public void applyScale(double scale, ZoomOption option, Coordinate rescalePoint) {
		// calculate theoretical new bounds
		Bbox newBbox = factory.createBbox(0, 0, getMapWidth() / scale, getMapHeight() / scale);
		double factor = scale / getScale();

		// Calculate translate vector to assure rescalePoint is on the same position as before.
		double dX = (rescalePoint.getX() - viewState.getX()) * (1 - 1 / factor);
		double dY = (rescalePoint.getY() - viewState.getY()) * (1 - 1 / factor);

		newBbox.setCenterPoint(new Coordinate(viewState.getX(), viewState.getY()));
		newBbox.translate(dX, dY);
		boolean scaleChanged = doApplyBounds(newBbox, option);

		if (scaleChanged) {
			if (dX == 0 && dY == 0) {
				eventBus.fireEvent(new ViewPortScaledEvent(this));
			} else {
				eventBus.fireEvent(new ViewPortChangedEvent(this));
			}
		} else if (dX == 0 && dY == 0) {
			eventBus.fireEvent(new ViewPortTranslatedEvent(this));
		} // else, no event.
	}

	public void applyBounds(Bbox bounds, ZoomOption option) {
		boolean scaleChanged = doApplyBounds(bounds, option);
		if (scaleChanged) {
			eventBus.fireEvent(new ViewPortChangedEvent(this));
		} else {
			eventBus.fireEvent(new ViewPortTranslatedEvent(this));
		}
	}

	public Bbox getMaximumBounds() {
		return maxBounds;
	}

	public void translate(double x, double y) {
		doSetOrigin(new Coordinate(viewState.getX() + x, viewState.getY() + y));
		eventBus.fireEvent(new ViewPortTranslatedEvent(this));
	}

	public void scale(double delta, ZoomOption option) {
		applyScale(viewState.getScale() * delta, option);
	}

	public void scale(double delta, ZoomOption option, Coordinate center) {
		applyScale(viewState.getScale() * delta, option, center);
	}

	/**
	 * Drag the view on the map, without firing definitive ViewPortChanged events. This is used while dragging the map.
	 * Other than the events, it behaves the same as a translate.
	 * 
	 * @param x
	 *            Translation factor along the X-axis in world space.
	 * @param y
	 *            Translation factor along the Y-axis in world space.
	 */
	public void drag(double x, double y) {
		doSetOrigin(new Coordinate(viewState.getX() + x, viewState.getY() + y));
		eventBus.fireEvent(new ViewPortDraggedEvent(this));
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public Coordinate transform(Coordinate coordinate, RenderSpace from, RenderSpace to) {
		return transformationService.transform(coordinate, from, to);
	}

	public Geometry transform(Geometry geometry, RenderSpace from, RenderSpace to) {
		return transformationService.transform(geometry, from, to);
	}

	public Bbox transform(Bbox bbox, RenderSpace from, RenderSpace to) {
		return transformationService.transform(bbox, from, to);
	}

	public MatrixImpl getTransformationMatrix(RenderSpace from, RenderSpace to) {
		return transformationService.getTransformationMatrix(from, to);
	}

	public MatrixImpl getTranslationMatrix(RenderSpace from, RenderSpace to) {
		return transformationService.getTranslationMatrix(from, to);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public String getCrs() {
		return crs;
	}

	public int getMapWidth() {
		return width;
	}

	public int getMapHeight() {
		return height;
	}

	public Coordinate getPanOrigin() {
		return new Coordinate(viewState.getPanX(), viewState.getPanY());
	}

	public void setMapSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	protected ViewPortState getViewPortState() {
		return viewState;
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

	private boolean doApplyBounds(Bbox bounds, ZoomOption option) {
		boolean scaleChanged = false;
		if (bounds != null && !bounds.isEmpty()) {
			// first set the scale, taking minimum and maximum scale into account
			double scale = getBestScale(bounds);
			scale = snapToResolution(scale, option);
			scaleChanged = doSetScale(scale, option);

			// now translate, taking maximum bounds into account
			doSetOrigin(bounds.getCenterPoint());
			viewState = viewState.copyAndSetPanOrigin(viewState.getX(), viewState.getY());
		}
		return scaleChanged;
	}

	private double getMinimumScale() {
		// the minimum scale is determined by the maximum bounds and the pixel size of the map
		if (maxBounds != null) {
			double wRatio = width / maxBounds.getWidth();
			double hRatio = height / maxBounds.getHeight();
			// return the maximum to fit outside
			return wRatio > hRatio ? wRatio : hRatio;
		} else {
			return Double.MIN_VALUE;
		}
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