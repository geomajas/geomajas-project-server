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

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerFilteredEvent;
import org.geomajas.gwt.client.map.event.LayerFilteredHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.event.LayerStyleChangeEvent;
import org.geomajas.gwt.client.map.event.LayerStyleChangedHandler;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.event.GraphicsReadyEvent;
import org.geomajas.gwt.client.widget.event.GraphicsReadyHandler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;

/**
 * <p>
 * Widget that shows the styles of the currently visible layers. For vector layers, there can be many styles. Note that
 * this widget will react automatically to the visibility status of the layers.
 * </p>
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class Legend extends Canvas {

	private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	private HandlerRegistration loadedRegistration;

	private HandlerRegistration resizeRegistration;

	private HandlerRegistration graphicsRegistration;

	private MapModel mapModel;

	private GraphicsWidget widget;

	private GraphicsContext graphics;

	private Composite parentGroup = new Composite("legend-group");

	private FontStyle fontStyle = new FontStyle("#000000", 14, "Arial", "normal", "normal");

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * A legend needs to be instantiated with the MapModel that contains (or will contain) the list of layers that this
	 * legend should listen to.
	 *
	 * @param mapModel map model
	 */
	public Legend(MapModel mapModel) {
		super();
		setWidth100();
		setHeight100();
		this.mapModel = mapModel;

		widget = new GraphicsWidget(SC.generateID());
		widget.setBackgroundColor(WidgetLayout.legendBackgroundColor);
		// adding the graphics here causes problems when embedding in HTML !
		// addChild(widget);

		graphics = widget.getVectorContext();

		loadedRegistration = mapModel.addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				initialize();
			}
		});

		resizeRegistration = addResizedHandler(new ResizedHandler() {

			public void onResized(ResizedEvent event) {
				// Triggered by the render method (setHeight):
				widget.setSize(getWidthAsString(), getHeightAsString());
				widget.resize();
			}
		});

		graphicsRegistration = widget.addGraphicsReadyHandler(new GraphicsReadyHandler() {

			public void onReady(GraphicsReadyEvent event) {
				// Triggered by the resized handler (i.e. we're ready to render the legend):
				renderWithoutResize();
			}
		});
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Render the legend. This triggers a complete redraw.
	 */
	public void render() {
		int y = WidgetLayout.marginSmall;
		for (Layer<?> layer : mapModel.getLayers()) {
			if (layer.isShowing()) {
				if (layer instanceof VectorLayer) {
					VectorLayer vLayer = (VectorLayer) layer;
					y += WidgetLayout.legendVectorRowHeight *
							vLayer.getLayerInfo().getNamedStyleInfo().getFeatureStyles().size();
				} else if (layer instanceof RasterLayer) {
					y += WidgetLayout.legendRasterRowHeight;
				}
			}
		}
		setHeight(y);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public MapModel getMapModel() {
		return mapModel;
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/** Render the legend, without actually resizing the widget. */
	private void renderWithoutResize() {
		graphics.deleteGroup(parentGroup);
		parentGroup = new Composite("legend-group");
		graphics.drawGroup(null, parentGroup);

		// Then go over all layers, to draw styles:
		int lineCount = 0;
		int y = WidgetLayout.marginSmall;
		int labelIndent = WidgetLayout.marginLarge + WidgetLayout.legendLabelIndent;
		for (Layer<?> layer : mapModel.getLayers()) {
			if (layer.isShowing()) {
				// Go over every truly visible layer:
				if (layer instanceof VectorLayer) {
					ClientVectorLayerInfo layerInfo = ((VectorLayer) layer).getLayerInfo();

					// For vector layer; loop over the style definitions:
					for (FeatureStyleInfo styleInfo : layerInfo.getNamedStyleInfo().getFeatureStyles()) {
						ShapeStyle style = new ShapeStyle(styleInfo);
						graphics.drawSymbolDefinition(null, styleInfo.getStyleId(), styleInfo.getSymbol(),
								new ShapeStyle(styleInfo), null);
						lineCount++;

						switch (layerInfo.getLayerType()) {
							case LINESTRING:
							case MULTILINESTRING:
								// Lines, draw a LineString;
								Coordinate[] coordinates = new Coordinate[4];
								coordinates[0] = new Coordinate(WidgetLayout.marginLarge, y);
								coordinates[1] = new Coordinate(WidgetLayout.marginLarge + 10, y + 5);
								coordinates[2] = new Coordinate(WidgetLayout.marginLarge + 5, y + 10);
								coordinates[3] = new Coordinate(WidgetLayout.marginLarge + 15, y + 15);
								LineString line = mapModel.getGeometryFactory().createLineString(coordinates);
								graphics.drawLine(parentGroup, "style" + lineCount, line, style);
								break;
							case POLYGON:
							case MULTIPOLYGON:
								// Polygons: draw a rectangle:
								Bbox rect = new Bbox(WidgetLayout.marginLarge, y, 16, 16);
								graphics.drawRectangle(parentGroup, "style" + lineCount, rect, style);
								break;
							case POINT:
							case MULTIPOINT:
								// Points: draw a symbol:
								graphics.drawSymbol(parentGroup, "style" + lineCount,
										new Coordinate(WidgetLayout.marginLarge + 8, y + 8), style,
										styleInfo.getStyleId());
								break;
							case GEOMETRY:
								// Lines + point
								Coordinate[] linePoints = new Coordinate[3];
								linePoints[0] = new Coordinate(WidgetLayout.marginLarge, y);
								linePoints[1] = new Coordinate(WidgetLayout.marginLarge + 10, y + 5);
								linePoints[2] = new Coordinate(WidgetLayout.marginLarge + 5, y + 10);
								LineString geometryLine = mapModel.getGeometryFactory().createLineString(linePoints);
								graphics.drawLine(parentGroup, "style" + lineCount, geometryLine, style);
								graphics.drawSymbol(parentGroup, "style" + lineCount, new Coordinate(18, y + 12), style,
										styleInfo.getStyleId());
								break;
							default:
								throw new IllegalStateException("Unhandled layer type " + layerInfo.getLayerType());
						}

						// After the style, draw the style's name:
						drawLabel(labelIndent, y, lineCount, styleInfo.getName());
						y += WidgetLayout.legendVectorRowHeight;
					}
				} else if (layer instanceof RasterLayer) {
					// For raster layers; show a nice symbol:
					lineCount++;

					graphics.drawImage(parentGroup, "style" + lineCount, Geomajas.getIsomorphicDir()
							+ WidgetLayout.legendRasterIcon,
							new Bbox(WidgetLayout.marginLarge, y, WidgetLayout.legendRasterIconWidth,
									WidgetLayout.legendRasterIconHeight),
							new PictureStyle(1));
					drawLabel(labelIndent, y, lineCount, layer.getLabel());
					y += WidgetLayout.legendRasterRowHeight;
				}
			}
		}
	}

	private void drawLabel(int x, int y, int lineCount, String label) {
		Coordinate textPosition = new Coordinate(x, y - 2);
		graphics.drawText(parentGroup, "text" + lineCount, label, textPosition, fontStyle);
	}

	/** Called when the MapModel configuration has been loaded. */
	private void initialize() {
		addChild(widget);
		for (Layer<?> layer : mapModel.getLayers()) {
			registrations.add(layer.addLayerChangedHandler(new LayerChangedHandler() {

				public void onLabelChange(LayerLabeledEvent event) {
				}

				public void onVisibleChange(LayerShownEvent event) {
					render();
				}

			}));
			registrations.add(layer.addLayerStyleChangedHandler(new LayerStyleChangedHandler() {

				public void onLayerStyleChange(LayerStyleChangeEvent event) {
					render();
				}
			}));
		}
		for (final VectorLayer layer : mapModel.getVectorLayers()) {
			layer.addLayerFilteredHandler(new LayerFilteredHandler() {

				public void onFilterChange(LayerFilteredEvent event) {
					render();
				}
			});
		}
		render();
	}

	/** Remove all handlers on unload. */
	protected void onUnload() {
		if (registrations != null) {
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
		}
		loadedRegistration.removeHandler();
		resizeRegistration.removeHandler();
		graphicsRegistration.removeHandler();
		super.onUnload();
	}
}