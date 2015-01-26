/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Test bean that needs autowiring.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AutowiredBean implements Rewirable {

	private String name;

	@Autowired(required = false)
	@Qualifier("beanC")
	private LifeCycleBean c;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LifeCycleBean getC() {
		return c;
	}

	public void setC(LifeCycleBean c) {
		this.c = c;
	}

}
