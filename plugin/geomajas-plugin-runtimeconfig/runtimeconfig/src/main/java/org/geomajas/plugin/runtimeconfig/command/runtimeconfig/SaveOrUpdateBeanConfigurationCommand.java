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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.geomajas.plugin.runtimeconfig.command.dto.SaveOrUpdateBeanConfigurationRequest;
import org.geomajas.plugin.runtimeconfig.command.dto.SaveOrUpdateBeanConfigurationResponse;
import org.geomajas.plugin.runtimeconfig.dto.BeanConfigurationInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanDefinitionHolderInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanDefinitionInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.ObjectBeanDefinitionInfo;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionDtoConverterService;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionDtoConverterService.NamedObject;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionWriterService;
import org.geomajas.plugin.runtimeconfig.service.ContextConfiguratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.stereotype.Component;

/**
 * Command to save or update one or more beans in the Spring configuration.
 * 
 * TODO: this is way to general, an idea might be to let plugins define a whitelist of bean types ?
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class SaveOrUpdateBeanConfigurationCommand implements
		Command<SaveOrUpdateBeanConfigurationRequest, SaveOrUpdateBeanConfigurationResponse> {

	@Autowired
	private ContextConfiguratorService configuratorService;

	@Autowired
	private BeanDefinitionDtoConverterService service;

	@Autowired
	private BeanDefinitionWriterService writerService;

	public void execute(SaveOrUpdateBeanConfigurationRequest request, SaveOrUpdateBeanConfigurationResponse response)
			throws Exception {
		BeanConfigurationInfo info = request.getBeanConfiguration();
		List<BeanDefinitionHolderInfo> beanHolders = info.getBeanDefinitions();
		List<NamedObject> objectBeans = new ArrayList<NamedObject>();
		for (BeanDefinitionHolderInfo beanHolder : beanHolders) {
			String name = beanHolder.getName();
			BeanDefinitionInfo beanInfo = beanHolder.getBeanInfo();
			if (beanInfo instanceof ObjectBeanDefinitionInfo) {
				objectBeans.add(new NamedObjectInfo((ObjectBeanDefinitionInfo) beanInfo, name));
			} else {
				BeanDefinition beanDefinition = service.toInternal(beanHolder.getBeanInfo());
				saveOrUpdateBean(name, beanDefinition);
			}
		}
		List<BeanDefinitionHolder> defs = service.createBeanDefinitionsByIntrospection(objectBeans);
		for (BeanDefinitionHolder def : defs) {
			saveOrUpdateBean(def.getBeanName(), def.getBeanDefinition());
		}

	}

	private void saveOrUpdateBean(String beanName, BeanDefinition beanDefinition) throws RuntimeConfigException {
		configuratorService.configureBeanDefinition(beanName, beanDefinition);
		writerService.persist(beanName, new BeanDefinitionHolder(beanDefinition, beanName));
	}

	public SaveOrUpdateBeanConfigurationResponse getEmptyCommandResponse() {
		return new SaveOrUpdateBeanConfigurationResponse();
	}

	/**
	 * Named object based on ObjectBeanDefinitionInfo.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	class NamedObjectInfo implements NamedObject {

		private ObjectBeanDefinitionInfo info;

		private String name;

		public NamedObjectInfo(ObjectBeanDefinitionInfo info, String name) {
			this.info = info;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Object getObject() {
			return info.getObject();
		}

	}

}