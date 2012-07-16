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
package org.geomajas.plugin.admin.command.admin;

import java.util.Map;

import org.geomajas.command.Command;
import org.geomajas.plugin.admin.command.dto.ListBeansOfTypeRequest;
import org.geomajas.plugin.admin.command.dto.ListBeansOfTypeResponse;
import org.geomajas.plugin.admin.service.BeanDefinitionDtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This command fetches all beans of a certain type (class), and returns the result as a list of bean definitions.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
@Component
public class ListBeansOfTypeCommand implements Command<ListBeansOfTypeRequest, ListBeansOfTypeResponse> {

	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private BeanDefinitionDtoConverterService service;

	public void execute(ListBeansOfTypeRequest request, ListBeansOfTypeResponse response) throws Exception {
		if (request.getType() != null) {
			String className = request.getType().getClassName();
			Map<String, ?> beans = context.getBeansOfType(Class.forName(className));
			for (String key : beans.keySet()) {
				BeanDefinition definition = context.getBeanFactory().getBeanDefinition(key);
				response.getBeanDefinitionMap().put(key, service.toDto(definition));
			}
		}
	}

	public ListBeansOfTypeResponse getEmptyCommandResponse() {
		return new ListBeansOfTypeResponse();
	}
}
