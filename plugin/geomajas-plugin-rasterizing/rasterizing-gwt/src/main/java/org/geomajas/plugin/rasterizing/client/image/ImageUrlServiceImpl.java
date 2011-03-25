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
package org.geomajas.plugin.rasterizing.client.image;

import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapRequest;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapResponse;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;

import com.google.gwt.core.client.GWT;

import java.util.Set;

/**
 * Default implementation of {@link ImageUrlService}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ImageUrlServiceImpl implements ImageUrlService {

	public void createImageUrl(MapWidget map, ImageUrlCallback imageCallBack) {
		makeRasterizable(map);
		GwtCommand commandRequest = new GwtCommand("command.rasterizing.RasterizeMap");
		RasterizeMapRequest request = new RasterizeMapRequest();
		request.setClientMapInfo(map.getMapModel().getMapInfo());
		commandRequest.setCommandRequest(request);
		final ImageUrlCallback callBack = imageCallBack;
		GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof RasterizeMapResponse) {
					RasterizeMapResponse rasterizeMapResponse = (RasterizeMapResponse) commandResponse;
					callBack.onImageUrl(toUrl(rasterizeMapResponse.getMapKey()),
							toUrl(rasterizeMapResponse.getLegendKey()));
				}
			}

		});

	}

	public void makeRasterizable(MapWidget map) {
		ClientMapInfo mapInfo = map.getMapModel().getMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		MapView mapView = map.getMapModel().getMapView();
		mapRasterizingInfo.setBounds(GeometryConverter.toDto(map.getMapModel().getMapView().getBounds()));
		mapRasterizingInfo.setScale(mapView.getCurrentScale());
		mapRasterizingInfo.setTransparent(true);
		LegendRasterizingInfo legendRasterizingInfo = new LegendRasterizingInfo();
		legendRasterizingInfo.setTitle(map.getApplicationId());
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
				vectorRasterizingInfo.setPaintLabels(layer.isLabeled());
				vectorRasterizingInfo.setShowing(layer.isShowing());
				ClientVectorLayerInfo layerInfo = vectorLayer.getLayerInfo();
				vectorRasterizingInfo.setStyle(layerInfo.getNamedStyleInfo());
				if (vectorLayer.getSelectedFeatures().size() > 0) {
					Set<String> selectedFeatures = vectorLayer.getSelectedFeatures();
					vectorRasterizingInfo.setSelectedFeatureIds(
							selectedFeatures.toArray(new String[selectedFeatures.size()]));
					FeatureStyleInfo selectStyle = null;
					switch (layerInfo.getLayerType()) {
						case GEOMETRY:
							break;
						case LINESTRING:
						case MULTILINESTRING:
							selectStyle = mapInfo.getLineSelectStyle();
							break;
						case MULTIPOINT:
						case POINT:
							selectStyle = mapInfo.getPolygonSelectStyle();
							break;
						case MULTIPOLYGON:
						case POLYGON:
							selectStyle = mapInfo.getPointSelectStyle();
							break;
					}
					selectStyle.applyDefaults();
					vectorRasterizingInfo.setSelectionStyle(selectStyle);
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
	}

	private String toUrl(String key) {
		String url = GWT.getHostPageBaseURL();
		url += "d/rasterizing/image/" + key + ".png";
		return url;
	}

}
