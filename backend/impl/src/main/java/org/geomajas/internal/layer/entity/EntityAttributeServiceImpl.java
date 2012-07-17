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
package org.geomajas.internal.layer.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.EditableAttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.entity.Entity;
import org.geomajas.layer.entity.EntityAttributeService;
import org.geomajas.layer.entity.EntityCollection;
import org.geomajas.layer.entity.EntityMapper;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationAttribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link EntityAttributeService}. This class maps attributes in a generic way using an
 * operation tree.
 * 
 * @author Jan De Moerloose
 */
@Component
public class EntityAttributeServiceImpl implements EntityAttributeService {

	public static final String ATTRIBUTE_SEPARATOR = "[/.]+";

	@Autowired
	private DtoConverterService dtoConverterService;

	/** {@inheritDoc} */
	public void setAttributes(Object object, FeatureInfo featureInfo, EntityMapper mapper,
			Map<String, Attribute<?>> attributes) throws LayerException {
		UpdateEntityOperation operation = new UpdateEntityOperation(mapper, mapper.asEntity(object), featureInfo,
				attributes);
		operation.execute();
	}

	/** {@inheritDoc} */
	public Attribute<?> getAttribute(Object feature, FeatureInfo featureInfo, EntityMapper mapper, String name)
			throws LayerException {
		String[] path = name.split(ATTRIBUTE_SEPARATOR);
		return getRecursiveAttribute(mapper.asEntity(feature), featureInfo, path);
	}

	private Attribute<?> getRecursiveAttribute(Entity entity, FeatureInfo featureInfo, String[] path)
			throws LayerException {
		String name = path[0];
		AssociationAttributeInfo associationAttributeInfo = null;
		// check attribute type
		Set<String> names = new HashSet<String>();
		// check for id
		PrimitiveAttributeInfo identifier = featureInfo.getIdentifier();
		if (identifier.getName().equals(name) || FilterService.ATTRIBUTE_ID.equalsIgnoreCase(name)) {
			try {
				return dtoConverterService
						.toDto(entity == null ? null : entity.getId(identifier.getName()), identifier);
			} catch (GeomajasException e) {
				throw new LayerException(e, ExceptionCode.CONVERSION_PROBLEM);
			}
		}
		for (AbstractAttributeInfo attributeInfo : featureInfo.getAttributes()) {
			names.add(attributeInfo.getName());
			if (attributeInfo.getName().equals(name)) {
				if (attributeInfo instanceof AssociationAttributeInfo) {
					associationAttributeInfo = (AssociationAttributeInfo) attributeInfo;
				} else if (attributeInfo instanceof PrimitiveAttributeInfo) {
					// primitive, return the attribute
					PrimitiveAttributeInfo primitiveAttributeInfo = (PrimitiveAttributeInfo) attributeInfo;
					try {
						return dtoConverterService.toDto(entity == null ? null : entity.getAttribute(name),
								primitiveAttributeInfo);
					} catch (GeomajasException e) {
						throw new LayerException(e, ExceptionCode.CONVERSION_PROBLEM);
					}
				} else {
					throw new IllegalStateException("UnHandled attribute type, " + attributeInfo.getClass().getName());
				}

			}
		}
		// association attribute
		if (associationAttributeInfo == null) {
			throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name, names);
		} else if (path.length > 1) {
			// go deeper
			return getRecursiveAttribute(entity == null ? null : entity.getChild(name),
					associationAttributeInfo.getFeature(), Arrays.copyOfRange(path, 1, path.length));
		} else {
			switch (associationAttributeInfo.getType()) {
				case MANY_TO_ONE:
					ManyToOneAttribute manyToOne = new ManyToOneAttribute();
					if (entity != null) {
						Entity oneEntity = entity.getChild(associationAttributeInfo.getName());
						AssociationValue value = getAssociationValue(oneEntity, associationAttributeInfo);
						manyToOne.setValue(value);
					}
					return manyToOne;
				case ONE_TO_MANY:
					OneToManyAttribute oneToMany = new OneToManyAttribute();
					if (entity != null) {
						EntityCollection children = entity.getChildCollection(associationAttributeInfo.getName());
						List<AssociationValue> values = new ArrayList<AssociationValue>();
						for (Entity manyEntity : children) {
							values.add(getAssociationValue(manyEntity, associationAttributeInfo));
						}
						oneToMany.setValue(values);
					}
					return oneToMany;
			}
			return null;
		}
	}

	private AssociationValue getAssociationValue(Entity entity, AssociationAttributeInfo associationAttributeInfo)
			throws LayerException {
		if (entity == null) {
			return null;
		}
		PrimitiveAttributeInfo idInfo = associationAttributeInfo.getFeature().getIdentifier();
		FeatureInfo childInfo = associationAttributeInfo.getFeature();
		PrimitiveAttribute<?> id;
		try {
			id = (PrimitiveAttribute) dtoConverterService.toDto(entity.getId(idInfo.getName()), idInfo);
		} catch (GeomajasException e) {
			throw new LayerException(e, ExceptionCode.CONVERSION_PROBLEM);
		}
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		for (AbstractAttributeInfo attributeInfo : childInfo.getAttributes()) {
			attributes.put(attributeInfo.getName(),
					getRecursiveAttribute(entity, childInfo, new String[] { attributeInfo.getName() }));
		}
		return new AssociationValue(id, attributes, false);
	}

	/**
	 * Operation interface.
	 * 
	 * @author Jan De Moerloose
	 */
	public interface Operation {

		/**
		 * Execute the operation.
		 *
		 * @throws LayerException oops
		 */
		void execute() throws LayerException;
	}

	/**
	 * Operation for updating the top entity belonging to the {@link FeatureInfo} of this class.
	 * 
	 * @author Jan De Moerloose
	 */
	class UpdateEntityOperation extends AbstractOperation {

		public UpdateEntityOperation(EntityMapper mapper, Entity entity, FeatureInfo featureInfo,
				Map<String, Attribute<?>> attributes) throws LayerException {
			super(mapper, entity, featureInfo, attributes);
			addChildOperations();
		}

	}

	/**
	 * Operation for creating a {@link ManyToOneAttribute}.
	 * 
	 * @author Jan De Moerloose
	 */
	class CreateManyToOneOperation extends AbstractOperation {

		private final Entity parent;

		private final String name;

		public CreateManyToOneOperation(EntityMapper mapper, Entity parent, AssociationAttributeInfo attributeInfo,
				AssociationValue value) throws LayerException {
			super(mapper, null, attributeInfo.getFeature(), value.getAllAttributes());
			this.parent = parent;
			this.name = attributeInfo.getName();
			Object id = null;
			if (value.getId() != null) {
				id = value.getId().getValue();
			}
			setEntity(getMapper().findOrCreateEntity(attributeInfo.getFeature().getDataSourceName(), id));
			addChildOperations();
		}

		@Override
		public void execute() throws LayerException {
			parent.setChild(name, getEntity());
			super.execute();
		}
	}

	/**
	 * Operation for updating a {@link ManyToOneAttribute}.
	 * 
	 * @author Jan De Moerloose
	 */
	class UpdateManyToOneOperation extends AbstractOperation {

		public UpdateManyToOneOperation(EntityMapper mapper, AssociationAttributeInfo attributeInfo, Entity existing,
				AssociationValue value) throws LayerException {
			super(mapper, existing, attributeInfo.getFeature(), value.getAllAttributes());
			addChildOperations();
		}

	}

	/**
	 * Operation for deleting a {@link ManyToOneAttribute}.
	 * 
	 * @author Jan De Moerloose
	 */
	class DeleteManyToOneOperation extends AbstractOperation {

		private final Entity parent;

		private final String name;

		public DeleteManyToOneOperation(EntityMapper mapper, Entity parent, AssociationAttributeInfo attributeInfo)
				throws LayerException {
			super(mapper, null, attributeInfo.getFeature(), null);
			this.parent = parent;
			this.name = attributeInfo.getName();
		}

		@Override
		public void execute() throws LayerException {
			parent.setChild(name, null);
		}
	}

	/**
	 * Operation for deleting a {@link OneToManyAttribute}.
	 * 
	 * @author Jan De Moerloose
	 */
	class DeleteOneToManyOperation extends AbstractOperation {

		private final Entity parent;

		private final String name;

		public DeleteOneToManyOperation(EntityMapper mapper, Entity parent, AssociationAttributeInfo attributeInfo)
				throws LayerException {
			super(mapper, null, attributeInfo.getFeature(), null);
			this.parent = parent;
			this.name = attributeInfo.getName();
		}

		@Override
		public void execute() throws LayerException {
			parent.setChild(name, null);
		}
	}

	/**
	 * Operation for removing value from a {@link OneToManyAttribute}.
	 * 
	 * @author Jan De Moerloose
	 */
	class RemoveManyValue extends AbstractOperation {

		private final Entity toDelete;

		private final EntityCollection existing;

		public RemoveManyValue(EntityMapper mapper, EntityCollection existing, Entity toDelete) throws LayerException {
			super(mapper, null, null, null);
			this.toDelete = toDelete;
			this.existing = existing;
		}

		@Override
		public void execute() throws LayerException {
			existing.removeEntity(toDelete);
		}
	}

	/**
	 * Operation for updating an existing value of a {@link OneToManyAttribute}.
	 * 
	 * @author Jan De Moerloose
	 */
	class UpdateManyValue extends AbstractOperation {

		public UpdateManyValue(EntityMapper mapper, Entity entity, AssociationAttributeInfo attributeInfo,
				AssociationValue associationValue) throws LayerException {
			super(mapper, entity, attributeInfo.getFeature(), associationValue.getAllAttributes());
			addChildOperations();
		}
	}

	/**
	 * Operation for adding a value to a {@link OneToManyAttribute}.
	 * 
	 * @author Jan De Moerloose
	 */
	class AddManyValue extends AbstractOperation {

		private final EntityCollection collection;

		public AddManyValue(EntityMapper mapper, EntityCollection collection, AssociationAttributeInfo attributeInfo,
				AssociationValue associationValue) throws LayerException {
			super(mapper, null, attributeInfo.getFeature(), associationValue.getAllAttributes());
			this.collection = collection;
			Object id = null;
			if (associationValue.getId() != null) {
				id = associationValue.getId().getValue();
			}
			setEntity(mapper.findOrCreateEntity(attributeInfo.getFeature().getDataSourceName(), id));
			addChildOperations();
		}

		@Override
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
	 */
	abstract class AbstractOperation implements Operation {

		private Entity entity;

		private final Map<String, PrimitiveAttribute<?>> primitives = new HashMap<String, PrimitiveAttribute<?>>();

		private final Map<String, Attribute<?>> attributes;

		private final FeatureInfo featureInfo;

		private final EntityMapper mapper;

		private List<Operation> children = new ArrayList<Operation>();

		public AbstractOperation(EntityMapper mapper, Entity entity, FeatureInfo featureInfo,
				Map<String, Attribute<?>> attributes) throws LayerException {
			this.mapper = mapper;
			this.entity = entity;
			this.featureInfo = featureInfo;
			this.attributes = attributes;
		}

		protected void addChildOperations() throws LayerException {
			Map<String, AssociationAttributeInfo> associationMap = new HashMap<String, AssociationAttributeInfo>();
			Map<String, PrimitiveAttributeInfo> primitiveMap = new HashMap<String, PrimitiveAttributeInfo>();
			for (AbstractAttributeInfo attributeInfo : featureInfo.getAttributes()) {
				if (attributeInfo instanceof EditableAttributeInfo &&
						((EditableAttributeInfo) attributeInfo).isEditable()) {
					if (attributeInfo instanceof AssociationAttributeInfo) {
						associationMap.put(attributeInfo.getName(), (AssociationAttributeInfo) attributeInfo);
					} else if (attributeInfo instanceof PrimitiveAttributeInfo) {
						primitiveMap.put(attributeInfo.getName(), (PrimitiveAttributeInfo) attributeInfo);
					}
				}
			}
			for (Map.Entry<String, Attribute<?>> entry : attributes.entrySet()) {
				Attribute<?> attribute = entry.getValue();
				if (primitiveMap.containsKey(entry.getKey())) {
					addPrimitive(entry.getKey(), (PrimitiveAttribute<?>) attribute);
				} else if (associationMap.containsKey(entry.getKey())) {
					AssociationAttribute<?> association = (AssociationAttribute<?>) attribute;
					AssociationAttributeInfo associationAttributeInfo = associationMap.get(entry.getKey());
					switch (associationAttributeInfo.getType()) {
						case MANY_TO_ONE:
							association = (association == null ? new ManyToOneAttribute() : association);
							addManyToOne(entry.getKey(), associationMap.get(entry.getKey()),
									(ManyToOneAttribute) association);
							break;
						case ONE_TO_MANY:
							association = (association == null ? new OneToManyAttribute() : association);
							addOneToMany(entry.getKey(), associationMap.get(entry.getKey()),
									(OneToManyAttribute) association);
							break;
					}
				}
			}
		}

		/** {@inheritDoc} */
		public void execute() throws LayerException {
			for (Map.Entry<String, PrimitiveAttribute<?>> entry : primitives.entrySet()) {
				Object value = (entry.getValue() == null ? null : entry.getValue().getValue());
				entity.setAttribute(entry.getKey(), value);
			}
			for (Operation operation : children) {
				operation.execute();
			}
		}

		public EntityMapper getMapper() {
			return mapper;
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
				addChild(new DeleteOneToManyOperation(getMapper(), getEntity(), attributeInfo));
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
						addChild(new UpdateManyValue(getMapper(), existingMap.get(id), attributeInfo,
								associationValue));
					} else {
						addChild(new AddManyValue(getMapper(), collection, attributeInfo,
								associationValue));
					}
				}
				for (Object id : deleteIds) {
					addChild(new RemoveManyValue(getMapper(), collection, existingMap.get(id)));
				}
			}
		}

		public void addManyToOne(String name, AssociationAttributeInfo attributeInfo, ManyToOneAttribute association)
				throws LayerException {
			Entity existing = entity.getChild(name);
			AssociationValue value = association.getValue();
			if (value == null) {
				addChild(new DeleteManyToOneOperation(getMapper(), getEntity(), attributeInfo));
			} else if (existing == null) {
				addChild(new CreateManyToOneOperation(getMapper(), getEntity(), attributeInfo, association.getValue()));
			} else {
				Object id = existing.getId(attributeInfo.getFeature().getIdentifier().getName());
				if (value.getId() == null || value.getId().isEmpty() || !value.getId().getValue().equals(id)) {
					addChild(new CreateManyToOneOperation(getMapper(), getEntity(), attributeInfo,
							association.getValue()));
				} else {
					addChild(new UpdateManyToOneOperation(getMapper(), attributeInfo, existing, value));
				}
			}
		}

		public void addPrimitive(String name, PrimitiveAttribute attribute) {
			primitives.put(name, attribute);
		}

	}

}
