/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
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

import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.entity.Entity;
import org.geomajas.layer.entity.EntityCollection;
import org.geomajas.layer.entity.EntityMapper;
import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

/**
 * Hibernate based implementation of {@link EntityMapper} for the {@link HibernateLayer}.
 * <p/>
 *  Values are read/written according to the Hibernate access type.
 * 
 * @author Jan De Moerloose
 */
public class HibernateEntityMapper implements EntityMapper {

	private final SessionFactory sessionFactory;

	/**
	 * Contructor.
	 *
	 * @param sessionFactory session factory
	 */
	public HibernateEntityMapper(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Entity findOrCreateEntity(String dataSourceName, Object id) throws LayerException {
		return new HibernateEntity(dataSourceName, id);
	}

	@Override
	public Entity asEntity(Object object) throws LayerException {
		if (object == null) {
			throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
		return new HibernateEntity(object);
	}

	/**
	 * Entity that provides access to a Hibernate entity.
	 * 
	 * @author Jan De Moerloose
	 */
	class HibernateEntity implements Entity {

		private Object object;

		private final ClassMetadata metadata;

		/**
		 * Construct an entity for the Hibernate object with given id.
		 *
		 * @param dataSourceName data source name
		 * @param id object id
		 */
		public HibernateEntity(String dataSourceName, Object id) {
			metadata = sessionFactory.getClassMetadata(dataSourceName);
			if (id != null) {
				object = sessionFactory.getCurrentSession().get(dataSourceName, (Serializable) id);
			}
			if (object == null) {
				object = metadata.instantiate(null, (SessionImplementor) sessionFactory.getCurrentSession());
			}
		}

		/**
		 * Create a entity for the given Hibernate managed object.
		 *
		 * @param object hibernate managed object
		 */
		public HibernateEntity(Object object) {
			this.object = object;
			metadata = sessionFactory.getClassMetadata(object.getClass());
		}

		/**
		 * Get the Hibernate managed object for this entity.
		 *
		 * @return hibernate managed object
		 */
		public Object getObject() {
			return object;
		}

		@Override
		public Object getId(String name) throws LayerException {
			return metadata.getIdentifier(object, (SessionImplementor) sessionFactory.getCurrentSession());
		}

		@Override
		public Entity getChild(String name) throws LayerException {
			Object child = (object == null ? null : metadata.getPropertyValue(object, name, EntityMode.POJO));
			return child == null ? null : new HibernateEntity(child);
		}

		@Override
		public void setChild(String name, Entity entity) throws LayerException {
			if (entity != null) {
				metadata.setPropertyValue(object, name, ((HibernateEntity) entity).getObject(), EntityMode.POJO);
			} else {
				metadata.setPropertyValue(object, name, null, EntityMode.POJO);
			}
		}

		@Override
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
				String entityName = ct.getAssociatedEntityName((SessionFactoryImplementor) sessionFactory);
				ClassMetadata childMetadata = sessionFactory.getClassMetadata(entityName);
				return new HibernateEntityCollection(metadata, childMetadata, object, coll);
			} else {
				throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM);
			}
		}

		@Override
		public void setAttribute(String name, Object value) throws LayerException {
			metadata.setPropertyValue(object, name, value, EntityMode.POJO);
		}

		@Override
		public Object getAttribute(String name) throws LayerException {
			if (metadata.getIdentifierPropertyName().equals(name)) {
				return getId(name);
			}
			return object == null ? null : metadata.getPropertyValue(object, name, EntityMode.POJO);
		}

	}

	/**
	 * {@link EntityCollection} that provides access to a Hibernate collection.
	 * 
	 * @author Jan De Moerloose
	 */
	class HibernateEntityCollection implements EntityCollection {

		private String parentName;

		private final ClassMetadata metadata;

		private final Object parent;

		private final Collection objects;

		/**
		 * Construct a entity collection.
		 *
		 * @param parentMetadata parent meta data
		 * @param childMetadata child meta data
		 * @param parent parent object
		 * @param objects child objects
		 */
		public HibernateEntityCollection(ClassMetadata parentMetadata, ClassMetadata childMetadata, Object parent,
				Collection<?> objects) {
			this.objects = objects;
			int i = 0;
			for (Type type : childMetadata.getPropertyTypes()) {
				if (type instanceof ManyToOneType) {
					ManyToOneType mto = (ManyToOneType) type;
					if (mto.getAssociatedEntityName().equals(parentMetadata.getEntityName())) {
						parentName = childMetadata.getPropertyNames()[i];
					}
				}
				i++;
			}
			this.metadata = childMetadata;
			this.parent = parent;
		}

		@Override
		public Iterator<Entity> iterator() {
			Collection<Entity> entities = new ArrayList<Entity>();
			for (Object bean : objects) {
				entities.add(new HibernateEntity(bean));
			}
			return entities.iterator();
		}

		@Override
		@SuppressWarnings("unchecked")
		public void addEntity(Entity entity) throws LayerException {
			Object object = ((HibernateEntity) entity).getObject();
			// assign parent to many-to-one
			if (parentName != null) {
				metadata.setPropertyValue(object, parentName, parent, EntityMode.POJO);
			}
			objects.add(object);
		}

		@Override
		public void removeEntity(Entity entity) throws LayerException {
			Object object = ((HibernateEntity) entity).getObject();
			// remove parent from many-to-one
			if (parentName != null) {
				metadata.setPropertyValue(object, parentName, null, EntityMode.POJO);
			}
			objects.remove(object);
		}

	}

}
