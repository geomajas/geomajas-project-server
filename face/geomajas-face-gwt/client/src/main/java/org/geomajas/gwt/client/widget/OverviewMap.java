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

package org.geomajas.gwt.client.widget;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.OverviewMapController;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.widget.event.GraphicsReadyEvent;
import org.geomajas.gwt.client.widget.event.GraphicsReadyHandler;

/**
 * MapWidget that listens to another MapWidget and shows an overview of it.
 * 
 * @author Kristof Heirwegh
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api
public class OverviewMap extends MapWidget {

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
	private boolean useTargetMaxExtent;

	private int maxExtentIncreasePercentage = 5;

	/** Draw the borders of the target map's maximum extent? */
	private boolean drawTargetMaxExtent;

	/**
	 * Constructor the an overview map. The scale bar, panning buttons and zoomOnScroll are automatically disabled. Also
	 * a {@link OverviewMapController} is automatically set.
	 * 
	 * @param id
	 *            Overview map ID from the configurations. Will also be use as widget id.
	 * @param applicationId
	 *            This application's unique identifier. Must resemble an ID from the XML configurations, so it can
	 *            initialize itself.
	 * @param targetMap
	 *            Reference to the main map, that this overview map has to follow.
	 * @param useTargetMaxExtent
	 *            Use the max extent to boot up this overview map (true), or use the default initial bounds from the
	 *            configuration (false).
	 * @param drawTargetMaxExtent
	 *            Draw the borders of the target map's maximum extent?
	 * @since 1.8.0
	 */
	@Api
	public OverviewMap(String id, String applicationId, MapWidget targetMap, boolean useTargetMaxExtent,
			boolean drawTargetMaxExtent) {
		super(id, applicationId, false);
		if (null == targetMap) {
			throw new IllegalArgumentException("Please provide a targetmap");
		}
		this.targetMap = targetMap;
		this.useTargetMaxExtent = useTargetMaxExtent;
		this.drawTargetMaxExtent = drawTargetMaxExtent;
		targetMap.getMapModel().getMapView().addMapViewChangedHandler(new UpdatePointOfViewHandler());
		scaleBarEnabled = false;
		navigationAddonEnabled = false;
		rectangleStyle = new ShapeStyle("#FF9900", 0.2f, "#FF9900", 1f, 2);
		targetMaxExtentRectangleStyle = new ShapeStyle("#555555", 0.4f, "#555555", 1f, 1);
		setZoomOnScrollEnabled(false);
		// handle max extent when both maps are loaded
		MaxExtentHandler meh = new MaxExtentHandler();
		getMapModel().addMapModelChangedHandler(meh);
		targetMap.getMapModel().addMapModelChangedHandler(meh);
		// handle max extent on resize
		getGraphics().addGraphicsReadyHandler(new GraphicsReadyHandler() {
			
			public void onReady(GraphicsReadyEvent event) {
				if (getMapModel().isInitialized()) {
					updateMaxExtent();
				}
			}
		});
		setController(new OverviewMapController(this));
		getMapModel().init();
	}

	/**
	 * Constructor the an overview map. The scale bar, panning buttons and zoomOnScroll are automatically disabled. Also
	 * a {@link OverviewMapController} is automatically set.
	 * 
	 * @param id
	 *            Overview map ID from the configurations. Will also be use as widget id.
	 * @param applicationId
	 *            This application's unique identifier. Must resemble an ID from the XML configurations, so it can
	 *            initialize itself.
	 * @param targetMap
	 *            Reference to the main map, that this overview map has to follow.
	 * @param useTargetMaxExtent
	 *            Use the max extent to boot up this overview map (true), or use the default initial bounds from the
	 *            configuration (false).
	 * @param drawTargetMaxExtent
	 *            Draw the borders of the target map's maximum extent?
	 * @param maxExtentIncreasePercentage
	 *            Percentage of border which should be displayed around the max extent in the overview map.
	 * @since 1.8.0
	 */
	@Api
	public OverviewMap(String id, String applicationId, MapWidget targetMap, boolean useTargetMaxExtent,
			boolean drawTargetMaxExtent, int maxExtentIncreasePercentage) {
		this(id, applicationId, targetMap, useTargetMaxExtent, drawTargetMaxExtent);
		setMaxExtentIncreasePercentage(maxExtentIncreasePercentage);
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the linked map for this overview map.
	 *
	 * @return linked map
	 */
	public MapWidget getTargetMap() {
		return targetMap;
	}

	/**
	 * Does the overview map zoom in/out according to targetMap?
	 *
	 * @return true when overview map zooms in and out with linked map
	 */
	public boolean isDynamicOverview() {
		return dynamicOverview;
	}

	/**
	 * Should the overview map zoom in/out according to targetMap?
	 * 
	 * @param dynamicOverview
	 *            true when overview map needs to zoom in/out with target map
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

	/**
	 * Set the percentage to increase the map maxExtent.
	 * 
	 * @param maxExtentIncreasePercentage
	 *            presentage to increase the maxExtent
	 * @since 1.8.0
	 */
	@Api
	public void setMaxExtentIncreasePercentage(int maxExtentIncreasePercentage) {
		this.maxExtentIncreasePercentage = maxExtentIncreasePercentage;
	}

	public ShapeStyle getRectangleStyle() {
		return rectangleStyle;
	}

	/**
	 * Set a new style for the rectangle that shows the current position on the target map.
	 * 
	 * @param rectangleStyle
	 *            rectangle style
	 * @since 1.8.0
	 */
	@Api
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
	 * Determine whether or not a rectangle that shows the target map's maximum extent should be shown.
	 * 
	 * @param drawTargetMaxExtent
	 *            should the max extent be marked on the map?
	 * @since 1.8.0
	 */
	@Api
	public void setDrawTargetMaxExtent(boolean drawTargetMaxExtent) {
		this.drawTargetMaxExtent = drawTargetMaxExtent;

		// Immediately draw or remove the max extent rectangle:
		if (drawTargetMaxExtent) {
			targetMaxExtentRectangle = new GfxGeometry("targetMaxExtentRectangle");
			targetMaxExtentRectangle.setStyle(targetMaxExtentRectangleStyle);

			Bbox targetMaxExtent = getOverviewMaxBounds();

			Bbox box = getMapModel().getMapView().getWorldViewTransformer().worldToView(targetMaxExtent);
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
	 *            max extent marker rectangle style
	 * @since 1.8.0
	 */
	@Api
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
	 * Override the refresh method, to perhaps apply the target map's max extent.
	 */
	@Override
	public void refreshCallback(ClientMapInfo info) {
		super.refreshCallback(info);
		updateMaxExtent();
	}

	/**
	 * Apply a maximum extent. This will extend the map extend to be used by the percentage to increase.
	 */
	private void updateMaxExtent() {
		if (targetMap.getMapModel().isInitialized() && getGraphics().isReady()) {
			// map view size may not have been set yet !
			getMapModel().getMapView().setSize(getWidth(), getHeight());
			Bbox targetMaxBounds = getOverviewMaxBounds();

			MapView mapView = getMapModel().getMapView();

			// Set the maxBounds on this map as well:
			mapView.setMaxBounds(targetMaxBounds.buffer(targetMaxBounds.getWidth()));

			// apply buffer
			if (maxExtentIncreasePercentage > 0) {
				targetMaxBounds = targetMaxBounds
						.buffer(targetMaxBounds.getWidth() * maxExtentIncreasePercentage / 100);
			}

			// Then apply the map extent:
			mapView.applyBounds(targetMaxBounds, MapView.ZoomOption.LEVEL_FIT);

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

	/**
	 * The maximum bounds depend on whether useTargetMaxExtent was set. If not set, the initialBounds from the
	 * overviewMap are used. It it was set the maximum bounds of the target view is used.
	 * 
	 * @return maxBounds for overview map
	 */
	private Bbox getOverviewMaxBounds() {
		Bbox targetMaxBounds;
		if (!useTargetMaxExtent) {
			targetMaxBounds = new Bbox(getMapModel().getMapInfo().getInitialBounds());
		} else {
			// maxBounds was not configured, or need to use maxBounds from target
			if (targetMap.getMapModel().isInitialized()) {
				// rely on target map bounds
				targetMaxBounds = targetMap.getMapModel().getMapView().getMaxBounds();
			} else {
				// fall back to configured bounds (should be temporary)
				targetMaxBounds = new Bbox(targetMap.getMapModel().getMapInfo().getMaxBounds());
			}
		}
		return targetMaxBounds;
	}

	/**
	 * Updates the max extent when both maps are ready to go.
	 * 
	 * @author Jan De Moerloose
	 */
	private class MaxExtentHandler implements MapModelChangedHandler {

		public void onMapModelChanged(MapModelChangedEvent event) {
			if (getMapModel().isInitialized() && targetMap.getMapModel().isInitialized()) {
				updateMaxExtent();
			}
		}

	}

	/**
	 * Updates the point of view of the overview map when the main map's view changes.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class UpdatePointOfViewHandler implements MapViewChangedHandler {

		public void onMapViewChanged(MapViewChangedEvent event) {
			updatePov();
		}

	}

}
