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

import java.util.List;

import org.geomajas.plugin.runtimeconfig.dto.bean.BeanDefinitionInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanMetadataElementInfo;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.core.convert.ConversionService;

/**
 * Converter service between Spring bean definitions and the DTO version, {@link BeanDefinitionInfo}. The DTO version is
 * meant to be used on the client. The following bean definition elements are supported:
 * <p>
 * <ul>
 * <li>
 * </ul>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public interface BeanDefinitionDtoConverterService {

	/**
	 * Convert from a DTO to an internal Spring bean definition.
	 * 
	 * @param beanDefinitionDto The DTO object.
	 * @return Returns a Spring bean definition.
	 */
	BeanDefinition toInternal(BeanDefinitionInfo beanDefinitionInfo);

	/**
	 * Convert from a DTO to an internal Spring bean definition.
	 * 
	 * @param beanDefinitionDto The DTO object.
	 * @return Returns a Spring bean definition.
	 */
	BeanMetadataElement toInternal(BeanMetadataElementInfo elementInfo);

	/**
	 * Convert from an internal Spring bean definition to a DTO.
	 * 
	 * @param beanDefinition The internal Spring bean definition.
	 * @return Returns a DTO representation.
	 */
	BeanDefinitionInfo toDto(BeanDefinition beanDefinition);

	/**
	 * Creates a bean definition by introspecting a Java beans object.
	 * 
	 * @param object the object to introspect
	 * @param conversionService the conversion service to be used to convert simple object types to string
	 * @return the bean definition
	 */
	BeanDefinition createBeanDefinitionByIntrospection(Object object, ConversionService conversionService);

	/**
	 * Creates a bean definition by introspecting a Java beans object.
	 * 
	 * @param object the object to introspect
	 * @return the bean definition
	 */
	BeanDefinition createBeanDefinitionByIntrospection(Object object);

	/**
	 * Creates a bean metadata element by introspecting a Java beans object.
	 * 
	 * @param object the object to introspect
	 * @param conversionService the conversion service to be used to convert simple object types to string
	 * @return the bean definition
	 */
	BeanMetadataElement createBeanMetadataElementByIntrospection(Object object, ConversionService conversionService);

	/**
	 * Creates a bean metadata by introspecting a Java beans object.
	 * 
	 * @param object the object to introspect
	 * @return the bean definition
	 */
	BeanMetadataElement createBeanMetadataElementByIntrospection(Object object);

	/**
	 * Creates a bean metadata element by introspecting a Java beans object.
	 * 
	 * @param object the object to introspect
	 * @param conversionService the conversion service to be used to convert simple object types to string
	 * @return the bean definition
	 */
	BeanMetadataElement createBeanMetadataElementByIntrospection(Object object, ConversionService conversionService,
			List<NamedObject> objectList);

	/**
	 * Creates a bean metadata by introspecting a Java beans object.
	 * 
	 * @param object the object to introspect
	 * @return the bean definition
	 */
	BeanMetadataElement createBeanMetadataElementByIntrospection(Object object, List<NamedObject> objectList);

	/**
	 * Creates a bean definition by introspecting a list Java beans objects. Bean objects that are referenced by other
	 * beans will appear as bean references in those beans.
	 * 
	 * @param objectMap the list of Java beans objects
	 * @param conversionService the conversion service to be used to convert simple object types to string
	 * @return the bean definition map
	 */
	List<BeanDefinitionHolder> createBeanDefinitionsByIntrospection(List<NamedObject> objectList,
			ConversionService conversionService);

	/**
	 * Creates a bean definition by introspecting a list Java beans objects. Bean objects that are referenced by other
	 * beans will appear as bean references in those beans.
	 * 
	 * @param objectMap the list of Java beans objects
	 * @return the bean definition map
	 */
	List<BeanDefinitionHolder> createBeanDefinitionsByIntrospection(List<NamedObject> objectList);

	/**
	 * Holder for object and name.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface NamedObject {

		/**
		 * Returns the name.
		 * 
		 * @return
		 */
		String getName();

		/**
		 * Returns the object.
		 * 
		 * @return
		 */
		Object getObject();
	}

}
