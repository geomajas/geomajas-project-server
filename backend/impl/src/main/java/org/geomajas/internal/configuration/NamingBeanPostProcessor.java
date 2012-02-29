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
package org.geomajas.internal.configuration;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Post-processes all beans and sets the id/name property based on the bean name.
 * 
 * @author Jan De Moerloose
 */
@Component
public class NamingBeanPostProcessor implements BeanPostProcessor {
	public NamingBeanPostProcessor(){
		
	}
	
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// assign the spring id unless the configuration explicitly defines the id and/or name property
		assignId(bean, beanName, false);
		return bean;
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "REC_CATCH_EXCEPTION")
	private void assignId(Object bean, String beanName, boolean override) {
		try {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), "id");
			if (descriptor != null) {
				if (override || (descriptor.getReadMethod().invoke(bean) == null)) {
					descriptor.getWriteMethod().invoke(bean, beanName);
				}
			}
		} catch (Exception be) { // NOSONAR
			// ignore if no id property
		}
		try {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), "name");
			if (descriptor != null) {
				if (override || (descriptor.getReadMethod().invoke(bean, null) == null)) {
					descriptor.getWriteMethod().invoke(bean, beanName);
				}
			}
		} catch (Exception be) { // NOSONAR
			// ignore if no name property
		}
	}


}

