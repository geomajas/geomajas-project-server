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
package org.geomajas.plugin.runtimeconfig.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.plugin.runtimeconfig.dto.bean.BeanDefinitionHolderInfo;

/**
 * Bean context data, by default serialized to XML.
 * 
 * @author Jan De Moerloose
 */
public class BeanConfigurationInfo implements Serializable {

	private static final long serialVersionUID = 100L;

	private List<BeanDefinitionHolderInfo> beanDefinitions = new ArrayList<BeanDefinitionHolderInfo>();

	private String pluginName;

	private String name;

	public List<BeanDefinitionHolderInfo> getBeanDefinitions() {
		return beanDefinitions;
	}

	public void setBeanDefinitions(List<BeanDefinitionHolderInfo> beanDefinitions) {
		this.beanDefinitions = beanDefinitions;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
