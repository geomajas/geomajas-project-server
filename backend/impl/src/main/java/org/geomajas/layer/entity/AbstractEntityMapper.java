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
package org.geomajas.layer.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationAttribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;

/**
 * Partial implementation of {@link EntityMapper}. This class handles the merge operation in a generic way. Subclasses
 * should implement the {@link #asEntity(Object)} and {@link #findOrCreateEntity(String, Object)} methods.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class AbstractEntityMapper implements EntityMapper {

	protected FeatureInfo featureInfo;

	public AbstractEntityMapper(FeatureInfo featureInfo) {
		this.featureInfo = featureInfo;
	}

	public void mergeEntity(Object object, Map<String, Attribute<?>> attributes) throws LayerException {
		UpdateEntityOperation operation = new UpdateEntityOperation(asEntity(object), attributes);
		operation.execute();
	}

	public abstract Entity findOrCreateEntity(String dataSourceName, Object id) throws LayerException;

	public abstract Entity asEntity(Object object);

	/**
	 * Operation interface.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface Operation {

		void execute() throws LayerException;
	}

	/**
	 * Operation for updating the top entity belonging to the {@link FeatureInfo} of this class.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class UpdateEntityOperation extends AbstractOperation {

		public UpdateEntityOperation(Entity entity, Map<String, Attribute<?>> attributes) throws LayerException {
			super(entity, featureInfo, attributes);
			addChildOperations();
		}

	}

	/**
	 * Operation for creating a {@link ManyToOneAttribute}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class CreateManyToOneOperation extends AbstractOperation {

		private Entity parent;

		private String name;

		public CreateManyToOneOperation(Entity parent, AssociationAttributeInfo attributeInfo, AssociationValue value)
				throws LayerException {
			super(null, attributeInfo.getFeature(), value.getAllAttributes());
			this.parent = parent;
			this.name = attributeInfo.getName();
			Object id = null;
			if (value.getId() != null) {
				id = value.getId().getValue();
			}
			setEntity(findOrCreateEntity(attributeInfo.getFeature().getDataSourceName(), id));
			addChildOperations();
		}

		public void execute() throws LayerException {
			parent.setChild(name, getEntity());
			super.execute();
		}
	}

	/**
	 * Operation for updating a {@link ManyToOneAttribute}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class UpdateManyToOneOperation extends AbstractOperation {

		public UpdateManyToOneOperation(AssociationAttributeInfo attributeInfo, Entity existing, AssociationValue value)
				throws LayerException {
			super(existing, attributeInfo.getFeature(), value.getAllAttributes());
			addChildOperations();
		}

	}

	/**
	 * Operation for deleting a {@link ManyToOneAttribute}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class DeleteManyToOneOperation extends AbstractOperation {

		private Entity parent;

		private String name;

		public DeleteManyToOneOperation(Entity parent, AssociationAttributeInfo attributeInfo) throws LayerException {
			super(null, attributeInfo.getFeature(), null);
			this.parent = parent;
			this.name = attributeInfo.getName();
		}

		public void execute() throws LayerException {
			parent.setChild(name, null);
		}
	}

	/**
	 * Operation for deleting a {@link OneToManyAttribute}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class DeleteOneToManyOperation extends AbstractOperation {

		private Entity parent;

		private String name;

		public DeleteOneToManyOperation(Entity parent, AssociationAttributeInfo attributeInfo) throws LayerException {
			super(null, attributeInfo.getFeature(), null);
			this.parent = parent;
			this.name = attributeInfo.getName();
		}

		public void execute() throws LayerException {
			parent.setChild(name, null);
		}
	}

	/**
	 * Operation for removing value from a {@link OneToManyAttribute}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class RemoveManyValue extends AbstractOperation {

		private Entity toDelete;

		private EntityCollection existing;

		public RemoveManyValue(EntityCollection existing, Entity toDelete) throws LayerException {
			super(null, null, null);
			this.toDelete = toDelete;
			this.existing = existing;
		}

		public void execute() throws LayerException {
			existing.removeEntity(toDelete);
		}
	}

	/**
	 * Operation for updating an existing value of a {@link OneToManyAttribute}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class UpdateManyValue extends AbstractOperation {

		public UpdateManyValue(Entity entity, AssociationAttributeInfo attributeInfo, AssociationValue associationValue)
				throws LayerException {
			super(entity, attributeInfo.getFeature(), associationValue.getAllAttributes());
			addChildOperations();
		}
	}

	/**
	 * Operation for adding a value to a {@link OneToManyAttribute}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class AddManyValue extends AbstractOperation {

		private EntityCollection collection;

		public AddManyValue(EntityCollection collection, AssociationAttributeInfo attributeInfo,
				AssociationValue associationValue) throws LayerException {
			super(null, attributeInfo.getFeature(), associationValue.getAllAttributes());
			this.collection = collection;
			Object id = null;
			if (associationValue.getId() != null) {
				id = associationValue.getId().getValue();
			}
			setEntity(findOrCreateEntity(attributeInfo.getFeature().getDataSourceName(), id));
			addChildOperations();
		}

		public void execute() throws LayerException {
			collection.addEntity(getEntity());
			super.execute();
		}
	}

	/**
	 * Abstract attribute operation that is able to generate a tree of operations and recursively execute it. The tree
	 * consists of all elementary operations needed to merge a new attribute map with a map of existing attributes as
	 * reflected in the bean properties.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	abstract class AbstractOperation implements Operation {

		private Entity entity;

		private Map<String, PrimitiveAttribute<?>> primitives = new HashMap<String, PrimitiveAttribute<?>>();

		private Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();

		private FeatureInfo featureInfo;

		private List<Operation> children = new ArrayList<Operation>();

		public AbstractOperation(FeatureInfo featureInfo, Map<String, Attribute<?>> attributes) throws LayerException {
			this(null, featureInfo, attributes);
		}

		public AbstractOperation(Entity entity, FeatureInfo featureInfo, Map<String, Attribute<?>> attributes)
				throws LayerException {
			this.entity = entity;
			this.featureInfo = featureInfo;
			this.attributes = attributes;
		}

		protected void addChildOperations() throws LayerException {
			Map<String, AssociationAttributeInfo> infoMap = new HashMap<String, AssociationAttributeInfo>();
			for (AttributeInfo attributeInfo : featureInfo.getAttributes()) {
				if (attributeInfo instanceof AssociationAttributeInfo) {
					infoMap.put(attributeInfo.getName(), (AssociationAttributeInfo) attributeInfo);
				}
			}
			for (Map.Entry<String, Attribute<?>> entry : attributes.entrySet()) {
				Attribute<?> attribute = entry.getValue();
				if (attribute.isPrimitive()) {
					addPrimitive(entry.getKey(), (PrimitiveAttribute<?>) attribute);
				} else {
					AssociationAttribute<?> association = (AssociationAttribute<?>) attribute;
					switch (association.getType()) {
						case MANY_TO_ONE:
							addManyToOne(entry.getKey(), infoMap.get(entry.getKey()), (ManyToOneAttribute) association);
							break;
						case ONE_TO_MANY:
							addOneToMany(entry.getKey(), infoMap.get(entry.getKey()), (OneToManyAttribute) association);
							break;
					}
				}
			}
		}

		public void execute() throws LayerException {
			for (Map.Entry<String, PrimitiveAttribute<?>> entry : primitives.entrySet()) {
				entity.setPrimitiveAttribute(entry.getKey(), entry.getValue().getValue());
			}
			for (Operation operation : children) {
				operation.execute();
			}
		}

		public Entity getEntity() {
			return entity;
		}

		public void setEntity(Entity entity) {
			this.entity = entity;
		}

		public void addChild(Operation child) {
			children.add(child);
		}

		public void addOneToMany(String name, AssociationAttributeInfo attributeInfo, OneToManyAttribute association)
				throws LayerException {
			EntityCollection collection = entity.getChildCollection(name);
			if (association.getValue() == null) {
				addChild(new DeleteOneToManyOperation(getEntity(), attributeInfo));
			} else {
				// sort on create, update, delete
				Map<Object, Entity> existingMap = new HashMap<Object, Entity>();
				Set<Object> oldIds = new HashSet<Object>();
				if (collection != null) {
					for (Entity entity : collection) {
						String idName = attributeInfo.getFeature().getIdentifier().getName();
						Object id = entity.getId(idName);
						oldIds.add(id);
						existingMap.put(id, entity);
					}
				}
				Set<Object> newIds = new HashSet<Object>();
				for (AssociationValue associationValue : association.getValue()) {
					if (associationValue.getId() != null && !associationValue.getId().isEmpty()) {
						newIds.add(associationValue.getId().getValue());
					}
				}
				Set<Object> updateIds = new HashSet<Object>(newIds);
				Set<Object> deleteIds = new HashSet<Object>(oldIds);
				updateIds.retainAll(oldIds);
				deleteIds.removeAll(newIds);

				for (AssociationValue associationValue : association.getValue()) {
					Object id = null;
					if (associationValue.getId() != null) {
						id = associationValue.getId().getValue();
					}
					if (updateIds.contains(id)) {
						addChild(new UpdateManyValue(existingMap.get(id), attributeInfo, associationValue));
					} else {
						addChild(new AddManyValue(collection, attributeInfo, associationValue));
					}
				}
				for (Object id : deleteIds) {
					addChild(new RemoveManyValue(collection, existingMap.get(id)));
				}
			}
		}

		public void addManyToOne(String name, AssociationAttributeInfo attributeInfo, ManyToOneAttribute association)
				throws LayerException {
			Entity existing = entity.getChild(name);
			if (association.getValue() == null) {
				addChild(new DeleteManyToOneOperation(getEntity(), attributeInfo));
			} else if (existing == null) {
				addChild(new CreateManyToOneOperation(getEntity(), attributeInfo, association.getValue()));
			} else {
				addChild(new UpdateManyToOneOperation(attributeInfo, existing, association.getValue()));
			}
		}

		public void addPrimitive(String name, PrimitiveAttribute attribute) {
			primitives.put(name, attribute);
		}

	}

}
