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

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.layer.LayerType;

/**
 * Painter for a VectorLayer object. Prepares the necessary groups for features, selected features and labels. Also
 * initiates shape-types, in case the vector layer is a point layer. On every draw, this painter will also check the
 * labeled and visible flags, and act accordingly.
 * 
 * @author Pieter De Graef
 */
public class VectorLayerPainter implements Painter {

	private FontStyle labelStyle;
	
	private MapWidget mapWidget;

	public VectorLayerPainter(MapWidget mapWidget) {
		this(mapWidget, new FontStyle("#FF0000", 10, "Courier New, Arial", "normal", "normal"));
	}

	public VectorLayerPainter(MapWidget mapWidget, FontStyle labelStyle) {
		this.mapWidget = mapWidget;
		this.labelStyle = labelStyle;
	}

	public String getPaintableClassName() {
		return VectorLayer.class.getName();
	}

	/**
	 * The actual painting function. Draws the groups.
	 * 
	 * @param paintable
	 *            A {@link VectorLayer} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		VectorLayer layer = (VectorLayer) paintable;

		// Create the needed groups in the correct order:
		context.getVectorContext().drawGroup(mapWidget.getGroup(RenderGroup.VECTOR), layer); // layer.getDefaultStyle???
		context.getVectorContext().drawGroup(layer, layer.getFeatureGroup());
		context.getVectorContext().drawGroup(layer, layer.getSelectionGroup());
		context.getVectorContext().drawGroup(layer, layer.getLabelGroup(), labelStyle);

		// Draw symbol types, as these can change any time:
		if (layer.getLayerInfo().getLayerType().equals(LayerType.POINT)
				|| layer.getLayerInfo().getLayerType().equals(LayerType.MULTIPOINT)) {
			for (FeatureStyleInfo style : layer.getLayerInfo().getNamedStyleInfo().getFeatureStyles()) {
				context.getVectorContext().drawSymbolDefinition(null, style.getStyleId(), style.getSymbol(),
						new ShapeStyle(style), null);
			}
		}

		// Check layer visibility:
		if (layer.isShowing()) {
			context.getVectorContext().unhide(layer);
		} else {
			context.getVectorContext().hide(layer);
		}

		// Check label visibility:
		if (layer.isLabeled()) {
			context.getVectorContext().unhide(layer.getLabelGroup());
		} else {
			context.getVectorContext().hide(layer.getLabelGroup());
		}
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It the object does not exist, nothing
	 * will be done.
	 * 
	 * @param paintable
	 *            The object to be painted.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		context.getVectorContext().deleteGroup(paintable);
	}

	// Getters and setters:

	/**
	 * Get the default font style used to draw labels.
	 * 
	 * @return font style
	 */
	public FontStyle getLabelStyle() {
		return labelStyle;
	}

	/**
	 * Set a new default font style used for drawing labels.
	 * 
	 * @param labelStyle
	 *            The new style
	 */
	public void setLabelStyle(FontStyle labelStyle) {
		this.labelStyle = labelStyle;
	}
}
