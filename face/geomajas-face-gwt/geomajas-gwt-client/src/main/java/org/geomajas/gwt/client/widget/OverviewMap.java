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

package org.geomajas.gwt.client.widget;

import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.OverviewMapController;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;

/**
 * MapWidget that listens to another MapWidget and shows an overview of it.
 * 
 * @author Kristof Heirwegh, Pieter De Graef
 */
@Api
public class OverviewMap extends MapWidget implements MapViewChangedHandler {
	
	private static final String TARGET_RETICLE_IMAGE = "geomajas/widget/target.gif";

	/** Reference to the main map, that this overview map is to follow. */
	private MapWidget targetMap;

	/** Should the overview map zoom in/out according to targetMap? */
	private boolean dynamicOverview = true;

	private Rectangle targetRectangle;

	private GfxGeometry targetMaxExtentRectangle;

	private Image targetReticle;

	private ShapeStyle rectangleStyle;

	private ShapeStyle targetMaxExtentRectangleStyle;

	/**
	 * Use the max extent to boot up this overview map (true), or use the default initial bounds from the configuration
	 * (false). The map extent is calculated from the map's layers.
	 */
	private boolean useMaxExtent;

	/** Draw the borders of the target map's maximum extent? */
	private boolean drawTargetMaxExtent;

	/**
	 * Constructor the an overview map. The scale bar, panning buttons and zoomOnScroll are automatically disabled. Also
	 * a {@link OverviewMapController} is automatically set.
	 * 
	 * @param id
	 *            This map's unique identifier. Must resemble an ID from the XML configurations, so it can initialize
	 *            itself.
	 * @param applicationId
	 *            This application's unique identifier. Must resemble an ID from the XML configurations, so it can
	 *            initialize itself.
	 * @param targetMap
	 *            Reference to the main map, that this overview map is to follow.
	 * @param useMaxExtent
	 *            Use the max extent to boot up this overview map (true), or use the default initial bounds from the
	 *            configuration (false). The map extent is calculated from the map's layers.
	 * @param drawTargetMaxExtent
	 *            Draw the borders of the target map's maximum extent?
	 */
	public OverviewMap(String id, String applicationId, MapWidget targetMap, boolean useMaxExtent,
			boolean drawTargetMaxExtent) {
		super(id, applicationId);
		if (null == targetMap) {
			throw new IllegalArgumentException("Please provide a targetmap");
		}
		this.targetMap = targetMap;
		this.useMaxExtent = useMaxExtent;
		this.drawTargetMaxExtent = drawTargetMaxExtent;
		targetMap.getMapModel().getMapView().addMapViewChangedHandler(this);
		scaleBarEnabled = false;
		navigationAddonEnabled = false;
		rectangleStyle = new ShapeStyle("#FF9900", 0.2f, "#FF9900", 1f, 2);
		targetMaxExtentRectangleStyle = new ShapeStyle("#555555", 0.4f, "#555555", 1f, 1);
		setZoomOnScrollEnabled(false);
		targetMap.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				updateMaxExtent();
			}
		});
		addResizedHandler(new ResizedHandler() {

			public void onResized(ResizedEvent event) {
				updateMaxExtent();
			}
		});
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Does not call it's super, because we don't want the OverViewMap to constantly redraw. Instead the POV is updated.
	 * This updating can possible have a redraw as a consequence, when the target map is moving out of range of the
	 * overview map. Note that this can only happen when <code>dynamicOverview</code> is true, and the OverViewMap has
	 * started using it's initial bounds (not max extents).
	 */
	public void onMapViewChanged(MapViewChangedEvent event) {
		// No call to super; we don't wan't the OverViewMap to constantly redraw!
		updatePov();
	}

	public MapWidget getTargetMap() {
		return targetMap;
	}

	public boolean isDynamicOverview() {
		return dynamicOverview;
	}

	/**
	 * Should the overview map zoom in/out according to targetMap?
	 * 
	 * @param dynamicOverview
	 */
	public void setDynamicOverview(boolean dynamicOverview) {
		this.dynamicOverview = dynamicOverview;
	}

	/**
	 * Move the Point of view (rectangle or reticle) to a new location. This does not update the target map!
	 * 
	 * @param deltaX
	 *            horizontal amount the pov should be moved from current location: screen view coordinate!
	 * @param deltaY
	 *            vertical amount the pov should be moved from current location: screen view coordinate!
	 */
	public void movePov(double deltaX, double deltaY) {
		if (null != targetRectangle) {
			targetRectangle.getBounds().translate(deltaX, deltaY);
			render(targetRectangle, RenderGroup.SCREEN, RenderStatus.UPDATE);

		} else if (null != targetReticle) {
			targetReticle.getBounds().translate(deltaX, deltaY);
			render(targetReticle, RenderGroup.SCREEN, RenderStatus.UPDATE);
		}
		// else not initialized
	}

	/**
	 * Pan the target map to a new location.
	 * <p>
	 * You do not need to use movePov(), this will be done through the event-listener.
	 * 
	 * @param deltaX
	 *            horizontal amount the target map should be moved from current location: world view coordinate!
	 * @param deltaY
	 *            vertical amount the target map should be moved from current location: world view coordinate!
	 */
	public void panTargetMap(double deltaX, double deltaY) {
		targetMap.getMapModel().getMapView().translate(deltaX, deltaY);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public ShapeStyle getRectangleStyle() {
		return rectangleStyle;
	}

	/**
	 * Set a new style for the rectangle that shows the current position on the target map.
	 * 
	 * @param rectangleStyle
	 */
	public void setRectangleStyle(ShapeStyle rectangleStyle) {
		this.rectangleStyle = rectangleStyle;
		if (targetRectangle != null) {
			targetRectangle.setStyle(rectangleStyle);
			render(targetRectangle, RenderGroup.SCREEN, RenderStatus.ALL);
		}
	}

	public boolean isDrawTargetMaxExtent() {
		return drawTargetMaxExtent;
	}

	/**
	 * Determine whether or not a rectangle that shows the target map's maximum extent, should be shown.
	 * 
	 * @param drawTargetMaxExtent
	 */
	public void setDrawTargetMaxExtent(boolean drawTargetMaxExtent) {
		this.drawTargetMaxExtent = drawTargetMaxExtent;

		// Immediately draw or remove the max extent rectangle:
		if (drawTargetMaxExtent) {
			targetMaxExtentRectangle = new GfxGeometry("targetMaxExtentRectangle");
			targetMaxExtentRectangle.setStyle(targetMaxExtentRectangleStyle);

			// Use the targetMap bounds, no this map's bounds!!!
			Bbox targetMaxBounds = targetMap.getMapModel().getMapView().getMaxBounds();
			if (null == targetMaxBounds) {
				targetMaxBounds = new Bbox(targetMap.getMapModel().getLayers().get(0).getLayerInfo().getMaxExtent());
			}

			Bbox box = getMapModel().getMapView().getWorldViewTransformer().worldToView(targetMaxBounds);
			LinearRing shell = getMapModel().getGeometryFactory().createLinearRing(
					new Coordinate[] { new Coordinate(-2, -2), new Coordinate(getWidth() + 2, -2),
							new Coordinate(getWidth() + 2, getHeight() + 2), new Coordinate(-2, getHeight() + 2) });
			LinearRing hole = getMapModel().getGeometryFactory().createLinearRing(
					new Coordinate[] { new Coordinate(box.getX(), box.getY()),
							new Coordinate(box.getMaxX(), box.getY()), new Coordinate(box.getMaxX(), box.getMaxY()),
							new Coordinate(box.getX(), box.getMaxY()) });
			Polygon polygon = getMapModel().getGeometryFactory().createPolygon(shell, new LinearRing[] { hole });
			targetMaxExtentRectangle.setGeometry(polygon);
			render(targetMaxExtentRectangle, RenderGroup.SCREEN, RenderStatus.ALL);
		} else {
			render(targetMaxExtentRectangle, RenderGroup.SCREEN, RenderStatus.DELETE);
			targetMaxExtentRectangle = null;
		}
	}

	public ShapeStyle getTargetMaxExtentRectangleStyle() {
		return targetMaxExtentRectangleStyle;
	}

	/**
	 * Set a new style for the rectangle that show the target map's maximum extent. This style will be applied
	 * immediately.
	 * 
	 * @param targetMaxExtentRectangleStyle
	 */
	public void setTargetMaxExtentRectangleStyle(ShapeStyle targetMaxExtentRectangleStyle) {
		this.targetMaxExtentRectangleStyle = targetMaxExtentRectangleStyle;
		if (targetMaxExtentRectangle != null) {
			targetMaxExtentRectangle.setStyle(targetMaxExtentRectangleStyle);
			if (drawTargetMaxExtent) {
				render(targetMaxExtentRectangle, RenderGroup.SCREEN, RenderStatus.UPDATE);
			}
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Override the initialize method, to set a controller, and perhaps apply the target map's max extent.
	 */
	@Override
	protected void initializationCallback(GetMapConfigurationResponse r) {
		super.initializationCallback(r);
		setController(new OverviewMapController(this));
		updateMaxExtent();
	}

	/**
	 * Apply a maximum extent. This will take the target map's maximum extent (actually 5% bigger)
	 */
	private void updateMaxExtent() {
		if (useMaxExtent && targetMap.getMapModel().isInitialized()) {
			MapView mapView = getMapModel().getMapView();

			// Calculate the map extent from the target map:
			Bbox targetMaxBounds = targetMap.getMapModel().getMapView().getMaxBounds();

			targetMaxBounds = targetMaxBounds.buffer(targetMaxBounds.getWidth() / 20);

			// Set the maxBounds on this map as well:
			mapView.setMaxBounds(targetMaxBounds.buffer(targetMaxBounds.getWidth()));

			// Then apply the map extent:
			mapView.applyBounds(targetMaxBounds, MapView.ZoomOption.LEVEL_FIT);
			super.onMapViewChanged(null);

			// Immediately draw or remove the max extent rectangle:
			setDrawTargetMaxExtent(drawTargetMaxExtent);
		}
	}

	/**
	 * Update the rectangle, and perhaps the entire map if needed.
	 */
	private void updatePov() {
		MapView mapView = getMapModel().getMapView();
		WorldViewTransformer transformer = new WorldViewTransformer(mapView);
		Bbox targetBox = targetMap.getMapModel().getMapView().getBounds();
		Bbox overviewBox = mapView.getBounds();

		// check if bounds are valid
		if (Double.isNaN(overviewBox.getX())) {
			return;
		}

		// zoom if current view is too small
		if (dynamicOverview && !overviewBox.contains(targetBox)) {
			// mapView.applyBounds(overviewBox.union(targetBox), MapView.ZoomOption.LEVEL_FIT);
			// super.onMapViewChanged(null);
		}

		// calculate boxSize
		Coordinate viewBegin = transformer.worldToView(targetBox.getOrigin());
		Coordinate viewEnd = transformer.worldToView(targetBox.getEndPoint());

		double width = Math.abs(viewEnd.getX() - viewBegin.getX());
		double height = Math.abs(viewEnd.getY() - viewBegin.getY());
		viewBegin.setY(viewBegin.getY() - height);

		// show recticle or box
		if (width < 20) {
			if (null != targetRectangle) {
				render(targetRectangle, RenderGroup.SCREEN, RenderStatus.DELETE);
				targetRectangle = null;
			}
			if (null == targetReticle) {
				targetReticle = new Image("targetReticle");
				targetReticle.setHref(Geomajas.getIsomorphicDir() + TARGET_RETICLE_IMAGE);
				targetReticle.setBounds(new Bbox(0, 0, 21, 21));
			}
			double x = viewBegin.getX() + (width / 2) - 10;
			double y = viewBegin.getY() + (width / 2) - 10;
			targetReticle.getBounds().setX(x);
			targetReticle.getBounds().setY(y);
			render(targetReticle, RenderGroup.SCREEN, RenderStatus.UPDATE);

		} else {
			if (null != targetReticle) {
				render(targetReticle, RenderGroup.SCREEN, RenderStatus.DELETE);
				targetReticle = null;
			}
			if (null == targetRectangle) {
				targetRectangle = new Rectangle("targetRect");
				targetRectangle.setStyle(rectangleStyle);
			}
			targetRectangle.setBounds(new Bbox(viewBegin.getX(), viewBegin.getY(), width, height));
			render(targetRectangle, RenderGroup.SCREEN, RenderStatus.UPDATE);
		}
	}
}