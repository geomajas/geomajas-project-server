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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedArray;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Implementation of bean definition persistence service that writes bean definitions in standard XML format.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class BeanDefinitionWriterServiceImpl implements BeanDefinitionWriterService {

	private FileSystemResource baseResource;

	private static final String START_TAG = "<beans xmlns=\"http://www.springframework.org/schema/beans\"\r\n"
			+ "	xmlns:mvc=\"http://www.springframework.org/schema/mvc\" "
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
			+ "	xmlns:context=\"http://www.springframework.org/schema/context\"\r\n"
			+ "	xmlns:util=\"http://www.springframework.org/schema/util\"\r\n" + "	xsi:schemaLocation=\"\r\n"
			+ "        http://www.springframework.org/schema/beans\r\n"
			+ "        http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\r\n"
			+ "        http://www.springframework.org/schema/mvc\r\n"
			+ "        http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd\r\n"
			+ "        http://www.springframework.org/schema/context\r\n"
			+ "        http://www.springframework.org/schema/context/spring-context-3.0.xsd\r\n"
			+ "        http://www.springframework.org/schema/util\r\n"
			+ "		http://www.springframework.org/schema/util/spring-util-2.0.xsd\">\r\n" + "\r\n" + "";

	private static final String END_TAG = "</beans>";

	public BeanDefinitionWriterServiceImpl() throws IOException {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		File tempDir = new File(baseDir, "geomajasContext");
		if (tempDir.exists()) {
			if (tempDir.isDirectory()) {
				baseResource = new FileSystemResource(tempDir.getPath() + File.separator);
			}
		} else {
			if (tempDir.mkdir()) {
				baseResource = new FileSystemResource(tempDir.getPath() + File.separator);
			}
		}
		if (baseResource == null) {
			throw new IOException("Could not create temporary context directory");
		}
	}

	public FileSystemResource getBaseResource() {
		return baseResource;
	}

	public void setBaseResource(FileSystemResource baseResource) {
		this.baseResource = baseResource;
	}

	public void persist(String key, BeanDefinitionHolder beanDefinition) throws RuntimeConfigException {
		persist(key, Collections.singletonList(beanDefinition));
	}

	public void persist(String key, List<BeanDefinitionHolder> beans) throws RuntimeConfigException {
		FileWriter fw;
		try {
			fw = new FileWriter(getBaseResource().createRelative(key + ".xml").getFile());
			fw.write(START_TAG);
			XStream stream = createStream();
			for (BeanDefinitionHolder bean : beans) {
				stream.toXML(bean, fw);
			}
			fw.write(END_TAG);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			throw new RuntimeConfigException(e, RuntimeConfigException.BEAN_PERSIST_FAILED, key);
		}
	}

	public void delete(String key) throws RuntimeConfigException {
		try {
			getBaseResource().createRelative(key + ".xml").getFile().delete();
		} catch (IOException e) {
			throw new RuntimeConfigException(e, RuntimeConfigException.BEAN_DELETE_FAILED, key);
		}
	}
	
	public String toString(List<BeanDefinitionHolder> beans) {
		StringWriter sw;
		sw = new StringWriter();
		sw.write(START_TAG);
		XStream stream = createStream();
		for (BeanDefinitionHolder bean : beans) {
			stream.toXML(bean, sw);
		}
		sw.write(END_TAG);
		return sw.toString();
	}

	private XStream createStream() {
		XStream stream = new XStream();
		stream.registerConverter(new BeanDefinitionConverter(stream.getMapper()));
		stream.registerConverter(new BeanDefinitionHolderConverter(stream.getMapper()));
		stream.registerConverter(new TypedStringValueConverter());
		stream.registerConverter(new ManagedCollectionConverter(stream.getMapper()));
		stream.registerConverter(new ManagedMapConverter(stream.getMapper()));
		stream.registerConverter(new RuntimeBeanReferenceConverter());
		stream.alias("map", ManagedMap.class);
		stream.alias("list", ManagedList.class);
		stream.alias("set", ManagedSet.class);
		stream.alias("array", ManagedArray.class);
		stream.aliasType("bean", BeanDefinition.class);
		stream.alias("bean", BeanDefinitionHolder.class);
		stream.alias("ref", RuntimeBeanReference.class);
		return stream;
	};

	/**
	 * Xstream converter for ManagedCollection class.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class ManagedCollectionConverter extends AbstractCollectionConverter {

		public ManagedCollectionConverter(Mapper mapper) {
			super(mapper);
		}

		public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
			Collection list = (Collection) source;
			for (Object o : list) {
				writeItem(o, context, writer);
			}
		}

		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			return null;
		}

		public boolean canConvert(Class type) {
			return type.equals(ManagedList.class) || type.equals(ManagedArray.class) || type.equals(ManagedSet.class);
		}
	}

	/**
	 * Xstream converter for ManagedMap class.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class ManagedMapConverter extends AbstractCollectionConverter {

		public ManagedMapConverter(Mapper mapper) {
			super(mapper);
		}

		public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
			ManagedMap<?, ?> map = (ManagedMap<?, ?>) source;
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				writer.startNode("entry");
				writer.startNode("key");
				if (entry.getKey().getClass().equals(TypedStringValue.class)) {
					writer.startNode("value");
					writer.setValue(((TypedStringValue) entry.getKey()).getValue());
					writer.endNode();
				} else {
					writeItem(entry.getKey(), context, writer);
				}
				writer.endNode();
				if (entry.getValue().getClass().equals(TypedStringValue.class)) {
					writer.startNode("value");
					writer.setValue(((TypedStringValue) entry.getValue()).getValue());
					writer.endNode();
				} else {
					writeItem(entry.getValue(), context, writer);
				}
				writer.endNode();
			}
		}

		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			return null;
		}

		public boolean canConvert(Class type) {
			return type.equals(ManagedMap.class);
		}
	}

	/**
	 * Xstream converter for TypedStringValue class.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class TypedStringValueConverter implements Converter {

		public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
			TypedStringValue value = (TypedStringValue) source;
			writer.addAttribute("value", value.getValue());
		}

		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			return null;
		}

		public boolean canConvert(Class type) {
			return type.equals(TypedStringValue.class);
		}

	}

	/**
	 * Xstream converter for BeanDefinitionHolder class.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class BeanDefinitionHolderConverter extends AbstractCollectionConverter {

		public BeanDefinitionHolderConverter(Mapper mapper) {
			super(mapper);
		}

		public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
			BeanDefinitionHolder holder = (BeanDefinitionHolder) source;
			BeanDefinition definition = holder.getBeanDefinition();
			writer.addAttribute("class", definition.getBeanClassName());
			writer.addAttribute("name", holder.getBeanName());
			for (PropertyValue property : definition.getPropertyValues().getPropertyValueList()) {
				writer.startNode("property");
				writer.addAttribute("name", property.getName());
				if (property.getValue().getClass().equals(TypedStringValue.class)) {
					context.convertAnother(property.getValue());
				} else {
					writeItem(property.getValue(), context, writer);
				}
				writer.endNode();
			}
		}

		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			return null;
		}

		public boolean canConvert(Class type) {
			return type.equals(BeanDefinitionHolder.class);
		}

	}

	/**
	 * Xstream converter for BeanDefinition class.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class BeanDefinitionConverter extends AbstractCollectionConverter {

		public BeanDefinitionConverter(Mapper mapper) {
			super(mapper);
		}

		public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
			BeanDefinition definition = (BeanDefinition) source;
			writer.addAttribute("class", definition.getBeanClassName());
			for (PropertyValue property : definition.getPropertyValues().getPropertyValueList()) {
				writer.startNode("property");
				writer.addAttribute("name", property.getName());
				if (property.getValue().getClass().equals(TypedStringValue.class)) {
					context.convertAnother(property.getValue());
				} else {
					writeItem(property.getValue(), context, writer);
				}
				writer.endNode();
			}

		}

		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			return null;
		}

		public boolean canConvert(Class type) {
			return BeanDefinition.class.isAssignableFrom(type);
		}
	}

	/**
	 * XStream converter for RuntimeBeanReference.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class RuntimeBeanReferenceConverter implements Converter {

		public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
			RuntimeBeanReference value = (RuntimeBeanReference) source;
			writer.addAttribute("bean", value.getBeanName());
		}

		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			return null;
		}

		public boolean canConvert(Class type) {
			return RuntimeBeanReference.class.equals(type);
		}
	}

}
