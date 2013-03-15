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
package org.geomajas.plugin.printing.client.template;

import java.util.Collection;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.util.StyleUtil;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.VectorServerLayer;
import org.geomajas.sld.RuleInfo;

/**
 * {@link PrintableLayerBuilder} for {@link VectorServerLayer} instances.
 * 
 * @author Jan De Moerloose
 * 
 */
public class VectorServerLayerBuilder implements PrintableLayerBuilder {

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double scale) {
		VectorServerLayer vectorLayer = (VectorServerLayer) layer;
		VectorLayerRasterizingInfo vectorRasterizingInfo = new VectorLayerRasterizingInfo();
		vectorRasterizingInfo.setPaintGeometries(true);
		vectorRasterizingInfo.setPaintLabels(vectorLayer.isLabeled());
		vectorRasterizingInfo.setShowing(layer.isShowing());
		ClientVectorLayerInfo layerInfo = (ClientVectorLayerInfo) vectorLayer.getLayerInfo();
		vectorRasterizingInfo.setStyle(layerInfo.getNamedStyleInfo());
		if (!vectorLayer.getSelectedFeatureIds().isEmpty()) {
			Collection<String> selectedFeatures = vectorLayer.getSelectedFeatureIds();
			vectorRasterizingInfo.setSelectedFeatureIds(selectedFeatures.toArray(new String[selectedFeatures.size()]));
			FeatureStyleInfo selectStyle;
			switch (layerInfo.getLayerType()) {
				case GEOMETRY:
				case LINESTRING:
				case MULTILINESTRING:
					selectStyle = mapPresenter.getConfiguration().getLineSelectStyle();
					break;
				case MULTIPOINT:
				case POINT:
					selectStyle = mapPresenter.getConfiguration().getPointSelectStyle();
					break;
				case MULTIPOLYGON:
				case POLYGON:
					selectStyle = mapPresenter.getConfiguration().getPolygonSelectStyle();
					break;
				default:
					throw new IllegalArgumentException("Unknown layer type " + layerInfo.getLayerType());
			}
			selectStyle.applyDefaults();
			RuleInfo selectionRule = StyleUtil.createRule(layerInfo.getLayerType(), selectStyle);
			vectorRasterizingInfo.setSelectionRule(selectionRule);
		}
		layerInfo.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, vectorRasterizingInfo);
		return layerInfo;
	}

	@Override
	public boolean supports(Layer layer) {
		return layer instanceof VectorServerLayer;
	}

}
