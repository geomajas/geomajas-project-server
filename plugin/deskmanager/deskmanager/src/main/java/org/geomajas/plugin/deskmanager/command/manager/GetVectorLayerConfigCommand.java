/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.geomajas.command.Command;
import org.geomajas.configuration.Parameter;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorLayerConfigRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorLayerConfigResponse;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristof Heirwegh
 */
@Component(GetVectorLayerConfigRequest.COMMAND)
public class GetVectorLayerConfigCommand implements
		Command<GetVectorLayerConfigRequest, GetVectorLayerConfigResponse> {

	private final Logger log = LoggerFactory.getLogger(GetVectorLayerConfigCommand.class);

	@Autowired
	private DiscoveryService discoServ;

	@Resource(name = "postGisDatastoreParams")
	private Map<String, String> postgisDataStoreParams;

	//FIXME: inconsistent with GetRasterLayerConfigCommand (String layer vs RasterCapabilitiesInfo)
	public void execute(GetVectorLayerConfigRequest request, GetVectorLayerConfigResponse response)
			throws Exception {
		if (request.getConnectionProperties() == null || request.getConnectionProperties().size() < 1
				|| request.getLayerName() == null || "".equals(request.getLayerName())) {
			Exception e = new IllegalArgumentException("Missing parameter connection properties or layer name.");
			log.error(e.getLocalizedMessage());
			throw e;
		} else {
			String sourceType = request.getConnectionProperties().get(DynamicLayerConfiguration.PARAM_SOURCE_TYPE);
			Map<String, String> connProps;
			if (DynamicLayerConfiguration.SOURCE_TYPE_SHAPE.equals(sourceType)) {
				connProps = postgisDataStoreParams; // get database properties
			} else {
				connProps = request.getConnectionProperties();
			}

			response.setVectorLayerConfiguration(discoServ.getVectorLayerConfiguration(connProps,
					request.getLayerName()));

			if (DynamicLayerConfiguration.SOURCE_TYPE_SHAPE.equals(sourceType)) {
				// remove connection properties, these are private and should not be sent to the client
				Parameter stp = new Parameter();
				stp.setName(DynamicLayerConfiguration.PARAM_SOURCE_TYPE);
				stp.setValue(DynamicLayerConfiguration.SOURCE_TYPE_SHAPE);
				response.getVectorLayerConfiguration().getParameters().clear();
				response.getVectorLayerConfiguration().getParameters().add(stp);
			}
		}
	}

	public GetVectorLayerConfigResponse getEmptyCommandResponse() {
		return new GetVectorLayerConfigResponse();
	}
}
