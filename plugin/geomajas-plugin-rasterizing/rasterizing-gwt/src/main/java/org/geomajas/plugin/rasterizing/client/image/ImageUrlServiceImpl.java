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
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.gfx.WorldPaintable;
import org.geomajas.gwt.client.gfx.paintable.Circle;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.paintable.Text;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.util.StyleUtil;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.rasterizing.command.dto.ClientWorldPaintableLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapRequest;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapResponse;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldEllipseInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldGeometryInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldImageInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldRectangleInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldTextInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;

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
		if (!map.getWorldPaintables().isEmpty()) {
			ClientWorldPaintableLayerInfo layer = new ClientWorldPaintableLayerInfo();
			layer.setLabel(mapInfo.getId() + ":world-paintables");
			for (WorldPaintable worldPaintable : map.getWorldPaintables().values()) {
				if (worldPaintable instanceof GfxGeometry) {
					GfxGeometry geometry = (GfxGeometry) worldPaintable;
					WorldGeometryInfo w = new WorldGeometryInfo();
					w.setGeometrySymbolizerInfo(createSymbolizer(geometry));
					w.setGeometry(GeometryConverter.toDto((Geometry) geometry.getOriginalLocation()));
					w.setLabel(geometry.getId());
					layer.getPaintables().add(w);
				} else if (worldPaintable instanceof Circle) {
					Circle circle = (Circle) worldPaintable;
					WorldEllipseInfo w = new WorldEllipseInfo();
					w.setGeometrySymbolizerInfo(createSymbolizer(circle.getStyle()));
					w.setBbox(GeometryConverter.toDto((Bbox) circle.getOriginalLocation()));
					w.setLabel(circle.getId());
					layer.getPaintables().add(w);
				} else if (worldPaintable instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) worldPaintable;
					WorldRectangleInfo w = new WorldRectangleInfo();
					w.setGeometrySymbolizerInfo(createSymbolizer(rectangle.getStyle()));
					w.setBbox(GeometryConverter.toDto((Bbox) rectangle.getOriginalLocation()));
					w.setLabel(rectangle.getId());
					layer.getPaintables().add(w);
				} else if (worldPaintable instanceof Image) {
					Image image = (Image) worldPaintable;
					WorldImageInfo w = new WorldImageInfo();
					w.setBbox(GeometryConverter.toDto((Bbox) image.getOriginalLocation()));
					w.setGeometrySymbolizerInfo(createSymbolizer(image.getStyle()));
					w.setUrl(image.getHref());
					w.setLabel(image.getId());
					layer.getPaintables().add(w);
				} else if (worldPaintable instanceof Text) {
					Text text = (Text) worldPaintable;
					WorldTextInfo w = new WorldTextInfo();
					w.setAnchor((Coordinate) text.getOriginalLocation());
					w.setLabelSymbolizerInfo(createTextSymbolizer(text.getStyle()));
					w.setLabel(text.getContent());
					layer.getPaintables().add(w);
				}
			}
			mapRasterizingInfo.getExtraLayers().add(layer);
		}
	}

	private SymbolizerTypeInfo createSymbolizer(PictureStyle style) {
		PolygonSymbolizerInfo symbolizerInfo = new PolygonSymbolizerInfo();
		symbolizerInfo.setFill(StyleUtil.createFill("#FFFFF", (float) style.getOpacity()));
		return symbolizerInfo;
	}

	private SymbolizerTypeInfo createSymbolizer(GfxGeometry geometry) {
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
		return StyleUtil.createSymbolizer(geometry.getGeometry().getLayerType(), fs);
	}

	private PolygonSymbolizerInfo createSymbolizer(ShapeStyle shapeStyle) {
		FeatureStyleInfo fs = new FeatureStyleInfo();
		fs.setFillColor(shapeStyle.getFillColor());
		fs.setStrokeColor(shapeStyle.getStrokeColor());
		fs.setFillOpacity(shapeStyle.getFillOpacity());
		fs.setStrokeOpacity(shapeStyle.getStrokeOpacity());
		fs.setStrokeWidth((int) shapeStyle.getStrokeWidth());
		fs.setName("");
		return (PolygonSymbolizerInfo) StyleUtil.createSymbolizer(LayerType.POLYGON, fs);
	}	

	private TextSymbolizerInfo createTextSymbolizer(FontStyle style) {
		FontStyleInfo font = new FontStyleInfo();
		font.setColor(style.getFillColor());
		font.setFamily(style.getFontFamily());
		font.setSize(style.getFontSize());
		font.setStyle(style.getFontStyle());
		font.setOpacity(1f);
		font.setWeight(style.getFontWeight());
		return StyleUtil.createSymbolizer(font);
	}

}
