/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * 
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

	private void assignId(Object bean, String beanName, boolean override) {
		try {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), "id");
			if (descriptor != null) {
				if (override || (descriptor.getReadMethod().invoke(bean, null) == null)) {
					descriptor.getWriteMethod().invoke(bean, beanName);
				}
			}
		} catch (Exception be) {
			// ignore if no id property
		}
		try {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), "name");
			if (descriptor != null) {
				if (override || (descriptor.getReadMethod().invoke(bean, null) == null)) {
					descriptor.getWriteMethod().invoke(bean, beanName);
				}
			}
		} catch (Exception be) {
			// ignore if no name property
		}
	}


}

