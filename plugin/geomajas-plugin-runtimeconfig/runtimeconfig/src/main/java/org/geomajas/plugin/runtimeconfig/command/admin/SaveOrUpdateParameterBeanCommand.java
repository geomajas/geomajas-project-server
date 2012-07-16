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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.Command;
import org.geomajas.plugin.admin.AdminException;
import org.geomajas.plugin.admin.command.dto.SaveOrUpdateParameterBeanRequest;
import org.geomajas.plugin.admin.command.dto.SaveOrUpdateParameterBeanResponse;
import org.geomajas.plugin.admin.dto.parameter.ParameterDto;
import org.geomajas.plugin.admin.service.BeanDefinitionWriterService;
import org.geomajas.plugin.admin.service.BeanFactoryService;
import org.geomajas.plugin.admin.service.ContextConfiguratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.stereotype.Component;

/**
 * Command to save or update one or more beans in the Spring configuration based on a map of parameters.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component(SaveOrUpdateParameterBeanRequest.COMMAND)
public class SaveOrUpdateParameterBeanCommand implements
		Command<SaveOrUpdateParameterBeanRequest, SaveOrUpdateParameterBeanResponse> {

	@Autowired
	private BeanFactoryService beanFactoryService;

	@Autowired
	private BeanDefinitionWriterService writerService;

	@Autowired
	private ContextConfiguratorService configuratorService;

	public void execute(SaveOrUpdateParameterBeanRequest request, SaveOrUpdateParameterBeanResponse response)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		for (ParameterDto<?> parameter : request.getParameters()) {
			params.put(parameter.getName(), parameter.getValue());
		}
		List<BeanDefinitionHolder> holders = beanFactoryService.createBeans(params);
		saveOrUpdateBean(holders);
	}

	public SaveOrUpdateParameterBeanResponse getEmptyCommandResponse() {
		return new SaveOrUpdateParameterBeanResponse();
	}

	private void saveOrUpdateBean(List<BeanDefinitionHolder> holders) throws AdminException {
		if (holders.size() > 0) {
			configuratorService.configureBeanDefinitions(holders);
			// use first bean name as persistence key
			writerService.persist(holders.get(0).getBeanName(), holders);
		}
	}

}
