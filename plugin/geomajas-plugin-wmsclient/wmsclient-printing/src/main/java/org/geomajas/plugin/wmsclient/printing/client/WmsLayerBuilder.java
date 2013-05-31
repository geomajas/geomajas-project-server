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
package org.geomajas.plugin.wmsclient.printing.client;

import java.util.Collection;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.util.StyleUtil;
import org.geomajas.layer.LayerType;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.plugin.printing.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.printing.server.dto.InfoSelectedFeature;
import org.geomajas.plugin.wmsclient.printing.server.dto.WmsClientLayerInfo;
import org.geomajas.plugin.wmsclient.printing.server.dto.WmsClientSelectionInfo;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ZoomStrategy.ZoomOption;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.OpacitySupported;
import org.geomajas.sld.RuleInfo;
import org.geomajas.geometry.Geometry;

/**
 * Builder for WMS layer.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 */
public class WmsLayerBuilder implements PrintableLayerBuilder {

	private MapPresenter mapPresenter;

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double scale) {
		WmsLayer wmsLayer = (WmsLayer) layer;
		this.mapPresenter = mapPresenter;

		// fromDto()
		WmsClientLayerInfo info = new WmsClientLayerInfo();
		info.setTiles(wmsLayer.getTiles(scale, worldBounds));
		info.setTileHeight(wmsLayer.getTileConfig().getTileHeight());
		info.setTileWidth(wmsLayer.getTileConfig().getTileWidth());
		// the actual scale may be different !
		info.setScale(mapPresenter.getViewPort().getZoomStrategy().checkScale(scale, ZoomOption.LEVEL_CLOSEST));
		// info.setSelectionInfo(wmsLayer.getSelectionInfo());
		info.setId(wmsLayer.getId());
		RasterLayerRasterizingInfo rasterInfo = new RasterLayerRasterizingInfo();
		rasterInfo.setShowing(wmsLayer.isShowing());
		if (layer instanceof OpacitySupported) {
			rasterInfo.setCssStyle(((OpacitySupported) wmsLayer).getOpacity() + "");
		} else {
			rasterInfo.setCssStyle("1");
		}

		info.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterInfo); // TODO

		if (wmsLayer instanceof FeaturesSupportedWmsLayer) {
			FeaturesSupportedWmsLayer featsSupportedLayer = (FeaturesSupportedWmsLayer) wmsLayer;
			info.setSelectionInfo(buildSelectionInfo(featsSupportedLayer));
		}
		return info;
	}

	@Override
	public boolean supports(Layer layer) {
		return layer instanceof WmsLayer;
	}

	private WmsClientSelectionInfo buildSelectionInfo(FeaturesSupportedWmsLayer featsSupportedWmsLayer) {
		WmsClientSelectionInfo selectionInfo = null;

		Collection<Feature> selectedFeatures = featsSupportedWmsLayer.getSelectedFeatures();
		if (selectedFeatures.isEmpty()) {
			selectionInfo = new WmsClientSelectionInfo();
		} else {

			selectionInfo = new WmsClientSelectionInfo();
			InfoSelectedFeature[] infoSelectedFeats = new InfoSelectedFeature[selectedFeatures.size()];
			int i = 0;
			for (Feature feat : selectedFeatures) {
				infoSelectedFeats[i++] = new InfoSelectedFeature(feat.getGeometry(), feat.getId());
			}
			selectionInfo.setInfoSelectedFeatures(infoSelectedFeats);

			FeatureStyleInfo selectStyle;
			LayerType layerType = null;

			String geomType = infoSelectedFeats[0].getGeometry().getGeometryType();
			if (Geometry.POINT.equals(geomType) || Geometry.MULTI_POINT.equals(geomType)) {
				layerType = Geometry.POINT.equals(geomType) ? LayerType.POINT : LayerType.MULTIPOINT;
				selectStyle = mapPresenter.getConfiguration().getServerConfiguration().getPointSelectStyle();
				// selectStyle = mapPresenter.getConfiguration().getPointSelectStyle();

			} else if (Geometry.LINE_STRING.equals(geomType) || Geometry.MULTI_LINE_STRING.equals(geomType)) {
				layerType = Geometry.LINE_STRING.equals(geomType) ? LayerType.LINESTRING : LayerType.MULTILINESTRING;
				selectStyle = mapPresenter.getConfiguration().getServerConfiguration().getLineSelectStyle();
				// selectStyle = mapPresenter.getConfiguration().getLineSelectStyle();

			} else if (Geometry.POLYGON.equals(geomType) || Geometry.MULTI_POLYGON.equals(geomType)) {
				layerType = Geometry.POLYGON.equals(geomType) ? LayerType.POLYGON : LayerType.MULTIPOLYGON;
				selectStyle = mapPresenter.getConfiguration().getServerConfiguration().getPolygonSelectStyle();
				// selectStyle = mapPresenter.getConfiguration().getPolygonSelectStyle();
			} else {
				throw new IllegalArgumentException("Unknown Geometry type " + geomType);
			}
			selectStyle.applyDefaults();

			RuleInfo selectionRule = StyleUtil.createRule(layerType, selectStyle);
			selectionInfo.setSelectionRule(selectionRule);
		}
		return selectionInfo;
	}
}