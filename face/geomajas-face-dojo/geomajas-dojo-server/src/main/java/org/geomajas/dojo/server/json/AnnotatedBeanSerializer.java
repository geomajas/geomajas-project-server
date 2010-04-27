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
package org.geomajas.dojo.server.json;

import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;
import org.geomajas.global.Json;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.service.BeanNameSimplifier;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Json serializer which considers the @Json annotations.
 *
 * @author Jan De Moerloose
 */
@Component("dojo.server.json.AnnotatedBeanSerializer")
public class AnnotatedBeanSerializer extends AbstractSerializer {

	private static final long serialVersionUID = 1;

	private final Logger log = LoggerFactory.getLogger(AnnotatedBeanSerializer.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private BeanNameSimplifier beanNameSimplifier;

	private static final Map<Class, BeanData> BEAN_CACHE = new ConcurrentHashMap<Class, BeanData>();

	private static final Class[] SERIALIZABLE_CLASSES = new Class[] {};

	private static final Class[] JSON_CLASSES = new Class[] {};

	public Class[] getSerializableClasses() {
		return SERIALIZABLE_CLASSES;
	}

	public Class[] getJSONClasses() {
		return JSON_CLASSES;
	}

	public boolean canSerialize(Class clazz, Class jsonClazz) {
		return (!clazz.isArray() && !clazz.isPrimitive() && (jsonClazz == null ||
				jsonClazz == JSONObject.class));
	}

	/**
	 * ???
	 */
	private static class BeanData {

		// in absence of getters and setters, these fields are
		// public to allow subclasses to access.
		private BeanInfo beanInfo;

		private HashMap<String, Method> readableProps;

		private HashMap<String, Method> writableProps;

		public BeanInfo getBeanInfo() {
			return beanInfo;
		}

		public void setBeanInfo(BeanInfo beanInfo) {
			this.beanInfo = beanInfo;
		}

		public HashMap<String, Method> getReadableProps() {
			return readableProps;
		}

		public void setReadableProps(HashMap<String, Method> readableProps) {
			this.readableProps = readableProps;
		}

		public HashMap<String, Method> getWritableProps() {
			return writableProps;
		}

		public void setWritableProps(HashMap<String, Method> writableProps) {
			this.writableProps = writableProps;
		}
	}

	/**
	 * ???
	 */
	public static class BeanSerializerState {

		// in absence of getters and setters, these fields are
		// public to allow subclasses to access.

		// Circular reference detection
		private HashSet beanSet = new HashSet();

		public HashSet getBeanSet() {
			return beanSet;
		}

		public void setBeanSet(HashSet beanSet) {
			this.beanSet = beanSet;
		}
	}

	private BeanData analyzeBean(Class clazz) throws IntrospectionException {
		log.debug("analyzing {}", clazz.getName());
		BeanData bd = new BeanData();
		bd.beanInfo = Introspector.getBeanInfo(clazz, clazz.isEnum() ? Enum.class : Object.class);
		bd.readableProps = new HashMap<String, Method>();
		bd.writableProps = new HashMap<String, Method>();
		PropertyDescriptor[] props = bd.beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor prop : props) {
			Method writeMethod = prop.getWriteMethod();
			if (writeMethod != null) {
				Json json = writeMethod.getAnnotation(Json.class);
				if (json == null || json.serialize()) {
					bd.writableProps.put(prop.getName(), writeMethod);
				} else {
					log.debug("skipping property {} for {}", prop.getName(), clazz.getName());
				}
			}
			Method readMethod = prop.getReadMethod();
			// due to a bug in the JAXB spec, Boolean getters are using isXXX
			// i.o. getXXX as
			// expected by bean introspectors
			if (readMethod == null) {
				if (writeMethod != null && writeMethod.getParameterTypes()[0].equals(Boolean.class)) {
					if (writeMethod.getName().startsWith("set")) {
						String isMethodName = "is" + writeMethod.getName().substring(3);
						try {
							readMethod = clazz.getMethod(isMethodName);
							if (!readMethod.getReturnType().equals(Boolean.class)) {
								readMethod = null;
							}
						} catch (Exception e) {
							log.error("discarding:" + e.getMessage());
						}
					}
				}
			}
			if (readMethod != null) {
				Json json = readMethod.getAnnotation(Json.class);
				if (json == null || json.serialize()) {
					bd.readableProps.put(prop.getName(), readMethod);
				}
			}
		}
		if (clazz.isEnum()) {
			try {
				bd.readableProps.put("value", clazz.getMethod("value"));
			} catch (Exception e) {
				try {
					bd.readableProps.put("value", Enum.class.getMethod("name"));
				} catch (Exception e1) {
					log.warn("cannot extract value of enum " + clazz.getName());
				}
			}
		}
		return bd;
	}

	private BeanData getBeanData(Class clazz) throws IntrospectionException {
		BeanData bd = BEAN_CACHE.get(clazz);
		if (bd == null) {
			bd = analyzeBean(clazz);
			BEAN_CACHE.put(clazz, bd);
		}
		return bd;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		BeanData bd;
		try {
			bd = getBeanData(clazz);
		} catch (IntrospectionException e) {
			throw new UnmarshallException(clazz.getName() + " is not a bean");
		}

		int match = 0, mismatch = 0;
		Iterator i = bd.writableProps.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry ent = (Map.Entry) i.next();
			String prop = (String) ent.getKey();
			if (jso.has(prop)) {
				match++;
			} else {
				mismatch++;
			}
		}
		if (match == 0) {
			throw new UnmarshallException("bean has no matches");
		}

		ObjectMatch m = null, tmp;
		i = jso.keys();
		while (i.hasNext()) {
			String field = (String) i.next();
			Method setMethod = bd.writableProps.get(field);
			if (setMethod != null) {
				try {
					Class[] param = setMethod.getParameterTypes();
					if (param.length != 1) {
						throw new UnmarshallException("bean " + clazz.getName() + " method "
								+ setMethod.getName() + " does not have one arg");
					}
					tmp = ser.tryUnmarshall(state, param[0], jso.get(field));
					if (m == null) {
						m = tmp;
					} else {
						m = m.max(tmp);
					}
				} catch (UnmarshallException e) {
					throw new UnmarshallException("bean " + clazz.getName() + " " + e.getMessage());
				}
			} else {
				mismatch++;
			}
		}
		return m.max(new ObjectMatch(mismatch));
	}

	public Object unmarshall(SerializerState state, Class clazz, Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		BeanData bd;
		try {
			bd = getBeanData(clazz);
		} catch (IntrospectionException e) {
			throw new UnmarshallException(clazz.getName() + " is not a bean");
		}
		log.debug("instantiating {}", clazz.getName());
		Object instance;
		try {
			String beanName = beanNameSimplifier.simplify(clazz.getName());
			if (applicationContext.containsBean(beanName)) {
				instance = applicationContext.getBean(beanName);
			} else {
				log.debug("instantiating " + clazz.getName());
				instance = clazz.newInstance();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new UnmarshallException("can't instantiate bean " + clazz.getName() + ": " + e.getMessage());
		}
		Object[] invokeArgs = new Object[1];
		Object fieldVal;
		Iterator i = jso.keys();
		while (i.hasNext()) {
			String field = (String) i.next();
			Method setMethod = bd.writableProps.get(field);
			if (setMethod != null) {
				try {
					Class param = setMethod.getParameterTypes()[0];
					if (instance instanceof PrimitiveAttribute && "value".equals(field)) {
						log.debug("replace 'value' type for PrimitiveAttribute");
						switch(((PrimitiveAttribute) instance).getType()) {
							case BOOLEAN:
								param = Boolean.class;
								break;
							case DATE:
								param = Date.class;
								break;
							case DOUBLE:
								param = Double.class;
								break;
							case FLOAT:
								param = Float.class;
								break;
							case INTEGER:
								param = Integer.class;
								break;
							case LONG:
								param = Long.class;
								break;
							case SHORT:
								param = Short.class;
								break;
							case CURRENCY:
							case IMGURL:
							case URL:
							case STRING:
								param = String.class;
								break;
							default:
								throw new UnmarshallException("Unknown type of PrimitiveAttribute " +
										((PrimitiveAttribute) instance).getType());
						}
					}
					fieldVal = ser.unmarshall(state, param, jso.get(field));
				} catch (UnmarshallException e) {
					throw new UnmarshallException("bean " + clazz.getName() + " " + e.getMessage());
				}
				log.debug("invoking {}({})", setMethod.getName(), fieldVal);
				invokeArgs[0] = fieldVal;
				try {
					setMethod.invoke(instance, invokeArgs);
				} catch (Throwable e) {
					if (e instanceof InvocationTargetException) {
						e = ((InvocationTargetException) e).getTargetException();
					}
					throw new UnmarshallException("bean " + clazz.getName() + "can't invoke "
							+ setMethod.getName() + ": " + e.getMessage());
				}
			}
		}
		return instance;
	}

	public Object marshall(SerializerState state, Object o) throws MarshallException {
		BeanSerializerState beanState;
		try {
			beanState = (BeanSerializerState) state.get(BeanSerializerState.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarshallException("bean serializer internal error : " + e.getMessage());
		}
		Integer identity = new Integer(System.identityHashCode(o));
		if (beanState.beanSet.contains(identity)) {
			throw new MarshallException("circular reference");
		}
		beanState.beanSet.add(identity);

		BeanData bd = null;
		try {
			bd = getBeanData(o.getClass());
		} catch (IntrospectionException e) {
			throw new MarshallException(o.getClass().getName() + " is not a bean");
		}

		JSONObject val = new JSONObject();
		if (ser.getMarshallClassHints()) {
			val.put("javaClass", o.getClass().getName());
		}
		Iterator i = bd.readableProps.entrySet().iterator();
		Object[] args = new Object[0];
		Object result;
		while (i.hasNext()) {
			Map.Entry ent = (Map.Entry) i.next();
			String prop = (String) ent.getKey();
			Method getMethod = (Method) ent.getValue();
			log.debug("invoking {}()", getMethod.getName());
			try {
				result = getMethod.invoke(o, args);
			} catch (Throwable e) {
				if (e instanceof InvocationTargetException) {
					e = ((InvocationTargetException) e).getTargetException();
				}
				throw new MarshallException("bean " + o.getClass().getName() + " can't invoke "
						+ getMethod.getName() + ": " + e.getMessage());
			}
			try {
				if (result != null || ser.getMarshallNullAttributes()) {

					val.put(prop, ser.marshall(state, result));
				}
			} catch (MarshallException e) {
				throw new MarshallException("bean " + o.getClass().getName() + " " + e.getMessage());
			}
		}

		beanState.beanSet.remove(identity);
		return val;
	}
}