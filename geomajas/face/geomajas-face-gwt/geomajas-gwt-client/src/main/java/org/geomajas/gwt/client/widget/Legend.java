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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.layer.LayerType;

import com.google.gwt.core.client.GWT;
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
 * @author Frank Wynants, Pieter De Graef
 */
public class Legend extends Canvas {

	private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	private HandlerRegistration loadedRegistration;

	private MapModel mapModel;

	private GraphicsWidget graphics;

	private Composite parentGroup = new Composite("legend-group");

	private FontStyle fontStyle = new FontStyle("#000000", 14, "Arial", "normal", "normal");

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * A legend needs to be instantiated with the MapModel that contains (or will contain) the list of layers that this
	 * legend should listen to.
	 */
	public Legend(MapModel mapModel) {
		super();
		this.mapModel = mapModel;

		graphics = new GraphicsWidget(this, SC.generateID());
		graphics.setBackgroundColor("#FFFFFF");
		addChild(graphics);

		addResizedHandler(new ResizedHandler() {

			public void onResized(ResizedEvent event) {
				GWT.log("Legend resizing: " + getWidth() + ", " + getHeight(), null);
				graphics.setSize(getWidth(), getHeight());
			}
		});

		mapModel.addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				initialize();
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
		graphics.deleteGroup(parentGroup);
		parentGroup = new Composite("legend-group");
		graphics.drawGroup(null, parentGroup);

		// Then go over all layers, to draw styles:
		int lineCount = 0;
		int y = 5;
		for (Layer<?> layer : mapModel.getLayers()) {
			if (layer.isShowing()) {

				// Go over every truly visible layer:
				if (layer instanceof VectorLayer) {
					VectorLayer vLayer = (VectorLayer) layer;

					// For vector layer; loop over the style definitions:
					for (FeatureStyleInfo styleInfo : vLayer.getLayerInfo().getNamedStyleInfo().getFeatureStyles()) {
						ShapeStyle style = new ShapeStyle(styleInfo);
						lineCount++;

						if (vLayer.getLayerInfo().getLayerType() == LayerType.LINESTRING
								|| vLayer.getLayerInfo().getLayerType() == LayerType.MULTILINESTRING) {
							// Lines, draw a LineString;
							Coordinate[] coordinates = new Coordinate[4];
							coordinates[0] = new Coordinate(10, y);
							coordinates[1] = new Coordinate(10 + 10, y + 5);
							coordinates[2] = new Coordinate(10 + 5, y + 10);
							coordinates[3] = new Coordinate(10 + 15, y + 15);
							LineString line = mapModel.getGeometryFactory().createLineString(coordinates);
							graphics.drawLine(parentGroup, "style" + lineCount, line, style);
						} else if (vLayer.getLayerInfo().getLayerType() == LayerType.POLYGON
								|| vLayer.getLayerInfo().getLayerType() == LayerType.MULTIPOLYGON) {
							// Polygons: draw a rectangle:
							Bbox rect = new Bbox(10, y, 16, 16);
							graphics.drawRectangle(parentGroup, "style" + lineCount, rect, style);
						} else if (vLayer.getLayerInfo().getLayerType() == LayerType.POINT
								|| vLayer.getLayerInfo().getLayerType() == LayerType.MULTIPOINT) {
							// Points: draw a symbol:
							graphics.drawSymbol(parentGroup, "style" + lineCount, new Coordinate(18, y + 8), style,
									styleInfo.getStyleId());
						}

						// After the style, draw the style's name:
						Coordinate textPosition = new Coordinate(30, y - 2);
						graphics.drawText(parentGroup, "text" + lineCount, styleInfo.getName(), textPosition,
								fontStyle);
						y += 21;
					}
				} else if (layer instanceof RasterLayer) {
					// For raster layers; show a nice symbol:
					lineCount++;

					graphics.drawImage(parentGroup, "style" + lineCount, Geomajas.getIsomorphicDir()
							+ "geomajas/layer-raster.png", new Bbox(10, y, 16, 16), new PictureStyle(1));
					Coordinate textPosition = new Coordinate(30, y - 2);
					graphics.drawText(parentGroup, "text" + lineCount, layer.getLabel(), textPosition, fontStyle);
					y += 20;
				}
			}
		}
		draw();
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

	/**
	 * Called when the MapModel configuration has been loaded.
	 */
	private void initialize() {
		for (Layer<?> layer : mapModel.getLayers()) {
			registrations.add(layer.addLayerChangedHandler(new LayerChangedHandler() {

				public void onLabelChange(LayerLabeledEvent event) {
				}

				public void onVisibleChange(LayerShownEvent event) {
					render();
				}
			}));
		}
		render();
	}

	/**
	 * TODO: Removing all handler on unload. Never tested if this works though...
	 */
	protected void onUnload() {
		super.onUnload();
		if (registrations != null) {
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
		}
		loadedRegistration.removeHandler();
		loadedRegistration = null;
	}
}