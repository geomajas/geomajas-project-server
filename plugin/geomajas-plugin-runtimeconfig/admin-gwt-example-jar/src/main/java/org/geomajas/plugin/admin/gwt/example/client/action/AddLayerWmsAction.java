/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.admin.gwt.example.client.action;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.admin.command.dto.SaveOrUpdateParameterBeanRequest;
import org.geomajas.plugin.admin.command.dto.SaveOrUpdateParameterBeanResponse;
import org.geomajas.plugin.admin.service.factory.ClientRasterLayerBeanFactory;
import org.geomajas.plugin.admin.service.factory.WmsLayerBeanFactory;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Adds a layer.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AddLayerWmsAction extends ToolbarAction {

	public static final String TOOL = "AddLayerWms";

	private MapWidget map;

	public AddLayerWmsAction(MapWidget map) {
		super("[ISOMORPHIC]/geomajas/osgeo/layer-wms-add.png", "Add WMS layer", "Add WMS layer");
		this.map = map;
	}

	public void onClick(ClickEvent event) {
		SaveOrUpdateParameterBeanRequest request = new SaveOrUpdateParameterBeanRequest();
		request.addStringParameter(WmsLayerBeanFactory.CLASS_NAME, "org.geomajas.layer.wms.WmsLayer");
		RasterLayerInfo info = new RasterLayerInfo();
		info.setDataSourceName("topp:states");
		info.setMaxExtent(new Bbox(-20026376.393709917, -20026376.393709917, 40052752.787419834, 40052752.787419834));
		info.setCrs("EPSG:900913");
		info.setTileWidth(512);
		info.setTileHeight(512);	
		request.addStringParameter(WmsLayerBeanFactory.BEAN_NAME, "wmsLayer");
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("TRANSPARENT", "TRUE"));	
		request.addListParameter(WmsLayerBeanFactory.PARAMETERS, params);
		request.addObjectParameter(WmsLayerBeanFactory.LAYER_INFO, info);
		GwtCommand command = new GwtCommand(SaveOrUpdateParameterBeanRequest.COMMAND);
		request.addStringParameter(WmsLayerBeanFactory.BASE_WMS_URL, "http://apps.geomajas.org/geoserver/wms");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<SaveOrUpdateParameterBeanResponse>() {

					public void execute(SaveOrUpdateParameterBeanResponse response) {
						SaveOrUpdateParameterBeanRequest request = new SaveOrUpdateParameterBeanRequest();
						request.addStringParameter(ClientRasterLayerBeanFactory.CLASS_NAME,
								"org.geomajas.configuration.client.ClientRasterLayerInfo");
						request.addStringParameter(ClientRasterLayerBeanFactory.BEAN_NAME, "clientWmsLayer");
						request.addStringParameter(ClientRasterLayerBeanFactory.LABEL, "WMS");
						request.addStringParameter(ClientRasterLayerBeanFactory.SERVER_LAYER_ID, "wmsLayer");
						request.addStringParameter(ClientRasterLayerBeanFactory.MAP_ID, "mapAdmin");
						GwtCommand command = new GwtCommand(SaveOrUpdateParameterBeanRequest.COMMAND);
						command.setCommandRequest(request);
						GwtCommandDispatcher.getInstance().execute(command,
								new AbstractCommandCallback<SaveOrUpdateParameterBeanResponse>() {

									public void execute(SaveOrUpdateParameterBeanResponse response) {
										map.getMapModel().refresh();
									}

								});
					}

				});
	}
}
