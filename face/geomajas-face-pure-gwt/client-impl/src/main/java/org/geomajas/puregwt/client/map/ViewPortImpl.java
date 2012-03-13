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

package org.geomajas.puregwt.client.map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.ZoomStrategy.ZoomOption;

import com.google.inject.Inject;

/**
 * Implementation of the ViewPort interface.
 * 
 * @author Pieter De Graef
 */
public final class ViewPortImpl implements ViewPort {

	/** The map's width in pixels. */
	private int mapWidth;

	/** The map's height in pixels. */
	private int mapHeight;

	/** The maximum bounding box available to this MapView. Never go outside it! */
	private Bbox maxBounds;

	private MapEventBus eventBus;

	private String crs;

	private ZoomStrategy zoomStrategy;

	// Current viewing parameters:

	private double scale;

	private Coordinate position;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	@Inject
	private ViewPortImpl() {
		position = new Coordinate();
	}

	// -------------------------------------------------------------------------
	// Configuration stuff:
	// -------------------------------------------------------------------------

	public void initialize(ClientMapInfo mapInfo, MapEventBus eventBus) {
		this.eventBus = eventBus;
		crs = mapInfo.getCrs();

		// Calculate maximum bounds:
		maxBounds = new Bbox(mapInfo.getMaxBounds().getX(), mapInfo.getMaxBounds().getY(), mapInfo.getMaxBounds()
				.getWidth(), mapInfo.getMaxBounds().getHeight());

		// if the max bounds was not configured, take the union of initial and layer bounds
		if (BboxService.equals(maxBounds, Bbox.ALL, 1e-10)) {
			for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
				maxBounds = new Bbox(mapInfo.getInitialBounds().getX(), mapInfo.getInitialBounds().getY(), mapInfo
						.getInitialBounds().getWidth(), mapInfo.getInitialBounds().getHeight());
				if (layerInfo.getMaxExtent() != null) {
					maxBounds = BboxService.union(maxBounds, layerInfo.getMaxExtent());
				}
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
		return new Bbox(x, y, w, h);
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
		if (eventBus != null) {
			eventBus.fireEvent(new ViewPortTranslatedEvent(this));
		}
	}

	public void applyScale(double scale) {
		applyScale(scale, position);
	}

	public void applyScale(double newScale, Coordinate rescalePoint) {
		double limitedScale = zoomStrategy.checkScale(newScale, ZoomOption.LEVEL_CLOSEST);
		if (limitedScale != scale) {
			// Calculate theoretical new bounds. First create a BBOX of correct size:
			Bbox newBbox = new Bbox(0, 0, getMapWidth() / limitedScale, getMapHeight() / limitedScale);

			// Calculate translate vector to assure rescalePoint is on the same position as before.
			double factor = limitedScale / scale;
			double dX = (rescalePoint.getX() - position.getX()) * (1 - 1 / factor);
			double dY = (rescalePoint.getY() - position.getY()) * (1 - 1 / factor);

			// Apply translation to set the BBOX on the correct location:
			newBbox = BboxService.setCenterPoint(newBbox, new Coordinate(position.getX(), position.getY()));
			newBbox = BboxService.translate(newBbox, dX, dY);

			// Now apply on this view port:
			scale = limitedScale;
			position = checkPosition(BboxService.getCenterPoint(newBbox), scale);
			if (eventBus != null) {
				if (dX == 0 && dY == 0) {
					eventBus.fireEvent(new ViewPortScaledEvent(this));
				} else {
					eventBus.fireEvent(new ViewPortChangedEvent(this));
				}
			}
		}
	}

	public void applyBounds(Bbox bounds) {
		double newScale = getScaleForBounds(bounds);
		Coordinate tempPosition = checkPosition(BboxService.getCenterPoint(bounds), newScale);
		if (newScale == scale) {
			if (!position.equals(tempPosition)) {
				position = tempPosition;
				if (eventBus != null) {
					eventBus.fireEvent(new ViewPortTranslatedEvent(this));
				}
			}
		} else {
			position = tempPosition;
			scale = newScale;
			if (eventBus != null) {
				eventBus.fireEvent(new ViewPortChangedEvent(this));
			}
		}
	}

	// ------------------------------------------------------------------------
	// ViewPort transformation methods:
	// ------------------------------------------------------------------------

	public Coordinate transform(Coordinate coordinate, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return new Coordinate(coordinate);
					case WORLD:
						return screenToWorld(coordinate);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToScreen(coordinate);
					case WORLD:
						return new Coordinate(coordinate);
				}
		}
		return coordinate;
	}

	public Geometry transform(Geometry geometry, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return GeometryService.clone(geometry);
					case WORLD:
						return screenToWorld(geometry);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToScreen(geometry);
					case WORLD:
						return GeometryService.clone(geometry);
				}
		}
		return geometry;
	}

	public Bbox transform(Bbox bbox, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return new Bbox(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
					case WORLD:
						return screenToWorld(bbox);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToScreen(bbox);
					case WORLD:
						return new Bbox(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
				}
		}
		return bbox;
	}

	public Matrix getTransformationMatrix(RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return Matrix.IDENTITY;
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						if (scale > 0) {
							double dX = -(position.getX() * scale) + mapWidth / 2;
							double dY = position.getY() * scale + mapHeight / 2;
							return new Matrix(scale, 0, 0, -scale, dX, dY);
						}
						return new Matrix(1, 0, 0, 1, 0, 0);
					case WORLD:
						return Matrix.IDENTITY;
				}
		}
		return null;
	}

	public Matrix getTranslationMatrix(RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return Matrix.IDENTITY;
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						if (scale > 0) {
							double dX = -(position.getX() * scale) + mapWidth / 2;
							double dY = position.getY() * scale + mapHeight / 2;
							return new Matrix(1, 0, 0, 1, dX, dY);
						}
						return new Matrix(1, 0, 0, 1, 0, 0);
					case WORLD:
						return Matrix.IDENTITY;
				}
		}
		return null;
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
			Coordinate minCoordinate = BboxService.getOrigin(maxBounds);
			Coordinate maxCoordinate = BboxService.getEndPoint(maxBounds);

			if ((w * 2) > maxBounds.getWidth()) {
				xCenter = BboxService.getCenterPoint(maxBounds).getX();
			} else {
				if ((xCenter - w) < minCoordinate.getX()) {
					xCenter = minCoordinate.getX() + w;
				}
				if ((xCenter + w) > maxCoordinate.getX()) {
					xCenter = maxCoordinate.getX() - w;
				}
			}
			if ((h * 2) > maxBounds.getHeight()) {
				yCenter = BboxService.getCenterPoint(maxBounds).getY();
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

	// -------------------------------------------------------------------------
	// Private Transformation methods:
	// -------------------------------------------------------------------------

	private Coordinate worldToScreen(Coordinate coordinate) {
		if (coordinate != null) {
			double x = coordinate.getX() * scale;
			double y = -coordinate.getY() * scale;
			double translateX = -(position.getX() * scale) + (mapWidth / 2);
			double translateY = (position.getY() * scale) + (mapHeight / 2);
			x += translateX;
			y += translateY;
			return new Coordinate(x, y);
		}
		return null;
	}

	private Geometry worldToScreen(Geometry geometry) {
		if (geometry != null) {
			Geometry result = new Geometry(geometry.getGeometryType(), geometry.getSrid(), geometry.getPrecision());
			if (geometry.getGeometries() != null) {
				Geometry[] transformed = new Geometry[geometry.getGeometries().length];
				for (int i = 0; i < geometry.getGeometries().length; i++) {
					transformed[i] = worldToScreen(geometry.getGeometries()[i]);
				}
				result.setGeometries(transformed);
			}
			if (geometry.getCoordinates() != null) {
				Coordinate[] transformed = new Coordinate[geometry.getCoordinates().length];
				for (int i = 0; i < geometry.getCoordinates().length; i++) {
					transformed[i] = worldToScreen(geometry.getCoordinates()[i]);
				}
				result.setCoordinates(transformed);
			}
			return result;
		}
		throw new IllegalArgumentException("Cannot transform null geometry.");
	}

	private Bbox worldToScreen(Bbox bbox) {
		if (bbox != null) {
			Coordinate c1 = worldToScreen(BboxService.getOrigin(bbox));
			Coordinate c2 = worldToScreen(BboxService.getEndPoint(bbox));
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return new Bbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}

	private Coordinate screenToWorld(Coordinate coordinate) {
		if (coordinate != null) {
			double inverseScale = 1 / scale;
			double x = coordinate.getX() * inverseScale;
			double y = -coordinate.getY() * inverseScale;

			double w = mapWidth / scale;
			double h = mapHeight / scale;
			// -cam: center X axis around cam. +bbox.w/2: to place the origin in the center of the screen
			double translateX = -position.getX() + (w / 2);
			double translateY = -position.getY() - (h / 2); // Inverted Y-axis here...
			x -= translateX;
			y -= translateY;
			return new Coordinate(x, y);
		}
		return null;
	}

	private Geometry screenToWorld(Geometry geometry) {
		if (geometry != null) {
			Geometry result = new Geometry(geometry.getGeometryType(), geometry.getSrid(), geometry.getPrecision());
			if (geometry.getGeometries() != null) {
				Geometry[] transformed = new Geometry[geometry.getGeometries().length];
				for (int i = 0; i < geometry.getGeometries().length; i++) {
					transformed[i] = screenToWorld(geometry.getGeometries()[i]);
				}
				result.setGeometries(transformed);
			}
			if (geometry.getCoordinates() != null) {
				Coordinate[] transformed = new Coordinate[geometry.getCoordinates().length];
				for (int i = 0; i < geometry.getCoordinates().length; i++) {
					transformed[i] = screenToWorld(geometry.getCoordinates()[i]);
				}
				result.setCoordinates(transformed);
			}
			return result;
		}
		throw new IllegalArgumentException("Cannot transform null geometry.");
	}

	private Bbox screenToWorld(Bbox bbox) {
		if (bbox != null) {
			Coordinate c1 = screenToWorld(BboxService.getOrigin(bbox));
			Coordinate c2 = screenToWorld(BboxService.getEndPoint(bbox));
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return new Bbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}
}