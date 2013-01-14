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
package org.geomajas.plugin.rasterizing.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.rasterizing.client.i18n.RasterizingMessages;
import org.geomajas.plugin.rasterizing.client.image.ImageUrlCallback;
import org.geomajas.plugin.rasterizing.client.image.ImageUrlService;
import org.geomajas.plugin.rasterizing.client.image.ImageUrlServiceImpl;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Action for getting a map image.
 * 
 * @author Jan De Moerloose
 */
public class GetLegendImageAction extends ToolbarAction implements ConfigurableAction {

	private MapWidget mapWidget;

	private static final RasterizingMessages MESSAGES = GWT.create(RasterizingMessages.class);

	private ImageUrlService imageUrlService = new ImageUrlServiceImpl();

	private boolean showAllLayers;

	/**
	 * Construct a new action for the specified map.
	 * 
	 * @param mapWidget the map
	 */
	public GetLegendImageAction(MapWidget mapWidget) {
		this(mapWidget , false);
	}

	/**
	 * Construct a new action for the specified map.
	 * 
	 * @param mapWidget the map
	 * @param showAllLayers if true, all layers are shown in the legend. If false, only the visible layers are shown.
	 */
	public GetLegendImageAction(MapWidget mapWidget, boolean showAllLayers) {
		super(null, null);
		this.mapWidget = mapWidget;
		setShowAllLayers(showAllLayers);
	}

	public void onClick(ClickEvent clickEvent) {
		imageUrlService.makeRasterizable(mapWidget);
		if (showAllLayers) {
			for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
				if (layer instanceof VectorLayer) {
					VectorLayerRasterizingInfo vectorInfo = (VectorLayerRasterizingInfo) layer.getLayerInfo()
							.getWidgetInfo().get(VectorLayerRasterizingInfo.WIDGET_KEY);
					vectorInfo.setShowing(true);
				} else if (layer instanceof RasterLayer) {
					RasterLayerRasterizingInfo rasterInfo = (RasterLayerRasterizingInfo) layer.getLayerInfo()
							.getWidgetInfo().get(RasterLayerRasterizingInfo.WIDGET_KEY);
					rasterInfo.setShowing(true);
				}
			}
		}
		imageUrlService.createImageUrl(mapWidget, new ImageUrlCallback() {

			public void onImageUrl(String mapUrl, String legendUrl) {
				com.google.gwt.user.client.Window.open(legendUrl, "_blank", null);
			}
		}, !showAllLayers);
		if (showAllLayers) {
			imageUrlService.makeRasterizable(mapWidget);
		}
	}
	
	public void setShowAllLayers(boolean showAllLayers) {
		this.showAllLayers = showAllLayers;
		if (showAllLayers) {
			setIcon("[ISOMORPHIC]/geomajas/osgeo/legend-export-all.png");
			setTitle(MESSAGES.getLegendImageAllTitle());
			setTooltip(MESSAGES.getLegendImageAllDescription());
		} else {
			setIcon("[ISOMORPHIC]/geomajas/osgeo/legend-export.png");
			setTitle(MESSAGES.getLegendImageTitle());
			setTooltip(MESSAGES.getLegendImageDescription());
		}
	}

	/** {@inheritDoc} */
	public void configure(String key, String value) {
		if ("showAllLayers".equals(key)) {
			setShowAllLayers(Boolean.parseBoolean(value));
		}

	}
}
