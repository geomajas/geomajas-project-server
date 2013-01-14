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
package org.geomajas.layer.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.entity.Entity;
import org.geomajas.layer.entity.EntityCollection;
import org.geomajas.layer.entity.EntityMapper;
import org.springframework.beans.BeanUtils;

/**
 * JavaBeans based implementation of {@link AbstractEntityMapper} for the {@link BeanLayer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class BeanEntityMapper implements  EntityMapper {

	public Entity findOrCreateEntity(String dataSourceName, Object id) throws LayerException {
		return new BeanEntity(dataSourceName, id);
	}

	public Entity asEntity(Object object) {
		return new BeanEntity(object);
	}

	/**
	 * {@link Entity} that provides access to a JavaBean.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class BeanEntity implements Entity {

		private Object bean;

		public BeanEntity(Object bean) {
			this.bean = bean;
		}

		public BeanEntity(String dataSourceName, Object id) throws LayerException {
			try {
				bean = Class.forName(dataSourceName).newInstance();
				// hard-coded
				if (bean instanceof ManyToOneAttributeBean) {
					List values = ManyToOneAttributeBean.manyToOneValues();
					for (Object value : values) {
						ManyToOneAttributeBean b = (ManyToOneAttributeBean) value;
						if (b.getId().equals(id)) {
							bean = b;
						}
					}
				}
			} catch (Exception e) { // NOSONAR
				throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM, "Could not instantiate entity");
			}
		}

		public Object getBean() {
			return bean;
		}

		public Object getId(String name) throws LayerException {
			return readProperty(bean, name);
		}

		public Entity getChild(String name) throws LayerException {
			Object child = readProperty(bean, name);
			return child == null ? null : new BeanEntity(child);
		}

		public void setChild(String name, Entity entity) throws LayerException {
			writeProperty(bean, name, entity == null ? null : ((BeanEntity) entity).getBean());
		}

		public EntityCollection getChildCollection(String name) throws LayerException {
			Collection collection = (Collection) readProperty(bean, name);
			if (collection == null) {
				collection = new ArrayList();
				writeProperty(bean, name, collection);
			}
			return new BeanEntityCollection(collection);
		}

		public Object getAttribute(String name) throws LayerException {
			return readProperty(bean, name);
		}

		public void setAttribute(String name, Object value) throws LayerException {
			writeProperty(bean, name, value);
		}

		private Object readProperty(Object object, String name) throws LayerException {
			if (object != null) {
				PropertyDescriptor d = BeanUtils.getPropertyDescriptor(object.getClass(), name);
				if (d != null && d.getReadMethod() != null) {
					BeanUtils.getPropertyDescriptor(object.getClass(), name);
					Method m = d.getReadMethod();
					if (!Modifier.isPublic(m.getDeclaringClass().getModifiers())) {
						m.setAccessible(true);
					}
					Object value;
					try {
						value = m.invoke(object);
					} catch (Throwable t) {
						throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
					}
					return value;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		private void writeProperty(Object object, String name, Object value) throws LayerException {
			if (object != null) {
				PropertyDescriptor d = BeanUtils.getPropertyDescriptor(object.getClass(), name);
				if (d != null && d.getWriteMethod() != null) {
					Method m = d.getWriteMethod();
					if (!Modifier.isPublic(m.getDeclaringClass().getModifiers())) {
						m.setAccessible(true);
					}
					try {
						m.invoke(object, value);
					} catch (Throwable t) {
						throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
					}
				}
			}
		}


	}

	/**
	 * {@link EntityCollection} that provides access to a collection of JavaBeans.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class BeanEntityCollection implements EntityCollection {

		private Collection<Object> beans;

		public BeanEntityCollection(Collection<Object> beans) {
			this.beans = beans;
		}

		public Iterator<Entity> iterator() {
			Collection<Entity> entities = new ArrayList<Entity>();
			for (Object bean : beans) {
				entities.add(new BeanEntity(bean));
			}
			return entities.iterator();
		}

		public void addEntity(Entity entity) throws LayerException {
			beans.add(((BeanEntity) entity).getBean());
		}

		public void removeEntity(Entity entity) throws LayerException {
			beans.remove(((BeanEntity) entity).getBean());
		}

	}

}
