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

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.map.ZoomStrategy.ZoomOption;
import org.geomajas.puregwt.client.map.event.EventBus;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
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

	private GeometryFactory factory;

	/** The map's width in pixels. */
	private int mapWidth;

	/** The map's height in pixels. */
	private int mapHeight;

	/** The maximum bounding box available to this MapView. Never go outside it! */
	private Bbox maxBounds;

	private ViewPortTransformationService transformationService;

	private EventBus eventBus;

	private String crs;

	private ZoomStrategy zoomStrategy;

	// Current viewing parameters:

	private double scale;

	private Coordinate position;

	private Coordinate dragOrigin;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public ViewPortImpl(EventBus eventBus) {
		if (eventBus == null) {
			throw new NullPointerException("EventBus should not be null.");
		}
		this.eventBus = eventBus;
		factory = new GeometryFactoryImpl();
		transformationService = new ViewPortTransformationService(this);
		dragOrigin = new Coordinate();
		position = new Coordinate();
	}

	// -------------------------------------------------------------------------
	// Configuration stuff:
	// -------------------------------------------------------------------------

	public void initialize(ClientMapInfo mapInfo) {
		crs = mapInfo.getCrs();

		// Calculate maximum bounds:
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

		// Set the best zoom strategy given the map info:
		if (mapInfo.getScaleConfiguration().getZoomLevels() != null
				&& mapInfo.getScaleConfiguration().getZoomLevels().size() > 0) {
			zoomStrategy = new FixedStepZoomStrategy(mapInfo, maxBounds);
		} else {
			zoomStrategy = new FreeForAllZoomStrategy(mapInfo, maxBounds);
		}
		zoomStrategy.setMapSize(mapWidth, mapHeight);
	}

	public Bbox getMaximumBounds() {
		return maxBounds;
	}

	public void setMapSize(int width, int height) {
		position = transform(new Coordinate(width / 2, height / 2), RenderSpace.SCREEN, RenderSpace.WORLD);
		this.mapWidth = width;
		this.mapHeight = height;
		if (zoomStrategy != null) {
			zoomStrategy.setMapSize(width, height);
		}
	}

	public String getCrs() {
		return crs;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	// -------------------------------------------------------------------------
	// Methods that retrieve what is visible on the map:
	// -------------------------------------------------------------------------

	public Coordinate getPanOrigin() {
		return new Coordinate(dragOrigin);
	}

	public Coordinate getPosition() {
		return new Coordinate(position);
	}

	public double getScale() {
		return scale;
	}

	/**
	 * Given the information in this ViewPort object, what is the currently visible area? This value is expressed in
	 * world coordinates.
	 * 
	 * @return Returns the bounding box that covers the currently visible area on the map.
	 */
	public Bbox getBounds() {
		double w = mapWidth / scale;
		double h = mapHeight / scale;
		double x = position.getX() - w / 2;
		double y = position.getY() - h / 2;
		return factory.createBbox(x, y, w, h);
	}

	// -------------------------------------------------------------------------
	// Methods that manipulate what is visible on the map:
	// -------------------------------------------------------------------------

	public ZoomStrategy getZoomStrategy() {
		return zoomStrategy;
	}

	public void setZoomStrategy(ZoomStrategy zoomStrategy) {
		this.zoomStrategy = zoomStrategy;
	}

	public void applyPosition(Coordinate coordinate) {
		position = checkPosition(coordinate, scale);
		eventBus.fireEvent(new ViewPortTranslatedEvent(this));
	}

	public void applyScale(double scale) {
		applyScale(scale, position);
	}

	public void applyScale(double newScale, Coordinate rescalePoint) {
		double limitedScale = zoomStrategy.checkScale(newScale, ZoomOption.LEVEL_CLOSEST);
		if (limitedScale != scale) {
			// Calculate theoretical new bounds. First create a BBOX of correct size:
			Bbox newBbox = factory.createBbox(0, 0, getMapWidth() / limitedScale, getMapHeight() / limitedScale);

			// Calculate translate vector to assure rescalePoint is on the same position as before.
			double factor = limitedScale / scale;
			double dX = (rescalePoint.getX() - position.getX()) * (1 - 1 / factor);
			double dY = (rescalePoint.getY() - position.getY()) * (1 - 1 / factor);

			// Apply translation to set the BBOX on the correct location:
			newBbox.setCenterPoint(new Coordinate(position.getX(), position.getY()));
			newBbox.translate(dX, dY);

			// Now apply on this view port:
			scale = limitedScale;
			position = newBbox.getCenterPoint();
			if (dX == 0 && dY == 0) {
				eventBus.fireEvent(new ViewPortScaledEvent(this));
			} else {
				eventBus.fireEvent(new ViewPortChangedEvent(this));
			}
		}
	}

	public void applyBounds(Bbox bounds) {
		double newScale = getScaleForBounds(bounds);
		Coordinate tempPosition = checkPosition(bounds.getCenterPoint(), newScale);
		if (newScale == scale) {
			if (!position.equals(tempPosition)) {
				position = tempPosition;
				eventBus.fireEvent(new ViewPortTranslatedEvent(this));
			}
		} else {
			position = tempPosition;
			scale = newScale;
			eventBus.fireEvent(new ViewPortChangedEvent(this));
		}
	}

	// ------------------------------------------------------------------------
	// ViewPort transformation methods:
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

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	private double getScaleForBounds(Bbox bounds) {
		double wRatio;
		double boundsWidth = bounds.getWidth();
		if (boundsWidth <= 0) {
			wRatio = zoomStrategy.getMinimumScale();
		} else {
			wRatio = mapWidth / boundsWidth;
		}
		double hRatio;
		double boundsHeight = bounds.getHeight();
		if (boundsHeight <= 0) {
			hRatio = zoomStrategy.getMinimumScale();
		} else {
			hRatio = mapHeight / boundsHeight;
		}
		// Return the checked scale for the minimum to fit inside:
		return zoomStrategy.checkScale(wRatio < hRatio ? wRatio : hRatio, ZoomOption.LEVEL_CLOSEST);
	}

	private Coordinate checkPosition(final Coordinate newPosition, final double newScale) {
		double xCenter = newPosition.getX();
		double yCenter = newPosition.getY();
		if (maxBounds != null) {
			double w = mapWidth / (newScale * 2);
			double h = mapHeight / (newScale * 2);
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
}