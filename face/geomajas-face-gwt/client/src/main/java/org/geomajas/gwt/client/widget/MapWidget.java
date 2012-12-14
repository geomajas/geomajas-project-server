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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.menu.AboutAction;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.controller.listener.Listener;
import org.geomajas.gwt.client.controller.listener.ListenerController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.ImageContext;
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.WorldPaintable;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.MapAddon;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.PanButtonCollection;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.ScaleBar;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.Watermark;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.ZoomAddon;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.ZoomToRectangleAddon;
import org.geomajas.gwt.client.gfx.painter.CirclePainter;
import org.geomajas.gwt.client.gfx.painter.FeaturePainter;
import org.geomajas.gwt.client.gfx.painter.FeatureTransactionPainter;
import org.geomajas.gwt.client.gfx.painter.GeometryPainter;
import org.geomajas.gwt.client.gfx.painter.ImagePainter;
import org.geomajas.gwt.client.gfx.painter.MapModelPainter;
import org.geomajas.gwt.client.gfx.painter.RasterLayerPainter;
import org.geomajas.gwt.client.gfx.painter.RasterTilePainter;
import org.geomajas.gwt.client.gfx.painter.RectanglePainter;
import org.geomajas.gwt.client.gfx.painter.TextPainter;
import org.geomajas.gwt.client.gfx.painter.VectorLayerPainter;
import org.geomajas.gwt.client.gfx.painter.VectorTilePainter;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.EditingEvent;
import org.geomajas.gwt.client.map.event.EditingEvent.EditingEventType;
import org.geomajas.gwt.client.map.event.EditingHandler;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerFilteredEvent;
import org.geomajas.gwt.client.map.event.LayerFilteredHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.event.LayerStyleChangeEvent;
import org.geomajas.gwt.client.map.event.LayerStyleChangedHandler;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.event.MapModelClearEvent;
import org.geomajas.gwt.client.map.event.MapModelClearHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.event.GraphicsReadyEvent;
import org.geomajas.gwt.client.widget.event.GraphicsReadyHandler;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;

/**
 * <p>
 * The most important widget in this framework. This is the central map. It is built using a model-view-controller
 * paradigm, where this widget basically holds the 3 components in one. The model is represented by the {@link MapModel}
 * class, the view is represented by the graphics contexts (check the {@link org.geomajas.gwt.client.gfx.MapContext}
 * class), and the controller by a {@link GraphicsController}.
 * </p>
 * <p>
 * This widget will initialize itself automatically on the onDraw event. The initialization means that it will fetch the
 * correct XML configuration (using the map's ID and the applicationID - see constructor), and build the MapModel. From
 * that point on, rendering is possible.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Oliver May
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api
public class MapWidget extends VLayout {

	// Private fields regarding internal workings:

	private MapModel mapModel;

	private GraphicsWidget graphics;

	private PainterVisitor painterVisitor;

	private double unitLength;

	private double pixelLength;

	private HandlerRegistration mouseWheelRegistration;

	private Menu defaultMenu;

	protected String applicationId;

	// MapWidget options:

	protected boolean navigationAddonEnabled = true;

	protected boolean scaleBarEnabled = true;

	/** Is zooming in and out using the mouse wheel enabled or not? This is true by default. */
	private boolean zoomOnScrollEnabled;

	private boolean resizedHandlerDisabled;

	// Rendering related fields:

	private PaintableGroup rasterGroup = new Composite("raster");

	private PaintableGroup vectorGroup = new Composite("vector");

	private PaintableGroup worldGroup = new Composite("world");

	private PaintableGroup screenGroup = new Composite("screen");

	private Map<String, WorldPaintable> worldPaintables = new LinkedHashMap<String, WorldPaintable>();

	private Map<String, MapAddon> addons = new LinkedHashMap<String, MapAddon>();

	private FeaturePainter featurePainter;
	
	private MapModelRenderer mapModelRenderer;
	
	private MapViewRenderer mapViewRenderer;

	private String cursor = Cursor.DEFAULT.getValue();
	
	private String defaultCursor = Cursor.DEFAULT.getValue();

	/**
	 * A list of handler registrations that are needed to correctly clean up after destruction.
	 */
	private List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	/**
	 * Map groups: rendering should be done in one of these. Try to always use either the SCREEN or the WORLD group,
	 * unless you need something very specific and you know what you are doing.
	 */
	public enum RenderGroup {
		/**
		 * The pan group. Drawing should be done in pan coordinates. All raster layers are drawn in pan coordinates. In
		 * essence this means that the coordinates are expected to have been scaled for the current scale before
		 * drawing, and that only the translation still needs to occur. For advanced use only.
		 */
		RASTER,

		/**
		 * The pan group. Drawing should be done in pan coordinates. All vector layers, their selection and their labels
		 * are drawn in pan coordinates. In essence this means that the coordinates are expected to have been scaled for
		 * the current scale before drawing, and that only the translation still needs to occur. For advanced use only.
		 */
		VECTOR,

		/**
		 * The world group. Drawing should be done in world coordinates. World coordinates means that the map coordinate
		 * system is used. The advantage of rendering objects in the world group, is that when the user moves the map
		 * around, the objects will move with it.
		 */
		WORLD,

		/**
		 * The screen group. Drawing should be done in screen coordinates. Screen coordinates are expressed in pixels,
		 * starting from the top left corner of the map. When rendering objects in the screen group they will always
		 * appear at a fixed position, even when the user moves the map about.
		 */
		SCREEN
	}

	/**
	 * Rendering statuses.
	 */
	public enum RenderStatus {
		/**
		 * Render paintable, including all children.
		 */
		ALL,

		/**
		 * Redraw paintable, parent only.
		 */
		UPDATE,

		/**
		 * Delete the paintable.
		 */
		DELETE
	}

	/**
	 * Types of scroll on zoom.
	 */
	public static enum ScrollZoomType {
		/**
		 * When scroll zooming, retain the center of the map position.
		 */
		ZOOM_CENTER,
		/**
		 * When scroll zooming, retain the mouse position.
		 */
		ZOOM_POSITION
	}

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Initialize a map with a unique ID that can be retrieved in the server side configuration, and an additional
	 * application ID that too can be retrieved in the server side configuration. When the onDraw event occurs, a map
	 * will automatically initialize itself.
	 * </p>
	 * <p>
	 * This constructor will register a whole list of default painters, set a default context menu and apply a zoom
	 * controller that reacts on mouse wheel events.
	 * </p>
	 * 
	 * @param mapId
	 *            The map's unique identifier, retrievable in the XML configuration.
	 * @param applicationId
	 *            The identifier of the application to which this map belongs.
	 * @since 1.6.0
	 */
	@Api
	public MapWidget(String mapId, String applicationId) {
		this(mapId, applicationId, true);
	}

	protected MapWidget(String mapId, String applicationId, boolean initMapModel) {
		setID(mapId);
		this.applicationId = applicationId;
		mapModel = new MapModel(mapId, applicationId);
		mapModelRenderer = new MapModelRenderer();
		mapModel.runWhenInitialized(mapModelRenderer);
		handlers.add(mapModel.addMapModelChangedHandler(mapModelRenderer));
		handlers.add(mapModel.addMapModelClearHandler(mapModelRenderer));
		mapViewRenderer = new MapViewRenderer();
		handlers.add(mapModel.getMapView().addMapViewChangedHandler(mapViewRenderer));
		graphics = new GraphicsWidget(getID() + "Graphics");
		painterVisitor = new PainterVisitor(graphics);
		handlers.add(mapModel.addFeatureSelectionHandler(new MapWidgetFeatureSelectionHandler(this)));
		graphics.setFallbackController(new PanController(this));

		// Painter registration:
		painterVisitor.registerPainter(new CirclePainter());
		painterVisitor.registerPainter(new RectanglePainter());
		painterVisitor.registerPainter(new TextPainter());
		painterVisitor.registerPainter(new GeometryPainter());
		painterVisitor.registerPainter(new ImagePainter());
		painterVisitor.registerPainter(new MapModelPainter(this));
		painterVisitor.registerPainter(new RasterLayerPainter(this));
		painterVisitor.registerPainter(new RasterTilePainter());
		painterVisitor.registerPainter(new VectorLayerPainter(this));
		painterVisitor.registerPainter(new VectorTilePainter(this.getMapModel().getMapView()));
		painterVisitor.registerPainter(new FeatureTransactionPainter(this));
		featurePainter = new FeaturePainter();
		painterVisitor.registerPainter(featurePainter);

		// Install a default menu:
		defaultMenu = new Menu();
		defaultMenu.addItem(new AboutAction());
		setContextMenu(defaultMenu);

		// Resizing should work correctly!
		setWidth100();
		setHeight100();
		setDynamicContents(true);
		handlers.add(graphics.addGraphicsReadyHandler(new MapResizedRenderer()));
		// adding the graphics here causes problems when embedding in HTML !
		// addChild(graphics);
		setZoomOnScrollEnabled(true);

		handlers.add(mapModel.getFeatureEditor().addEditingHandler(new EditingHandler() {

			public void onEditingChange(EditingEvent event) {
				FeatureTransaction ft = mapModel.getFeatureEditor().getFeatureTransaction();
				if (ft != null) {
					if (event.getEditingEventType().equals(EditingEventType.START_EDITING)) {
						render(ft, RenderGroup.VECTOR, RenderStatus.ALL);
					} else if (event.getEditingEventType().equals(EditingEventType.STOP_EDITING)) {
						render(ft, RenderGroup.VECTOR, RenderStatus.DELETE);
					}
				}
			}
		}));
	}

	// -------------------------------------------------------------------------
	// Initialization:
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * The map's initialization method. This method is triggered automatically on the "onDraw" event. So in normal cases
	 * you should never have to call it. Nevertheless, there are a few cases where the onDraw event might never come, so
	 * it is still possible to initialize the map directly.
	 * </p>
	 * <p>
	 * Know that without this method, the map would be an empty shell. This method will ask the server for the correct
	 * configuration, so that it is possible to build a model (MapModel).
	 * </p>
	 * 
	 * @since 1.6.0
	 */
	@Api
	public void init() {
		mapModel.init();
	}

	// -------------------------------------------------------------------------
	// Rendering related methods:
	// -------------------------------------------------------------------------

	/**
	 * Return the Object that represents one the default RenderGroups in the DOM tree when drawing.
	 * 
	 * @param group
	 *            The general group definition (RenderGroup.SCREEN, RenderGroup.WORLD, ...)
	 * @return paintable group
	 */
	public PaintableGroup getGroup(RenderGroup group) {
		switch (group) {
			case RASTER:
				return rasterGroup;
			case VECTOR:
				return vectorGroup;
			case WORLD:
				return worldGroup;
			case SCREEN:
			default:
				return screenGroup;
		}
	}

	/**
	 * Render the map completely.
	 * 
	 * @param force force rendering now
	 * @since 1.10.0
	 * @deprecated instead of calling this method directly, rendering should be triggered by events.
	 */
	@Api
	@Deprecated
	public void renderAll(boolean force) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			public void execute() {
				if (graphics.isReady()) {
					render(mapModel, null, RenderStatus.ALL);
				}
			}
		});
	}

	/**
	 * The main rendering method. Renders some paintable object in the given group, using the given status.
	 * 
	 * @param paintable
	 *            The actual object to be rendered. Should always contain location and styling information.
	 * @param renderGroup
	 *            In what group to render the paintable object?
	 * @param status
	 *            how to render
	 * @since 1.6.0
	 */
	@Api
	public void render(Paintable paintable, RenderGroup renderGroup, RenderStatus status) {
		if (!graphics.isReady() || !mapViewRenderer.isViewPortKnown() || !mapModelRenderer.isReadyToDraw()) {
			return;
		}
		PaintableGroup group = null;
		if (renderGroup != null) {
			group = getGroup(renderGroup);
		}
		if (paintable == null) {
			paintable = this.mapModel;
		}
		if (RenderStatus.DELETE.equals(status)) {
			List<Painter> painters = painterVisitor.getPaintersForObject(paintable);
			if (painters != null) {
				for (Painter painter : painters) {
					painter.deleteShape(paintable, group, graphics);
				}
			}
		} else {
			if (RenderStatus.ALL.equals(status)) {
				paintable.accept(painterVisitor, group, mapModel.getMapView().getBounds(), true);
				if (paintable == this.mapModel) {
					// Paint the world space paintable objects:
					for (WorldPaintable worldPaintable : worldPaintables.values()) {
						worldPaintable.transform(mapModel.getMapView().getWorldViewTransformer());
						worldPaintable.accept(painterVisitor, getGroup(RenderGroup.WORLD), mapModel.getMapView()
								.getBounds(), true);
					}
				}
			} else if (RenderStatus.UPDATE.equals(status)) {
				if (paintable == this.mapModel) {
					// Paint the world space paintable objects:
					for (WorldPaintable worldPaintable : worldPaintables.values()) {
						worldPaintable.transform(mapModel.getMapView().getWorldViewTransformer());
						worldPaintable.accept(painterVisitor, getGroup(RenderGroup.WORLD), mapModel.getMapView()
								.getBounds(), false);
					}
				}
				paintable.accept(painterVisitor, group, mapModel.getMapView().getBounds(), false);
			}
		}
	}

	/**
	 * Apply a new cursor on the map.
	 * 
	 * @param cursor
	 *            The new cursor to be used when the mouse hovers over the map.
	 */
	public void setCursor(Cursor cursor) {
		graphics.getRasterContext().setCursor(null, cursor.getValue());
		graphics.getVectorContext().setCursor(null, cursor.getValue());
		this.cursor = cursor.getValue(); 
	}

	/**
	 * Apply a new cursor on the map.
	 * 
	 * @param cursor
	 *            The new cursor to be used when the mouse hovers over the map.
	 * @since 1.10.0
	 */
	@Api
	public void setCursorString(String cursor) {
		try {
			Cursor c = Cursor.valueOf(cursor.toUpperCase());
			setCursor(c);
		} catch (Exception e) { // NOSONAR
			// Let us assume the cursor points to an image:
			this.cursor = cursor;
			if (!cursor.contains("url")) {
				this.cursor = "url('" + cursor + "'),auto";
			}
			graphics.getRasterContext().setCursor(null, this.cursor);
			graphics.getVectorContext().setCursor(null, this.cursor);
		}
	}

	/**
	 * Returns the cursor as a string. Could be something like "url('blop.img'),auto"
	 * 
	 * @return The cursor as a string.
	 * @since 1.10.0
	 */
	@Api
	public String getCursorString() {
		return this.cursor;
	}

	/**
	 * Apply a new default cursor on the map. This cursor will be set on deactivation of a controller.
	 * 
	 * @param cursor The new default cursor to be used when the mouse hovers over the map.
	 * @since 1.12.0
	 */
	@Api
	public void setDefaultCursorString(String cursor) {
		try {
			defaultCursor = cursor.toUpperCase();
			Cursor.valueOf(cursor.toUpperCase());
		} catch (Exception e) { // NOSONAR
			// Let us assume the cursor points to an image:
			defaultCursor = cursor;
			if (!cursor.contains("url")) {
				defaultCursor = "url('" + cursor + "'),auto";
			}
		}
		setCursorString(defaultCursor);
	}

	/**
	 * Returns the default cursor as a string. Could be something like "url('blop.img'),auto"
	 * 
	 * @return The default cursor as a string.
	 * @since 1.12.0
	 */
	@Api
	public String getDefaultCursorString() {
		return defaultCursor;
	}

	/**
	 * Apply a new context menu on the map.
	 * 
	 * @param contextMenu
	 *            The new context menu to be used when the user right clicks on the map.
	 */
	public void setContextMenu(Menu contextMenu) {
		if (null == contextMenu) {
			super.setContextMenu(defaultMenu);
		} else {
			super.setContextMenu(contextMenu);
		}
	}

	// -------------------------------------------------------------------------
	// Registration of functional objects:
	// -------------------------------------------------------------------------

	/**
	 * Register a new painter. A painter is responsible for painting <code>Paintable</code> objects of a certain class.
	 * 
	 * @param painter
	 *            The new painter to be registered. If that painter is already in the list, nothing will happen.
	 * @since 1.6.0
	 */
	@Api
	public void registerPainter(Painter painter) {
		painterVisitor.registerPainter(painter);
	}

	/**
	 * Remove a certain painter from the list again, so that it is no longer used.
	 * 
	 * @param painter
	 *            The registered painter to be removed. If it can't be found in the list, nothing will happen.
	 * @since 1.6.0
	 */
	@Api
	public void unregisterPainter(Painter painter) {
		painterVisitor.unregisterPainter(painter);
	}

	/**
	 * Register a certain <code>MapAddon</code> to be placed on the map. Map add-ons are fixed position entities on a
	 * map with possibly additional functionality. Examples are the scale bar, and navigation buttons.
	 * 
	 * @param addon
	 *            The new add-on to be added to the map.
	 */
	public void registerMapAddon(MapAddon addon) {
		if (addon != null && !addons.containsKey(addon.getId())) {
			addons.put(addon.getId(), addon);
		}
		if (graphics.isReady() && mapModelRenderer.isReadyToDraw()) {
			addon.setMapSize(getWidth(), getHeight());
			render(addon, RenderGroup.SCREEN, RenderStatus.UPDATE);
			addon.onDraw();
		}
	}

	/**
	 * Remove a registered map add-on from the map. Map add-ons are fixed position entities on a map with possibly
	 * additional functionality. Examples are the scale bar, and navigation buttons.
	 * 
	 * @param addon
	 *            The add-on to be removed from the map. If it can't be found, nothing happens.
	 */
	public void unregisterMapAddon(MapAddon addon) {
		if (addon != null && addons.containsKey(addon.getId())) {
			addons.remove(addon.getId());
			graphics.getVectorContext().deleteGroup(addon);
			addon.onRemove();
		}
	}

	/**
	 * Returns all add-ons registered for this map.
	 * 
	 * @return an unmodifiable map of add-ons
	 * @since 1.9.0
	 */
	@Api
	public Map<String, MapAddon> getMapAddons() {
		return Collections.unmodifiableMap(addons);
	}

	/**
	 * <p>
	 * Register a <code>WorldPaintable</code> object to be painted on the map. By doing so, the object will be painted
	 * immediately on the correct position, and when the user navigates around, the map will automatically make sure the
	 * <code>WorldPaintable</code> object is re-drawn at the correct location.
	 * </p>
	 * <p>
	 * If you want to draw objects in World Space, this would be the way to go.
	 * </p>
	 * 
	 * @param worldPaintable
	 *            The new WorldPaintable object to be rendered on the map.
	 * @since 1.6.0
	 */
	@Api
	public void registerWorldPaintable(WorldPaintable worldPaintable) {
		if (worldPaintable != null && !worldPaintables.containsKey(worldPaintable.getId())) {
			worldPaintables.put(worldPaintable.getId(), worldPaintable);
			worldPaintable.transform(mapModel.getMapView().getWorldViewTransformer());
			render(worldPaintable, RenderGroup.WORLD, RenderStatus.ALL);
		}
	}

	/**
	 * Remove a registered <code>WorldPaintable</code> object from the list and from the map.
	 * 
	 * @param worldPaintable
	 *            The registered WorldPaintable object to be removed again from the map.
	 * @since 1.6.0
	 */
	@Api
	public void unregisterWorldPaintable(WorldPaintable worldPaintable) {
		if (worldPaintable != null && worldPaintables.containsKey(worldPaintable.getId())) {
			render(worldPaintable, RenderGroup.WORLD, RenderStatus.DELETE);
			worldPaintables.remove(worldPaintable.getId());
		}
	}

	/**
	 * Returns the registered world paintable with the specified name.
	 * 
	 * @param name
	 *            the name of the world paintable
	 * @return the world paintable
	 * @since 1.9.0
	 */
	@Api
	public WorldPaintable getWorldPaintable(String name) {
		return worldPaintables.get(name);
	}

	/**
	 * Returns all world paintables registered for this map.
	 * 
	 * @return an unmodifiable map of world paintables
	 * @since 1.9.0
	 */
	@Api
	public Map<String, WorldPaintable> getWorldPaintables() {
		return Collections.unmodifiableMap(worldPaintables);
	}

	// -------------------------------------------------------------------------
	// Applying options:
	// -------------------------------------------------------------------------

	/**
	 * Determines whether or not the zooming using the mouse wheel should be enabled or not.
	 * 
	 * @param zoomOnScrollEnabled
	 *            True or false. Enable or disable zooming using the mouse wheel.
	 */
	public void setZoomOnScrollEnabled(boolean zoomOnScrollEnabled) {
		if (mouseWheelRegistration != null) {
			mouseWheelRegistration.removeHandler();
			mouseWheelRegistration = null;
		}
		this.zoomOnScrollEnabled = zoomOnScrollEnabled;
		if (zoomOnScrollEnabled) {
			mouseWheelRegistration = graphics.addMouseWheelHandler(new ZoomOnScrollController());
		}
	}

	/**
	 * Is the zooming using the mouse wheel currently enabled or not?
	 * 
	 * @return true when zoom on scroll is enabled
	 */
	public boolean isZoomOnScrollEnabled() {
		return zoomOnScrollEnabled;
	}

	/**
	 * Enables or disables the scale bar. This setting has immediate effect on the map.
	 * 
	 * @param enabled
	 *            set status
	 */
	public void setScalebarEnabled(boolean enabled) {
		scaleBarEnabled = enabled;
		final String scaleBarId = "scalebar";

		if (scaleBarEnabled) {
			if (!getMapAddons().containsKey(scaleBarId)) {
				ScaleBar scalebar = new ScaleBar(scaleBarId, this);
				scalebar.setVerticalAlignment(VerticalAlignment.BOTTOM);
				scalebar.setHorizontalMargin(2);
				scalebar.setVerticalMargin(2);
				scalebar.initialize(getMapModel().getMapInfo().getDisplayUnitType(), unitLength, new Coordinate(20,
						graphics.getHeight() - 25));
				registerMapAddon(scalebar);
			}
		} else {
			unregisterMapAddon(addons.get(scaleBarId));
		}
	}

	/**
	 * Is the scale bar (MapAddon) currently enabled/visible or not?
	 * 
	 * @return true when scale bar is enabled
	 */
	public boolean isScaleBarEnabled() {
		return scaleBarEnabled;
	}

	/**
	 * Enables or disables the panning buttons. This setting has immediate effect on the map.
	 * 
	 * @param enabled
	 *            enabled status
	 */
	public void setNavigationAddonEnabled(boolean enabled) {
		navigationAddonEnabled = enabled;
		final String panId = "panBTNCollection";
		final String zoomId = "zoomAddon";
		final String zoomRectId = "zoomRectAddon";

		if (enabled) {
			if (!getMapAddons().containsKey(panId)) {
				PanButtonCollection panButtons = new PanButtonCollection(panId, this);
				panButtons.setHorizontalMargin(5);
				panButtons.setVerticalMargin(5);
				registerMapAddon(panButtons);
			}

			if (!getMapAddons().containsKey(zoomId)) {
				ZoomAddon zoomAddon = new ZoomAddon(zoomId, this);
				zoomAddon.setHorizontalMargin(20);
				zoomAddon.setVerticalMargin(65);
				registerMapAddon(zoomAddon);
			}

			if (!getMapAddons().containsKey(zoomRectId)) {
				ZoomToRectangleAddon zoomToRectangleAddon = new ZoomToRectangleAddon(zoomRectId, this);
				zoomToRectangleAddon.setHorizontalMargin(20);
				zoomToRectangleAddon.setVerticalMargin(135);
				registerMapAddon(zoomToRectangleAddon);
			}
		} else {
			unregisterMapAddon(addons.get(panId));
			unregisterMapAddon(addons.get(zoomId));
			unregisterMapAddon(addons.get(zoomRectId));
		}
	}

	/**
	 * Is the navigation (MapAddon) currently enabled/visible or not?
	 * 
	 * @return true when navigation addon is enabled
	 */
	public boolean isNavigationAddonEnabled() {
		return navigationAddonEnabled;
	}

	/**
	 * Will the map automatically react on resize events or not? This option is turned on be default.
	 * 
	 * @return true when the resize handler is disabled
	 */
	public boolean isResizedHandlerDisabled() {
		return resizedHandlerDisabled;
	}

	/**
	 * Determine whether or not the map should automatically react and resize when it receives a resize event.
	 * 
	 * @param resizedHandlerDisabled
	 *            true or false
	 */
	public void setResizedHandlerDisabled(boolean resizedHandlerDisabled) {
		this.resizedHandlerDisabled = resizedHandlerDisabled;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Apply a new {@link GraphicsController} on the map. This controller will handle all mouse-events that are global
	 * for the map. Only one controller can be set at any given time.
	 * 
	 * @param controller
	 *            The new {@link GraphicsController} object.
	 * @since 1.6.0
	 */
	@Api
	public void setController(GraphicsController controller) {
		setCursorString(defaultCursor);
		graphics.setController(controller);
	}

	/**
	 * Get the currently active {@link GraphicsController}.
	 * 
	 * @return currently active {@link GraphicsController}.
	 * @since 1.8.0
	 */
	@Api
	public GraphicsController getController() {
		return graphics.getController();
	}

	/**
	 * Set a new mouse wheel controller on the map. If the zoom on scroll is currently enabled, it will be disabled
	 * first.
	 * 
	 * @param controller
	 *            The new mouse wheel controller to be applied on the map.
	 * @since 1.6.0
	 */
	@Api
	public void setMouseWheelController(MouseWheelHandler controller) {
		setZoomOnScrollEnabled(false);
		mouseWheelRegistration = graphics.addMouseWheelHandler(controller);
	}

	/**
	 * An optional fallbackController to return to, when no controller is explicitly set (controller=null). If no
	 * current controller is active when this setter is called, it is applied immediately. The default fall-back
	 * controller when a map is initialized, is the {@link PanController}, which allows you to navigate.
	 * 
	 * @param fallbackController
	 *            The new fall-back controller to use.
	 * @since 1.7.0
	 */
	@Api
	public void setFallbackController(GraphicsController fallbackController) {
		graphics.setFallbackController(fallbackController);
	}

	/**
	 * Get the currently active set of listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. The difference with a {@link GraphicsController} is that
	 * controllers can do whatever they want, while a listener is not allowed to interfere with the mouse events in any
	 * way.
	 * 
	 * @return Returns the full set of currently active listeners.
	 * @since 1.8.0
	 */
	@Api
	public Set<Listener> getListeners() {
		Set<ListenerController> controllers = graphics.getListeners();
		Set<Listener> listeners = new LinkedHashSet<Listener>();
		for (ListenerController controller : controllers) {
			listeners.add(controller.getListener());
		}
		return listeners;
	}

	/**
	 * Add a new listener to the map. These listeners passively listen to mouse events on the map, without actually
	 * interfering with these events. The difference with a {@link GraphicsController} is that controllers can do
	 * whatever they want, while a listener is not allowed to interfere with the mouse events in any way.
	 * 
	 * @param listener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 * @since 1.8.0
	 */
	@Api
	public boolean addListener(Listener listener) {
		return graphics.addListener(new ListenerController(this, listener));
	}

	/**
	 * Remove one of the currently active listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. The difference with a {@link GraphicsController} is that
	 * controllers can do whatever they want, while a listener is not allowed to interfere with the mouse events in any
	 * way.
	 * 
	 * @param listener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 * @since 1.8.0
	 */
	@Api
	public boolean removeListener(Listener listener) {
		return graphics.removeListener(graphics.getController(listener));
	}

	public double getUnitLength() {
		return unitLength;
	}

	public double getPixelLength() {
		return pixelLength;
	}

	/**
	 * Returns the number of pixels per map unit.
	 * 
	 * @return length of a pixel expressed in map units
	 * @since 1.7.0
	 */
	@Api
	public double getPixelPerUnit() {
		return pixelLength / unitLength;
	}

	/**
	 * Get the map's inner model. This model contains all the layers, handles selection, etc.
	 * 
	 * @return map model
	 * @since 1.6.0
	 */
	@Api
	public MapModel getMapModel() {
		return mapModel;
	}

	/**
	 * Get the context that handles right mouse clicks.
	 * 
	 * @return menu context
	 * @since 1.6.0
	 */
	@Api
	public MenuContext getMenuContext() {
		return graphics.getMenuContext();
	}

	/**
	 * Get the drawing context for rendering in general. If you are not using the render method, this would be an
	 * alternative - for advanced users only.
	 * 
	 * @return vector context
	 * @since 1.6.0
	 */
	@Api
	public GraphicsContext getVectorContext() {
		return graphics.getVectorContext();
	}

	/**
	 * Get the drawing context for raster layer rendering. If you are not using the render method, this would be an
	 * alternative - for advanced users only.
	 * 
	 * @return raster context
	 * @since 1.6.0
	 */
	@Api
	public ImageContext getRasterContext() {
		return graphics.getRasterContext();
	}

	public ShapeStyle getLineSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getLineSelectStyle();
		}
		return null;
	}

	public void setLineSelectStyle(ShapeStyle lineSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setLineSelectStyle(lineSelectStyle);
		}
	}

	public ShapeStyle getPointSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getPointSelectStyle();
		}
		return null;
	}

	public void setPointSelectStyle(ShapeStyle pointSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setPointSelectStyle(pointSelectStyle);
		}
	}

	public ShapeStyle getPolygonSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getPolygonSelectStyle();
		}
		return null;
	}

	public void setPolygonSelectStyle(ShapeStyle polygonSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setPolygonSelectStyle(polygonSelectStyle);
		}
	}

	public Menu getDefaultMenu() {
		return defaultMenu;
	}

	public void setDefaultMenu(Menu defaultMenu) {
		this.defaultMenu = defaultMenu;
	}

	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Given a type of Paintable, which painters have been registered to handle it?
	 * 
	 * @param paintable
	 *            A type of paintable for which a list of painters should be returned.
	 * @return Returns a list of painters that apply on the given paintable.
	 * @since 1.8.0
	 */
	public List<Painter> getPaintersForObject(Paintable paintable) {
		return painterVisitor.getPaintersForObject(paintable);
	}

	// -------------------------------------------------------------------------
	// MapViewChangedHandler implementation:
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private FeaturePainter getFeaturePainter() {
		List<Painter> painters = painterVisitor.getPaintersForObject(new Feature());
		for (Painter painter : painters) {
			if (painter instanceof FeaturePainter) {
				return (FeaturePainter) painter;
			}
		}
		return null;
	}

	protected void onDraw() {
		super.onDraw();

		// must be called before anything else !
		final int width = getWidth();
		final int height = getHeight();
		mapModel.getMapView().setSize(width, height); // causes renderAll if size changed

		// must be called before anything else !
		addMember(graphics);

		init();

		// Register the watermark MapAddon:
		Watermark watermark = new Watermark(id + "-watermark", this);
		watermark.setAlignment(Alignment.RIGHT);
		watermark.setVerticalAlignment(VerticalAlignment.BOTTOM);
		registerMapAddon(watermark);
	}

	/**
	 * Callback used on refresh of the map widget.
	 * <p/>
	 * Can be extended to customize the map. Do not call directly.
	 * 
	 * @param info
	 *            map configuration
	 */
	public void refreshCallback(ClientMapInfo info) {
		if (info != null) {
			// must be called before anything else !
			unitLength = info.getUnitLength();
			pixelLength = info.getPixelLength();
			graphics.setBackgroundColor(info.getBackgroundColor());
			featurePainter.setPointSelectStyle(new ShapeStyle(info.getPointSelectStyle()));
			featurePainter.setLineSelectStyle(new ShapeStyle(info.getLineSelectStyle()));
			featurePainter.setPolygonSelectStyle(new ShapeStyle(info.getPolygonSelectStyle()));
			if (graphics.isReady()) {
				setAddons();
			}

			for (final Layer<?> layer : mapModel.getLayers()) {
				layer.addLayerChangedHandler(new LayerChangedHandler() {

					public void onLabelChange(LayerLabeledEvent event) {
						render(layer, null, RenderStatus.ALL);
					}

					public void onVisibleChange(LayerShownEvent event) {
						render(layer, null, RenderStatus.ALL);
					}

				});

				layer.addLayerStyleChangedHandler(new LayerStyleChangedHandler() {

					public void onLayerStyleChange(LayerStyleChangeEvent event) {
						render(layer, null, RenderStatus.ALL);
					}
				});
			}
			for (final VectorLayer layer : mapModel.getVectorLayers()) {
				layer.addLayerFilteredHandler(new LayerFilteredHandler() {

					public void onFilterChange(LayerFilteredEvent event) {
						render(layer, null, RenderStatus.ALL);
					}
				});
			}
		}
	}
	
	/**
	 * Get the graphics widget of this map.
	 * @return the graphics widget
	 */
	protected GraphicsWidget getGraphics() {
		return graphics;
	}
	
	private void setAddons() {
		if (getMapModel().isInitialized()) {
			ClientMapInfo info = getMapModel().getMapInfo();
			setNavigationAddonEnabled(info.isPanButtonsEnabled());
			setScalebarEnabled(info.isScaleBarEnabled());
			for (MapAddon addon : addons.values()) {
				addon.setMapSize(getWidth(), getHeight());
				render(addon, RenderGroup.SCREEN, RenderStatus.ALL);
				if (graphics.isReady()) {
					addon.onDraw();
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// Private ResizedHandler class
	// -------------------------------------------------------------------------

	/**
	 * Handles map view and scale bar on resize (through graphics ready for stable size !).
	 */
	private class MapResizedRenderer implements GraphicsReadyHandler {

		private int previousWidth, previousHeight;

		public void onReady(GraphicsReadyEvent event) {
			if (mapModelRenderer.isReadyToDraw() && mapViewRenderer.isViewPortKnown()) {
				handleResize();
			} else {
				// map model not ready yet, try deferred
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					public void execute() {
						onReady(null);
					}
				});
			}
		}

		private void handleResize() {
			try {
				mapModel.accept(painterVisitor, null, mapModel.getMapView().getBounds(), false); // render base groups

				final int width = getWidth();
				final int height = getHeight();
				if (previousWidth != width || previousHeight != height) {
					previousWidth = width;
					previousHeight = height;

					mapModel.getMapView().setSize(width, height);
					setAddons();
					for (String addonId : addons.keySet()) {
						MapAddon addon = addons.get(addonId);
						addon.setMapSize(width, height);
						render(addon, RenderGroup.SCREEN, RenderStatus.UPDATE);
					}
				}
				setCursorString(getDefaultCursorString());
			} catch (Exception e) {
				Log.logError("OnResized exception", e);
			}
		}
	}

	/**
	 * Handles re-rendering on map view changes. Has grace period to avoid too many requests while scrolling.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class MapViewRenderer implements MapViewChangedHandler {

		private long previousRedraw; // previous redraw timestamp to avoid double redraw

		private Bbox previousRedrawBbox; // previous redraw bbox to avoid double redraw

		private static final long REDRAW_GRACE = 2500; // 2.5s min between redraw of same bbox

		private static final double DELTA = 1e-10; // delta for comparing bboxes

		private int previousRenderAllWidth;

		private int previousRenderAllHeight;

		private boolean viewPortKnown; // can't draw before the view port is known

		public void onMapViewChanged(MapViewChangedEvent event) {
			viewPortKnown = true;
			if (graphics.isReady() && mapModelRenderer.isReadyToDraw()) {
				for (String addonId : addons.keySet()) {
					MapAddon addon = addons.get(addonId);
					if (addon.isRepaintOnMapViewChange()) {
						render(addon, RenderGroup.SCREEN, RenderStatus.UPDATE);
					}
				}
				if (event != null && event.isPanDragging()) {
					render(mapModel, null, RenderStatus.UPDATE);
				} else {
					if (mapModelRenderer.isReadyToDraw()) {
						long now = System.currentTimeMillis();
						int width = getWidth();
						int height = getHeight();
						if (now > previousRedraw + REDRAW_GRACE
								|| !getMapModel().getMapView().getBounds().equals(previousRedrawBbox, DELTA)
								|| previousRenderAllWidth != width || previousRenderAllHeight != height) {
							previousRenderAllWidth = width;
							previousRenderAllHeight = height;
							previousRedraw = now;
							previousRedrawBbox = (Bbox) getMapModel().getMapView().getBounds().clone();
							render(mapModel, null, RenderStatus.ALL);
						}
					}
				}
			}
		}

		public boolean isViewPortKnown() {
			return viewPortKnown;
		}

	}

	/**
	 * Handles re-rendering on map model changes and at construction time (if map is already loaded).
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class MapModelRenderer implements Runnable, MapModelChangedHandler, MapModelClearHandler {

		private List<Layer<?>> previousLayers = new ArrayList<Layer<?>>(); // to be able to delete them on refresh

		private boolean readyToDraw;

		public void onMapModelChanged(MapModelChangedEvent event) {
			run();
		}

		public void run() {
			if (graphics.isReady() && mapViewRenderer.isViewPortKnown()) {
				handleModel();
			} else {
				// map view not ready yet (can happen at construction time), call ourselves at the end of the event loop
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					public void execute() {
						run();
					}
				});
			}
		}

		private void handleModel() {
			readyToDraw = true;
			render(mapModel, null, RenderStatus.UPDATE);

			// delete old layers first (if refresh or reorder), could we do this more subtly ?
			for (Layer<?> layer : previousLayers) {
				render(layer, null, RenderStatus.DELETE);
			}
			
			previousLayers.clear(); // just to be safe
			previousLayers.addAll(mapModel.getLayers());
			refreshCallback(mapModel.getMapInfo());
			
			// render all
			render(mapModel, null, RenderStatus.ALL);
		}

		public void onMapModelClear(MapModelClearEvent event) {
			// remove previous layers
			for (Layer<?> layer : previousLayers) {
				if (layer instanceof VectorLayer) {
					render(layer, RenderGroup.VECTOR, RenderStatus.DELETE);
				} else if (layer instanceof RasterLayer) {
					render(layer, RenderGroup.RASTER, RenderStatus.DELETE);
				}
			}
			previousLayers.clear();
			readyToDraw = false;
		}

		
		public boolean isReadyToDraw() {
			return readyToDraw;
		}
		
		

	}


	/**
	 * Controller that allows for zooming when scrolling the mouse wheel.
	 * 
	 * Default the controller will scroll retaining mouse position.
	 * 
	 * @author Pieter De Graef
	 * @author Oliver May
	 */
	private class ZoomOnScrollController implements MouseWheelHandler {

		private ScrollZoomType zoomType = ScrollZoomType.ZOOM_POSITION;

		public void onMouseWheel(MouseWheelEvent event) {
			if (event.isNorth()) {
				if (zoomType == ScrollZoomType.ZOOM_POSITION) {
					mapModel.getMapView().scale(
							2.0f,
							MapView.ZoomOption.LEVEL_CHANGE,
							mapModel.getMapView().getWorldViewTransformer()
									.viewToWorld(new Coordinate(event.getX(), event.getY())));
				} else {
					mapModel.getMapView().scale(2.0f, MapView.ZoomOption.LEVEL_CHANGE);
				}
			} else {
				if (zoomType == ScrollZoomType.ZOOM_POSITION) {
					mapModel.getMapView().scale(
							0.5f,
							MapView.ZoomOption.LEVEL_CHANGE,
							mapModel.getMapView().getWorldViewTransformer()
									.viewToWorld(new Coordinate(event.getX(), event.getY())));
				} else {
					mapModel.getMapView().scale(0.5f, MapView.ZoomOption.LEVEL_CHANGE);
				}
			}
		}
	}

	/**
	 * Renders feature on select/deselect
	 */
	private class MapWidgetFeatureSelectionHandler implements FeatureSelectionHandler {

		private MapWidget mapWidget;

		public MapWidgetFeatureSelectionHandler(MapWidget mapWidget) {
			this.mapWidget = mapWidget;
		}

		public void onFeatureSelected(FeatureSelectedEvent event) {
			mapWidget.render(event.getFeature(), RenderGroup.SCREEN, RenderStatus.UPDATE);
		}

		public void onFeatureDeselected(FeatureDeselectedEvent event) {
			mapWidget.render(event.getFeature(), RenderGroup.SCREEN, RenderStatus.DELETE);
		}
	}

	/**
	 * Refresh a layer. This will re-render the layer with freshly fetched data.
	 *
	 * @param layer layer
	 * @since 1.11.0
	 */
	@Api
	public void refreshLayer(Layer<?> layer) {
		if (layer instanceof VectorLayer) {
			VectorLayer vLayer = (VectorLayer) layer;
			vLayer.getFeatureStore().clear();
		} else if (layer instanceof RasterLayer) {
			RasterLayer rLayer = (RasterLayer) layer;
			rLayer.getStore().clear();
		}
		render(layer, null, RenderStatus.ALL);
	}

	@Override
	protected void onDestroy() {
		for (HandlerRegistration handler : handlers) {
			handler.removeHandler();
		}
		super.onDestroy();
	}

}
