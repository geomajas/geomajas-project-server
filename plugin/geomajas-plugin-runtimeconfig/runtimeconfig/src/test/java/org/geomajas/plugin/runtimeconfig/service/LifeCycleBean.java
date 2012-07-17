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
package org.geomajas.plugin.runtimeconfig.service;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class LifeCycleBean implements Rewirable {

	private String myString;

	private LifeCycle lifeCycle = LifeCycle.IDLE;

	@Autowired(required = false)
	private Map<String, AutowiredBean> allBeans;

	@Autowired(required = true)
	private ApplicationContext applicationContext;

	private AutowiredBean innerBean;

	public enum LifeCycle {
		IDLE, INITIALIZED, DESTROYED
	}

	public String getMyString() {
		return myString;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setMyString(String myString) {
		this.myString = myString;
	}

	public LifeCycle getLifeCycle() {
		return lifeCycle;
	}

	public Map<String, AutowiredBean> getBeans() {
		return allBeans;
	}

	public AutowiredBean getInnerBean() {
		return innerBean;
	}

	public void setInnerBean(AutowiredBean innerBean) {
		this.innerBean = innerBean;
	}

	@PostConstruct
	public void init() {
		lifeCycle = LifeCycle.INITIALIZED;
	}

	@PreDestroy
	public void destroy() {
		lifeCycle = LifeCycle.DESTROYED;
	}

}
