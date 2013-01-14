/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Null;

import org.geomajas.plugin.runtimeconfig.dto.bean.BeanDefinitionHolderInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanDefinitionInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanMetadataElementInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanReferenceInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.GenericBeanDefinitionInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.ManagedListInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.ManagedMapInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.ManagedSetInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.ObjectBeanDefinitionInfo;
import org.geomajas.plugin.runtimeconfig.dto.bean.TypedStringInfo;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedArray;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Converter service between Spring bean definitions and the DTO version, {@link BeanDefinitionInfo}.
 * </p>
 * <p>
 * Only beans that are read from an XML Spring configuration have been tested so far. This means that property values
 * are typically represented as Strings. In other words, these bean definitions, or their DTO counterparts do not
 * contain the actual type information for the properties.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public class BeanDefinitionDtoConverterServiceImpl implements BeanDefinitionDtoConverterService {

	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Autowired
	@Qualifier("adminConversionService")
	private FormattingConversionService conversionService;

	@PostConstruct
	public void postConstruct() {
		DateFormatter dateFormatter = new DateFormatter();
		dateFormatter.setPattern("yyyy-MM-dd");
		conversionService.addFormatterForFieldType(Date.class, dateFormatter);
	}

	/**
	 * Convert from a DTO to an internal Spring bean definition.
	 * 
	 * @param beanDefinitionDto The DTO object.
	 * @return Returns a Spring bean definition.
	 */
	public BeanDefinition toInternal(BeanDefinitionInfo beanDefinitionInfo) {
		if (beanDefinitionInfo instanceof GenericBeanDefinitionInfo) {
			GenericBeanDefinitionInfo genericInfo = (GenericBeanDefinitionInfo) beanDefinitionInfo;
			GenericBeanDefinition def = new GenericBeanDefinition();
			def.setBeanClassName(genericInfo.getClassName());
			if (genericInfo.getPropertyValues() != null) {
				MutablePropertyValues propertyValues = new MutablePropertyValues();
				for (Entry<String, BeanMetadataElementInfo> entry : genericInfo.getPropertyValues().entrySet()) {
					BeanMetadataElementInfo info = entry.getValue();
					propertyValues.add(entry.getKey(), toInternal(info));
				}
				def.setPropertyValues(propertyValues);
			}
			return def;
		} else if (beanDefinitionInfo instanceof ObjectBeanDefinitionInfo) {
			ObjectBeanDefinitionInfo objectInfo = (ObjectBeanDefinitionInfo) beanDefinitionInfo;
			return createBeanDefinitionByIntrospection(objectInfo.getObject());
		} else {
			throw new IllegalArgumentException("Conversion to internal of " + beanDefinitionInfo.getClass().getName()
					+ " not implemented");
		}
	}

	/**
	 * Convert from an internal Spring bean definition to a DTO.
	 * 
	 * @param beanDefinition The internal Spring bean definition.
	 * @return Returns a DTO representation.
	 */
	public BeanDefinitionInfo toDto(BeanDefinition beanDefinition) {
		if (beanDefinition instanceof GenericBeanDefinition) {
			GenericBeanDefinitionInfo info = new GenericBeanDefinitionInfo();
			info.setClassName(beanDefinition.getBeanClassName());

			if (beanDefinition.getPropertyValues() != null) {
				Map<String, BeanMetadataElementInfo> propertyValues = new HashMap<String, BeanMetadataElementInfo>();
				for (PropertyValue value : beanDefinition.getPropertyValues().getPropertyValueList()) {
					Object obj = value.getValue();
					if (obj instanceof BeanMetadataElement) {
						propertyValues.put(value.getName(), toDto((BeanMetadataElement) obj));
					} else {
						throw new IllegalArgumentException("Type " + obj.getClass().getName()
								+ " is not a BeanMetadataElement for property: " + value.getName());
					}
				}
				info.setPropertyValues(propertyValues);
			}
			return info;
		} else {
			throw new IllegalArgumentException("Conversion to DTO of " + beanDefinition.getClass().getName()
					+ " not implemented");
		}
	}

	protected BeanMetadataElementInfo toDto(BeanMetadataElement element) {
		if (element instanceof TypedStringValue) {
			// Direct property values:
			TypedStringValue stringValue = (TypedStringValue) element;
			return new TypedStringInfo(stringValue.getValue());
		} else if (element instanceof BeanDefinition) {
			// Property is actually another bean:
			BeanDefinition definition = (BeanDefinition) element;
			return toDto(definition);
		} else if (element instanceof BeanDefinitionHolder) {
			// Property is a bean too:
			BeanDefinitionHolder holder = (BeanDefinitionHolder) element;
			return new BeanDefinitionHolderInfo(holder.getBeanName(), toDto(holder.getBeanDefinition()));
		} else if (element instanceof RuntimeBeanReference) {
			// Property is actually a reference to another bean:
			RuntimeBeanReference reference = (RuntimeBeanReference) element;
			return new BeanReferenceInfo(reference.getBeanName());
		} else if (element instanceof ManagedList<?>) {
			// Property is actually list of objects:
			ManagedList<?> reference = (ManagedList<?>) element;
			ManagedListInfo dto = new ManagedListInfo();
			for (Object o : reference) {
				dto.add(toDto((BeanMetadataElement) o));
			}
			return dto;
		} else if (element instanceof ManagedMap<?, ?>) {
			// Property is actually map of object/object pairs
			ManagedMap<?, ?> reference = (ManagedMap<?, ?>) element;
			ManagedMapInfo dto = new ManagedMapInfo();
			for (Map.Entry<?, ?> entry : reference.entrySet()) {
				dto.put(toDto((BeanMetadataElement) entry.getKey()), toDto((BeanMetadataElement) entry.getValue()));
			}
			return dto;
		} else if (element instanceof ManagedSet<?>) {
			// Property is actually set of objects
			ManagedSet<?> reference = (ManagedSet<?>) element;
			ManagedSetInfo dto = new ManagedSetInfo();
			for (Object o : reference) {
				dto.add(toDto((BeanMetadataElement) o));
			}
			return dto;
		} else {
			throw new IllegalArgumentException("Conversion to dto of " + element.getClass().getName()
					+ " not implemented");
		}
	}

	public BeanMetadataElement toInternal(BeanMetadataElementInfo info) {
		if (info instanceof TypedStringInfo) {
			return new TypedStringValue(((TypedStringInfo) info).getValue());
		} else if (info instanceof BeanDefinitionHolderInfo) {
			BeanDefinitionHolderInfo holderInfo = (BeanDefinitionHolderInfo) info;
			return new BeanDefinitionHolder(toInternal(holderInfo.getBeanInfo()), holderInfo.getName());
		} else if (info instanceof BeanDefinitionInfo) {
			// Property is actually another bean:
			BeanDefinitionInfo definitionInfo = (BeanDefinitionInfo) info;
			return toInternal(definitionInfo);
		} else if (info instanceof BeanReferenceInfo) {
			// Property is actually a reference to another bean:
			BeanReferenceInfo referenceInfo = (BeanReferenceInfo) info;
			return new RuntimeBeanReference(referenceInfo.getBeanName());
		} else if (info instanceof ManagedListInfo) {
			// Property is actually a list:
			ManagedList<BeanMetadataElement> list = new ManagedList<BeanMetadataElement>();
			for (BeanMetadataElementInfo metadataInfo : ((ManagedListInfo) info)) {
				list.add(toInternal(metadataInfo));
			}
			return list;

		} else if (info instanceof ManagedMapInfo) {
			// Property is actually a map:
			ManagedMap<BeanMetadataElement, BeanMetadataElement> map = 
					new ManagedMap<BeanMetadataElement, BeanMetadataElement>();
			for (Map.Entry<BeanMetadataElementInfo, BeanMetadataElementInfo> entry :
				((ManagedMapInfo) info).entrySet()) {
				map.put(toInternal(entry.getKey()), (toInternal(entry.getValue())));
			}
			return map;

		} else if (info instanceof ManagedSetInfo) {
			// Property is actually a set:
			ManagedSet<BeanMetadataElement> set = new ManagedSet<BeanMetadataElement>();
			for (BeanMetadataElementInfo metadataInfo : ((ManagedSetInfo) info)) {
				set.add(toInternal(metadataInfo));
			}
			return set;

		} else {
			throw new IllegalArgumentException("Conversion to internal of " + info.getClass().getName()
					+ " not implemented");
		}
	}

	public List<BeanDefinitionHolder> createBeanDefinitionsByIntrospection(List<NamedObject> objectList,
			ConversionService conversionService) {
		NamedBeanMap refs = new NamedBeanMap(objectList);
		List<BeanDefinitionHolder> beans = new ArrayList<BeanDefinitionHolder>();
		for (NamedObject object : objectList) {
			beans.add(new BeanDefinitionHolder(createBeanDefinitionByIntrospection(object.getObject(), refs,
					conversionService), object.getName()));
		}
		return beans;
	}

	public List<BeanDefinitionHolder> createBeanDefinitionsByIntrospection(List<NamedObject> objectList) {
		return createBeanDefinitionsByIntrospection(objectList, conversionService);
	}

	public BeanDefinition createBeanDefinitionByIntrospection(Object object) {
		return createBeanDefinitionByIntrospection(object, new NamedBeanMap(), conversionService);
	}

	public BeanDefinition createBeanDefinitionByIntrospection(Object object, ConversionService conversionService) {
		return createBeanDefinitionByIntrospection(object, new NamedBeanMap(), conversionService);
	}

	public BeanMetadataElement createBeanMetadataElementByIntrospection(Object object) {
		return createMetadataElementByIntrospection(object, new NamedBeanMap(), conversionService);
	}

	public BeanMetadataElement createBeanMetadataElementByIntrospection(Object object,
			ConversionService conversionService) {
		return createBeanDefinitionByIntrospection(object, new NamedBeanMap(), conversionService);
	}
	
	public BeanMetadataElement createBeanMetadataElementByIntrospection(Object object,
			ConversionService conversionService, List<NamedObject> objectList) {
		NamedBeanMap refs = new NamedBeanMap(objectList);
		return createMetadataElementByIntrospection(object, refs, conversionService);
	}

	public BeanMetadataElement createBeanMetadataElementByIntrospection(Object object, List<NamedObject> objectList) {
		NamedBeanMap refs = new NamedBeanMap(objectList);
		return createMetadataElementByIntrospection(object, refs, conversionService);
	}

	private BeanDefinition createBeanDefinitionByIntrospection(Object object, NamedBeanMap refs,
			ConversionService conversionService) {
		validate(object);
		GenericBeanDefinition def = new GenericBeanDefinition();
		def.setBeanClass(object.getClass());
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(object.getClass())) {
			if (descriptor.getWriteMethod() != null) {
				try {
					Object value = descriptor.getReadMethod().invoke(object, (Object[]) null);
					if (value != null) {
						if ("id".equals(descriptor.getName())) {

						} else {
							propertyValues.addPropertyValue(descriptor.getName(),
									createMetadataElementByIntrospection(value, refs, conversionService));
						}
					}
				} catch (Exception e) {
					// our contract says to ignore this property
				}
			}
		}
		def.setPropertyValues(propertyValues);
		return def;
	}

	/**
	 * Take a stab at fixing validation problems ?
	 * 
	 * @param object
	 */
	private void validate(Object object) {
		Set<ConstraintViolation<Object>> viols = validator.validate(object);
		for (ConstraintViolation<Object> constraintViolation : viols) {
			if (Null.class.isAssignableFrom(constraintViolation.getConstraintDescriptor().getAnnotation().getClass())) {
				Object o = constraintViolation.getLeafBean();
				Iterator<Node> iterator = constraintViolation.getPropertyPath().iterator();
				String propertyName = null;
				while (iterator.hasNext()) {
					propertyName = iterator.next().getName();
				}
				if (propertyName != null) {
					try {
						PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(o.getClass(), propertyName);
						descriptor.getWriteMethod().invoke(o, new Object[] { null });
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private BeanMetadataElement createMetadataElementByIntrospection(Object object, NamedBeanMap refs,
			ConversionService conversionService) {
		validate(object);
		if (BeanUtils.isSimpleValueType(object.getClass())) {
			String s = null;
			if (object.getClass().isEnum()) {
				// for an enum Spring expects the name !
				s = ((Enum<?>) object).name();
			} else {
				if (conversionService.canConvert(object.getClass(), String.class)) {
					s = conversionService.convert(object, String.class);
				} else {
					s = object.toString();
				}
			}
			TypedStringValue value = new TypedStringValue(s);
			return value;
		} else if (object.getClass().isArray()) {
			ManagedArray array = new ManagedArray(object.getClass().getComponentType().getName(),
					Array.getLength(object));
			for (int i = 0; i < Array.getLength(object); i++) {
				array.add(createMetadataElementByIntrospection(Array.get(object, i), refs, conversionService));
			}
			return array;
		} else if (List.class.isAssignableFrom(object.getClass())) {
			ManagedList<BeanMetadataElement> list = new ManagedList<BeanMetadataElement>();
			List<?> l = (List<?>) object;
			for (Object o : l) {
				list.add(createMetadataElementByIntrospection(o, refs, conversionService));
			}
			return list;
		} else if (Map.class.isAssignableFrom(object.getClass())) {
			ManagedMap<BeanMetadataElement, BeanMetadataElement> map = 
					new ManagedMap<BeanMetadataElement, BeanMetadataElement>();
			Map<?, ?> m = (Map<?, ?>) object;
			for (Map.Entry<?, ?> entry : m.entrySet()) {
				map.put(createMetadataElementByIntrospection(entry.getKey(), refs, conversionService),
						createMetadataElementByIntrospection(entry.getValue(), refs, conversionService));
			}
			return map;
		} else if (Set.class.isAssignableFrom(object.getClass())) {
			ManagedSet<BeanMetadataElement> set = new ManagedSet<BeanMetadataElement>();
			Set<?> s = (Set<?>) object;
			for (Object o : s) {
				set.add(createMetadataElementByIntrospection(o, refs, conversionService));
			}
			return set;
		} else if (conversionService.canConvert(object.getClass(), BeanDefinition.class)) {
			return conversionService.convert(object, BeanDefinition.class);
		} else {
			GenericBeanDefinition def = new GenericBeanDefinition();
			def.setBeanClass(object.getClass());
			RuntimeBeanReference ref = null;
			MutablePropertyValues propertyValues = new MutablePropertyValues();
			for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(object.getClass())) {
				if (descriptor.getWriteMethod() != null) {
					try {
						Object value = descriptor.getReadMethod().invoke(object, (Object[]) null);
						if (value != null) {
							if ("id".equals(descriptor.getName()) || "name".equals(descriptor.getName())) {
								if (refs.containsName((String) value)) {
									ref = new RuntimeBeanReference((String) value);
									break;
								} else {
									def.setAttribute("name", (String) value);
									propertyValues.addPropertyValue(descriptor.getName(),
											createMetadataElementByIntrospection(value, refs, conversionService));
								}
							} else {
								propertyValues.addPropertyValue(descriptor.getName(),
										createMetadataElementByIntrospection(value, refs, conversionService));
							}
						}
					} catch (Exception e) {
						// our contract says to ignore this property
					}
				}
			}
			def.setPropertyValues(propertyValues);
			return ref != null ? ref : def;
		}
	}

	/**
	 * Map of named bean objects.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class NamedBeanMap {

		private Map<String, Object> beans = new HashMap<String, Object>();

		public NamedBeanMap(List<NamedObject> objectList) {
			for (NamedObject object : objectList) {
				if (!beans.containsValue(object.getObject())) {
					beans.put(object.getName(), object.getObject());
				}
			}
		}

		public NamedBeanMap() {
			this(Collections.EMPTY_LIST);
		}

		boolean containsBean(Object o) {
			return beans.containsValue(o);
		}

		boolean containsName(String name) {
			return beans.containsKey(name);
		}

		String getName(Object o) {
			for (String beanName : beans.keySet()) {
				if (beans.get(beanName).equals(o)) {
					return beanName;
				}
			}
			return null;
		}
	}

}
