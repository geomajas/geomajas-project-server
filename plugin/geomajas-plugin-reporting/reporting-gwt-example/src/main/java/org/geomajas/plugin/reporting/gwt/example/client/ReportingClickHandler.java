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

package org.geomajas.plugin.reporting.gwt.example.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.rasterizing.client.image.ImageUrlService;
import org.geomajas.plugin.rasterizing.client.image.ImageUrlServiceImpl;
import org.geomajas.plugin.reporting.command.dto.PrepareReportingRequest;
import org.geomajas.plugin.reporting.command.dto.PrepareReportingResponse;

/**
 * Opens a report in the specified format on a single feature.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
public class ReportingClickHandler implements ClickHandler {

	private final String reportName;

	private final String clientLayerId;
	private String serverLayerId;

	private final String format;

	private final MapWidget mapWidget;

	private final ImageUrlService imageUrlService = new ImageUrlServiceImpl();

	/**
	 * Creates a handler for the specified map and format.
	 *
	 * @param mapWidget the map widget
	 * @param reportName report name
	 * @param layerId (client) layer id
	 * @param format the report format
	 */
	public ReportingClickHandler(MapWidget mapWidget, String reportName, String layerId, String format) {
		this.mapWidget = mapWidget;
		this.reportName = reportName;
		this.clientLayerId = layerId;
		this.format = format;
	}

	public void onClick(MenuItemClickEvent event) {
		VectorLayer layer = mapWidget.getMapModel().getVectorLayer(clientLayerId);
		if (null == layer) {
			Log.logError("ReportingClickHandler only be on the client id for a vector layer, so not " + clientLayerId);
			return;
		}
		serverLayerId = layer.getServerLayerId();

		PrepareReportingRequest request = new PrepareReportingRequest();
		request.setImageWidth(500);
		request.setImageHeight(500);
		request.setLegendWidth(200);
		request.setLegendHeight(602);
		request.setMargin(200);
		request.setDpi(300);
		imageUrlService.makeRasterizable(mapWidget);

		ClientMapInfo mapInfo = mapWidget.getMapModel().getMapInfo();
		/*
		for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
			if (layerId.equals(layerInfo.getServerLayerId())) {
				VectorLayerRasterizingInfo v = (VectorLayerRasterizingInfo) layerInfo.getWidgetInfo().get(
						VectorLayerRasterizingInfo.WIDGET_KEY);
				v.getSelectionRule().setName(mapWidget.getMapModel().getSelectedFeature());
			}
		}
		*/
		request.setClientMapInfo(mapInfo);
		request.setLayerId(serverLayerId);
		request.setFilter(layer.getFilter());
		GwtCommand command = new GwtCommand(PrepareReportingRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new ReportCallback());
	}

	/**
	 * Callback to open report.
	 *
	 * @author Jan De Moerloose
	 */
	public class ReportCallback extends AbstractCommandCallback<PrepareReportingResponse> {

		public void execute(PrepareReportingResponse response) {
			String url = GWT.getHostPageBaseURL();
			url += "d/reporting/c/" + serverLayerId + "/" + reportName + "." + format + "?key=" + response.getKey();
			com.google.gwt.user.client.Window.open(url, "_blank", null);
		}
	}

}
