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
package org.geomajas.plugin.admin.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.plugin.admin.dto.BeanConfigurationInfo;

/**
 * Request object for {@link org.geomajas.configurator.command.configurator.SaveOrUpdateBeanConfigurationCommand}.
 * 
 * @author Jan De Moerloose
 */
public class SaveOrUpdateBeanConfigurationRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	private BeanConfigurationInfo beanConfiguration = new BeanConfigurationInfo();

	public BeanConfigurationInfo getBeanConfiguration() {
		return beanConfiguration;
	}
}