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
package org.geomajas.plugin.rasterizing.client.image;

import java.util.Set;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.gfx.WorldPaintable;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.util.StyleUtil;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.rasterizing.command.dto.ClientGeometryLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapRequest;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapResponse;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.sld.RuleInfo;

/**
 * Implementation for {@link ImageUrlService}.
 * 
 * @author Jan De Moerloose
 */
public class ImageUrlServiceImpl implements ImageUrlService {

	public void createImageUrl(MapWidget map, ImageUrlCallback imageCallBack, boolean makeRasterizable) {
		if (makeRasterizable) {
			makeRasterizable(map);
		}
		GwtCommand commandRequest = new GwtCommand(RasterizeMapRequest.COMMAND);
		RasterizeMapRequest request = new RasterizeMapRequest();
		request.setClientMapInfo(map.getMapModel().getMapInfo());
		commandRequest.setCommandRequest(request);
		final ImageUrlCallback callBack = imageCallBack;
		GwtCommandDispatcher.getInstance().execute(commandRequest, new AbstractCommandCallback<RasterizeMapResponse>() {

			public void execute(RasterizeMapResponse response) {
				callBack.onImageUrl(response.getMapUrl(),
						response.getLegendUrl());
			}

		});

	}

	public void createImageUrl(MapWidget map, ImageUrlCallback imageCallBack) {
		createImageUrl(map, imageCallBack, true);
	}

	public void makeRasterizable(MapWidget map) {
		ClientMapInfo mapInfo = map.getMapModel().getMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		MapView mapView = map.getMapModel().getMapView();
		mapRasterizingInfo.setBounds(GeometryConverter.toDto(map.getMapModel().getMapView().getBounds()));
		mapRasterizingInfo.setScale(mapView.getCurrentScale());
		mapRasterizingInfo.setTransparent(true);
		LegendRasterizingInfo legendRasterizingInfo = new LegendRasterizingInfo();
		legendRasterizingInfo.setTitle("Legend");
		FontStyleInfo font = new FontStyleInfo();
		font.applyDefaults();
		legendRasterizingInfo.setFont(font);
		mapRasterizingInfo.setLegendRasterizingInfo(legendRasterizingInfo);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		for (Layer<?> layer : map.getMapModel().getLayers()) {
			if (layer instanceof VectorLayer) {
				VectorLayer vectorLayer = (VectorLayer) layer;
				VectorLayerRasterizingInfo vectorRasterizingInfo = new VectorLayerRasterizingInfo();
				vectorRasterizingInfo.setPaintGeometries(true);
				vectorRasterizingInfo.setPaintLabels(layer.isLabelsShowing());
				vectorRasterizingInfo.setShowing(layer.isShowing());
				ClientVectorLayerInfo layerInfo = vectorLayer.getLayerInfo();
				vectorRasterizingInfo.setStyle(layerInfo.getNamedStyleInfo());
				if (vectorLayer.getSelectedFeatures().size() > 0) {
					Set<String> selectedFeatures = vectorLayer.getSelectedFeatures();
					vectorRasterizingInfo.setSelectedFeatureIds(selectedFeatures.toArray(new String[selectedFeatures
							.size()]));
					FeatureStyleInfo selectStyle;
					switch (layerInfo.getLayerType()) {
						case GEOMETRY:
						case LINESTRING:
						case MULTILINESTRING:
							selectStyle = mapInfo.getLineSelectStyle();
							break;
						case MULTIPOINT:
						case POINT:
							selectStyle = mapInfo.getPointSelectStyle();
							break;
						case MULTIPOLYGON:
						case POLYGON:
							selectStyle = mapInfo.getPolygonSelectStyle();
							break;
						default:
							throw new IllegalArgumentException("Unknown layer type " + layerInfo.getLayerType());
					}
					selectStyle.applyDefaults();
					RuleInfo selectionRule = StyleUtil.createRule(layerInfo.getLayerType(), selectStyle);
					vectorRasterizingInfo.setSelectionRule(selectionRule);
				}
				layerInfo.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, vectorRasterizingInfo);
			} else if (layer instanceof RasterLayer) {
				RasterLayerRasterizingInfo rasterInfo = new RasterLayerRasterizingInfo();
				ClientRasterLayerInfo layerInfo = (ClientRasterLayerInfo) layer.getLayerInfo();
				rasterInfo.setShowing(layer.isShowing());
				rasterInfo.setCssStyle(layerInfo.getStyle());
				layerInfo.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterInfo);
			}
		}
		mapRasterizingInfo.getExtraLayers().clear();
		for (WorldPaintable worldPaintable : map.getWorldPaintables().values()) {
			if (worldPaintable instanceof GfxGeometry) {
				ClientGeometryLayerInfo layer = new ClientGeometryLayerInfo();
				GfxGeometry geometry = (GfxGeometry) worldPaintable;
				layer.getGeometries().add(GeometryConverter.toDto((Geometry) geometry.getOriginalLocation()));
				RuleInfo rule = createRule(geometry);
				layer.setStyle(StyleUtil.createStyle(rule));
				layer.setLayerType(geometry.getGeometry().getLayerType());
				layer.setLabel(geometry.getId());
				layer.setId(geometry.getId());
				mapRasterizingInfo.getExtraLayers().add(layer);
			}
		}
	}

	private RuleInfo createRule(GfxGeometry geometry) {
		ShapeStyle shapeStyle = geometry.getStyle();
		SymbolInfo symbol = geometry.getSymbolInfo();
		FeatureStyleInfo fs = new FeatureStyleInfo();
		fs.setFillColor(shapeStyle.getFillColor());
		fs.setStrokeColor(shapeStyle.getStrokeColor());
		fs.setFillOpacity(shapeStyle.getFillOpacity());
		fs.setStrokeOpacity(shapeStyle.getStrokeOpacity());
		fs.setStrokeWidth((int) shapeStyle.getStrokeWidth());
		fs.setSymbol(symbol);
		fs.setName(geometry.getId());
		return StyleUtil.createRule(geometry.getGeometry().getLayerType(), fs);
	}

}
