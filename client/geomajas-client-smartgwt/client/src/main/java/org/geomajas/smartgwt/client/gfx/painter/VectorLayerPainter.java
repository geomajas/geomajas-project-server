/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.smartgwt.client.gfx.painter;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.smartgwt.client.gfx.MapContext;
import org.geomajas.smartgwt.client.gfx.Paintable;
import org.geomajas.smartgwt.client.gfx.Painter;
import org.geomajas.smartgwt.client.gfx.style.FontStyle;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.layer.LayerType;

/**
 * Painter for a VectorLayer object. Prepares the necessary groups for features, selected features and labels. Also
 * initiates shape-types, in case the vector layer is a point layer. On every draw, this painter will also check the
 * labeled and visible flags, and act accordingly.
 * 
 * @author Pieter De Graef
 */
public class VectorLayerPainter implements Painter {
	
	private MapWidget mapWidget;

	public VectorLayerPainter(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
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
		context.getVectorContext().drawGroup(mapWidget.getGroup(MapWidget.RenderGroup.VECTOR), layer);
		// layer.getDefaultStyle???
		context.getVectorContext().drawGroup(layer, layer.getFeatureGroup());
		context.getVectorContext().drawGroup(layer, layer.getSelectionGroup());
		FontStyle labelStyle = getLabelFontstyle(layer);
		context.getVectorContext().drawGroup(layer, layer.getLabelGroup(), labelStyle);

		// Create the needed groups in the correct order:
		context.getRasterContext().drawGroup(mapWidget.getGroup(MapWidget.RenderGroup.RASTER), layer);
		// layer.getDefaultStyle???
		context.getRasterContext().drawGroup(layer, layer.getFeatureGroup());
		context.getRasterContext().drawGroup(layer, layer.getLabelGroup());

		// Draw symbol types, as these can change any time:
		LayerType layerType = layer.getLayerInfo().getLayerType();
		if (LayerType.POINT == layerType || LayerType.MULTIPOINT == layerType || LayerType.GEOMETRY == layerType) {
			for (FeatureStyleInfo style : layer.getLayerInfo().getNamedStyleInfo().getFeatureStyles()) {
				context.getVectorContext().drawSymbolDefinition(null, style.getStyleId(), style.getSymbol(),
						new ShapeStyle(style), null);
			}
		}

		// Check layer visibility:
		if (layer.isShowing()) {
			context.getVectorContext().unhide(layer);
			context.getRasterContext().unhide(layer);
		} else {
			context.getVectorContext().hide(layer);
			context.getRasterContext().hide(layer);
		}

		// Check label visibility:
		if (layer.isLabelsShowing()) {
			context.getVectorContext().unhide(layer.getLabelGroup());
			context.getRasterContext().unhide(layer.getLabelGroup());
		} else {
			context.getVectorContext().hide(layer.getLabelGroup());
			context.getRasterContext().hide(layer.getLabelGroup());
		}
	}

	private FontStyle getLabelFontstyle(VectorLayer layer) {
		FontStyleInfo info = layer.getLayerInfo().getNamedStyleInfo().getLabelStyle().getFontStyle();
		return new FontStyle(info);
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It the object does not exist, nothing
	 * will be done.
	 * 
	 * @param paintable
	 *            The object to be painted.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		context.getVectorContext().deleteGroup(paintable);
		context.getRasterContext().deleteGroup(paintable);
	}

	// Getters and setters:

}
