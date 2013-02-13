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
package org.geomajas.widget.utility.gwt.client.action.map;

import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;
import org.geomajas.widget.utility.gwt.client.action.AbstractButtonAction;
import org.geomajas.widget.utility.gwt.client.i18n.GuwMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Ribbon button that refreshes all visible layers when clicked.
 * 
 * @author Kristof Heirwegh
 */
public class RefreshLayersAction extends AbstractButtonAction {

	private static final GuwMessages MESSAGES = GWT.create(GuwMessages.class);

	public static final String IDENTIFIER = "RefreshLayersAction";
	
	private MapWidget mapWidget;

	public RefreshLayersAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/images/osgeo/redraw.png", MESSAGES.refreshLayersTitle(),
				MESSAGES.refreshLayersTooltip());
		this.mapWidget = mapWidget;
	}

	public void onClick(ClickEvent event) {
		for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
			if (layer.isShowing()) {
				if (layer instanceof VectorLayer) {
					VectorLayer vLayer = (VectorLayer) layer;
					vLayer.getFeatureStore().clear();
				} else if (layer instanceof RasterLayer) {
					RasterLayer rLayer = (RasterLayer) layer;
					rLayer.getStore().clear();
				}
				mapWidget.render(layer, null, RenderStatus.ALL);
			}
		}
	}
}
