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

import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.rasterizing.client.i18n.RasterizingMessages;
import org.geomajas.plugin.rasterizing.client.image.ImageUrlCallback;
import org.geomajas.plugin.rasterizing.client.image.ImageUrlService;
import org.geomajas.plugin.rasterizing.client.image.ImageUrlServiceImpl;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Action for getting a map image.
 * 
 * @author Jan De Moerloose
 */
public class GetMapImageAction extends ToolbarAction {

	private MapWidget mapWidget;

	private static final RasterizingMessages MESSAGES = GWT.create(RasterizingMessages.class);

	private ImageUrlService imageUrlService = new ImageUrlServiceImpl();

	/**
	 * Construct a new action for the specified map.
	 * 
	 * @param mapWidget the map
	 */
	public GetMapImageAction(MapWidget mapWidget) {
		super(WidgetLayout.iconExportImage, MESSAGES.getMapImageTitle(), MESSAGES.getMapImageDescription());
		this.mapWidget = mapWidget;
	}

	public void onClick(ClickEvent clickEvent) {
		imageUrlService.createImageUrl(mapWidget, new ImageUrlCallback() {

			public void onImageUrl(String mapUrl, String legendUrl) {
				com.google.gwt.user.client.Window.open(mapUrl, "_blank", null);
			}
		});
	}
}
