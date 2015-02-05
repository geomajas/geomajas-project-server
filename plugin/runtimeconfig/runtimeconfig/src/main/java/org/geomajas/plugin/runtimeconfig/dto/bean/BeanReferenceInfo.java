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
package org.geomajas.plugin.runtimeconfig.dto.bean;

import java.io.Serializable;

/**
 * Data transfer object for a bean reference.
 * 
 * @author Pieter De Graef
 */
public class BeanReferenceInfo implements BeanMetadataElementInfo, Serializable {

	private static final long serialVersionUID = 100L;

	private String beanName;

	public BeanReferenceInfo() {
	}

	public BeanReferenceInfo(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String toString() {
		return "[BeanRef,{name=" + getBeanName() + "}]";
	}
}
