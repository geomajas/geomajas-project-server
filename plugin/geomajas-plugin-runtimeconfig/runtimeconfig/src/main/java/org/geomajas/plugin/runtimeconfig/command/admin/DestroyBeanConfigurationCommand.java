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
package org.geomajas.plugin.runtimeconfig.command.admin;

import org.geomajas.command.Command;
import org.geomajas.plugin.runtimeconfig.command.dto.DestroyBeanConfigurationRequest;
import org.geomajas.plugin.runtimeconfig.command.dto.DestroyBeanConfigurationResponse;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionWriterService;
import org.geomajas.plugin.runtimeconfig.service.ContextConfiguratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command to destroy a list of beans in the configuration.
 * 
 * @author Jan De Moerloose
 */
@Component
public class DestroyBeanConfigurationCommand implements
		Command<DestroyBeanConfigurationRequest, DestroyBeanConfigurationResponse> {

	@Autowired
	private ContextConfiguratorService configuratorService;
	
	@Autowired
	private BeanDefinitionWriterService writerService;

	public void execute(DestroyBeanConfigurationRequest request, DestroyBeanConfigurationResponse response)
			throws Exception {
		for (String name : request.getBeanNames()) {
			configuratorService.removeBeanDefinition(name);
			writerService.delete(name);
		}
	}

	public DestroyBeanConfigurationResponse getEmptyCommandResponse() {
		return new DestroyBeanConfigurationResponse();
	}

}