/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.entity.AbstractEntityMapper;
import org.geomajas.layer.entity.Entity;
import org.geomajas.layer.entity.EntityCollection;
import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;

/**
 * Hibernate based implementation of {@link AbstractEntityMapper} for the {@link HibernateLayer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class HibernateEntityMapper extends AbstractEntityMapper {

	private SessionFactory sessionFactory;

	public HibernateEntityMapper(FeatureInfo featureInfo, SessionFactory sessionFactory) {
		super(featureInfo);
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Entity findOrCreateEntity(String dataSourceName, Object id) throws LayerException {
		return new HibernateEntity(dataSourceName, id);
	}

	@Override
	public Entity asEntity(Object object) {
		return new HibernateEntity(object);
	}

	/**
	 * {@link Entity} that provides access to a Hibernate entity.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class HibernateEntity implements Entity {

		private Object object;

		private ClassMetadata metadata;

		public HibernateEntity(String dataSourceName, Object id) {
			metadata = sessionFactory.getClassMetadata(dataSourceName);
			if (id != null) {
				object = sessionFactory.getCurrentSession().get(dataSourceName, (Serializable) id);
			}
			if (object == null) {
				object = metadata.instantiate(null, (SessionImplementor) sessionFactory.getCurrentSession());
			}
		}

		public HibernateEntity(Object object) {
			this.object = object;
			metadata = sessionFactory.getClassMetadata(object.getClass());
		}

		public Object getObject() {
			return object;
		}

		public Object getId(String name) throws LayerException {
			return metadata.getIdentifier(object, (SessionImplementor) sessionFactory.getCurrentSession());
		}

		public Entity getChild(String name) throws LayerException {
			Object child = metadata.getPropertyValue(object, name, EntityMode.POJO);
			return child == null ? null : new HibernateEntity(child);
		}

		public void setChild(String name, Entity entity) throws LayerException {
			metadata.setPropertyValue(object, name, ((HibernateEntity) entity).getObject(), EntityMode.POJO);
		}

		public EntityCollection getChildCollection(String name) throws LayerException {
			Type type = metadata.getPropertyType(name);
			if (type instanceof CollectionType) {
				CollectionType ct = (CollectionType) type;
				Collection coll = (Collection) metadata.getPropertyValue(object, name, EntityMode.POJO);
				if (coll == null) {
					// normally should not happen, hibernate instantiates it automatically
					coll = (Collection) ct.instantiate(0);
					metadata.setPropertyValue(object, name, coll, EntityMode.POJO);
				}
				return new HibernateEntityCollection(coll, ct);
			} else {
				throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM);
			}
		}

		public void setPrimitiveAttribute(String name, Object value) throws LayerException {
			metadata.setPropertyValue(object, name, value, EntityMode.POJO);
		}

	}

	/**
	 * {@link EntityCollection} that provides access to a Hibernate collection.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class HibernateEntityCollection implements EntityCollection {

		private CollectionType type;

		private Collection objects;

		public HibernateEntityCollection(Collection<?> objects, CollectionType type) {
			this.objects = objects;
			this.type = type;
		}

		public Iterator<Entity> iterator() {
			Collection<Entity> entities = new ArrayList<Entity>();
			for (Object bean : objects) {
				entities.add(new HibernateEntity(bean));
			}
			return entities.iterator();
		}

		public void addEntity(Entity entity) throws LayerException {
			// TODO check parent ?
			Object object = ((HibernateEntity) entity).getObject();
			objects.add(object);
		}

		public void removeEntity(Entity entity) throws LayerException {
			Object object = ((HibernateEntity) entity).getObject();
			objects.remove(object);
		}

	}

}
