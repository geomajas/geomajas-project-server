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
package org.geomajas.plugin.runtimeconfig.command.dto;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanDefinitionInfo;

/**
 * Response object for the fetching of bean types. In reality, this response contains a list of
 * {@link BeanDefinitionInfo} objects.
 * 
 * @author Pieter De Graef
 */
public class ListBeansOfTypeResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private Map<String, BeanDefinitionInfo> beanDefinitionMap = new HashMap<String, BeanDefinitionInfo>();

	public Map<String, BeanDefinitionInfo> getBeanDefinitionMap() {
		return beanDefinitionMap;
	}

	public void setBeanDefinitionMap(Map<String, BeanDefinitionInfo> beanDefinitionMap) {
		this.beanDefinitionMap = beanDefinitionMap;
	}
}