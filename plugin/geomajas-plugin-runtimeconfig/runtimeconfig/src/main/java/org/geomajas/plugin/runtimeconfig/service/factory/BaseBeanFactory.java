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
package org.geomajas.plugin.admin.service.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.plugin.admin.AdminException;
import org.geomajas.plugin.admin.service.BeanDefinitionDtoConverterService;
import org.geomajas.plugin.admin.service.BeanDefinitionDtoConverterService.NamedObject;
import org.geomajas.plugin.admin.service.BeanFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * Base implementation of {@link BeanFactory}.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class BaseBeanFactory implements BeanFactory {

	protected String className;

	private Set<String> ignoredParams = new HashSet<String>();

	@Autowired
	protected BeanDefinitionDtoConverterService beanDefinitionDtoConverterService;

	protected BaseBeanFactory(Class<?> clazz) {
		this(clazz.getName());
	}

	protected BaseBeanFactory(String className) {
		this.className = className;
		addToIgnoreList(BEAN_NAME);
		addToIgnoreList(CLASS_NAME);
		addToIgnoreList(BEAN_REFS);
	}

	public Priority getPriority(Map<String, Object> parameters) {
		Priority priority = Priority.DEFAULT;
		priority = priority.and(checkString(BEAN_NAME, parameters));
		priority = priority.and(checkEquals(CLASS_NAME, className, parameters));
		return priority;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BeanDefinitionHolder> createBeans(Map<String, Object> parameters) throws AdminException {
		GenericBeanDefinition def = new GenericBeanDefinition();
		def.setBeanClassName(className);
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		List<NamedObject> namedObjects = new ArrayList<NamedObject>();
		if (checkCollection(BEAN_REFS, NamedObject.class, parameters) != Priority.NONE) {
			namedObjects.addAll((Collection) parameters.get(BEAN_REFS));
		}
		for (String name : parameters.keySet()) {
			if (!ignoredParams.contains(name)) {
				propertyValues.addPropertyValue(name, beanDefinitionDtoConverterService
						.createBeanMetadataElementByIntrospection(parameters.get(name), namedObjects));
			}
		}
		def.setPropertyValues(propertyValues);
		BeanDefinitionHolder holder = new BeanDefinitionHolder(def, (String) parameters.get(BEAN_NAME));
		List<BeanDefinitionHolder> holders = new ArrayList<BeanDefinitionHolder>();
		holders.add(holder);
		return holders;
	}

	protected void addToIgnoreList(String name) {
		ignoredParams.add(name);
	}

	protected String getString(String name, Map<String, Object> parameters) {
		return (String) parameters.get(name);
	}

	protected Priority checkString(String name, Map<String, Object> parameters) {
		return checkClass(name, String.class, parameters);
	}

	protected Priority checkEquals(String name, Object value, Map<String, Object> parameters) {
		if (value.equals(parameters.get(name))) {
			return Priority.DEFAULT;
		} else {
			return Priority.NONE;
		}
	}

	protected Priority checkClass(String name, Class<?> clazz, Map<String, Object> parameters) {
		if (parameters.containsKey(name)) {
			if (clazz.isAssignableFrom(parameters.get(name).getClass())) {
				return Priority.DEFAULT;
			}
		}
		return Priority.NONE;
	}

	protected Priority checkOptionalString(String name, Map<String, Object> parameters) {
		return checkOptionalClass(name, String.class, parameters);
	}

	protected Priority checkOptionalClass(String name, Class<?> clazz, Map<String, Object> parameters) {
		if (parameters.containsKey(name)) {
			if (clazz.isAssignableFrom(parameters.get(name).getClass())) {
				return Priority.DEFAULT;
			} else {
				return Priority.NONE;
			}
		}
		return Priority.DEFAULT;
	}

	protected Priority checkCollection(String name, Class<?> clazz, Map<String, Object> parameters) {
		if (parameters.containsKey(name)) {
			if (parameters.get(name) instanceof Collection) {
				Collection<?> collection = (Collection<?>) parameters.get(name);
				for (Object object : collection) {
					if (!clazz.isAssignableFrom(object.getClass())) {
						return Priority.NONE;
					}
				}
				return Priority.DEFAULT;
			} else {
				return Priority.NONE;
			}
		}
		return Priority.NONE;
	}

	protected Priority checkOptionalCollection(String name, Class<?> clazz, Map<String, Object> parameters) {
		if (parameters.containsKey(name)) {
			if (parameters.get(name) instanceof Collection) {
				Collection<?> collection = (Collection<?>) parameters.get(name);
				for (Object object : collection) {
					if (!clazz.isAssignableFrom(object.getClass())) {
						return Priority.NONE;
					}
				}
				return Priority.DEFAULT;
			} else {
				return Priority.NONE;
			}
		}
		return Priority.DEFAULT;
	}
	
	protected <T> Collection<T> getCollection(String name, Map<String, Object> parameters) {
		return (Collection) parameters.get(name);
	}

	protected <T> T getObject(String name, Map<String, Object> parameters) {
		return (T) parameters.get(name);
	}
	

	protected void addBeanRef(final String name, final NamedStyleInfo style, Map<String, Object> parameters) {
		if (!parameters.containsKey(BEAN_REFS)) {
			parameters.put(BEAN_REFS, new ArrayList<NamedObject>());
		}
		getCollection(BEAN_REFS, parameters).add(new NamedObject() {
			
			public Object getObject() {
				return style;
			}
			
			public String getName() {
				return name;
			}
		});
	}


}
