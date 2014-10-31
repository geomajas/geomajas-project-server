package org.geomajas.plugin.jmeter;

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
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;


public class DefaultClientApplicationInfoFactory implements ClientApplicationInfoFactory {

	@Override
	public ClientApplicationInfo create(PerformanceCommandService commandDispatcher, JavaSamplerContext context) {
		GetConfigurationRequest request = new GetConfigurationRequest();
		request.setApplicationId(context.getParameter("appId"));
		String userToken = context.getParameter("userToken");
		CommandResponse response = commandDispatcher.execute(GetConfigurationRequest.COMMAND, request, userToken, null);
		GetConfigurationResponse configResponse = (GetConfigurationResponse) response;
		ClientApplicationInfo applicationInfo = configResponse.getApplication();
		// fix scale bug
		for (ClientMapInfo map : applicationInfo.getMaps()) {
			for (ScaleInfo scale : map.getScaleConfiguration().getZoomLevels()) {
				fix(scale);
			}
			fix(map.getScaleConfiguration().getMaximumScale());
			for (ClientLayerInfo layer : map.getLayers()) {
				fix(layer.getMaximumScale());
				fix(layer.getMinimumScale());
				fix(layer.getZoomToPointScale());
			}
		}
		return applicationInfo;
	}
	
	private void fix(ScaleInfo scale) {
		scale.setConversionFactor(ScaleInfo.PIXEL_PER_METER);
	}

}
