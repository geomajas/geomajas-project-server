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
package org.geomajas.layer.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.AssociationAttribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.service.DtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * A simple Java beans based feature model.
 * 
 * @author Jan De Moerloose
 */
public class BeanFeatureModel implements FeatureModel {

	private final Logger log = LoggerFactory.getLogger(BeanFeatureModel.class);

	public static final String XPATH_SEPARATOR = "/";

	public static final String SEPARATOR = ".";

	public static final String SEPARATOR_REGEXP = "\\.";

	private Class<?> beanClass;

	private int srid;

	private VectorLayerInfo vectorLayerInfo;

	private boolean wkt;

	private DtoConverterService converterService;

	private Map<String, AttributeInfo> attributeInfoMap = new HashMap<String, AttributeInfo>();

	public BeanFeatureModel(VectorLayerInfo vectorLayerInfo, int srid, DtoConverterService converterService)
			throws LayerException {
		this.vectorLayerInfo = vectorLayerInfo;
		this.converterService = converterService;

		try {
			beanClass = Class.forName(vectorLayerInfo.getFeatureInfo().getDataSourceName());
		} catch (ClassNotFoundException e) {
			throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM, "Feature class "
					+ vectorLayerInfo.getFeatureInfo().getDataSourceName() + " was not found", e);
		}
		this.srid = srid;
		PropertyDescriptor d = BeanUtils.getPropertyDescriptor(beanClass, getGeometryAttributeName());
		Class geometryClass = d.getPropertyType();
		if (Geometry.class.isAssignableFrom(geometryClass)) {
			wkt = false;
		} else if (geometryClass == String.class) {
			wkt = true;
		} else {
			throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM, "Feature "
					+ vectorLayerInfo.getFeatureInfo().getDataSourceName() + " has no valid geometry attribute");
		}

		FeatureInfo featureInfo = vectorLayerInfo.getFeatureInfo();
		attributeInfoMap.put(featureInfo.getIdentifier().getName(), featureInfo.getIdentifier());
		for (AttributeInfo info : featureInfo.getAttributes()) {
			addAttribute(null, info);
		}
	}

	private void addAttribute(String prefix, AttributeInfo info) {
		String name = info.getName();
		if (null != prefix) {
			name = prefix + SEPARATOR + name;
		}
		attributeInfoMap.put(name, info);
		if (info instanceof AssociationAttributeInfo) {
			FeatureInfo association = ((AssociationAttributeInfo) info).getFeature();
			attributeInfoMap.put(name + SEPARATOR + association.getIdentifier().getName(), association.getIdentifier());
			for (AttributeInfo assInfo : association.getAttributes()) {
				addAttribute(name, assInfo);
			}
		}
	}

	public boolean canHandle(Object feature) {
		return beanClass.isInstance(feature);
	}

	public Attribute getAttribute(Object feature, String name) throws LayerException {
		Object attr = getAttributeRecursively(feature, name);
		name = name.replace(XPATH_SEPARATOR, SEPARATOR);
		AttributeInfo attributeInfo = attributeInfoMap.get(name);
		if (null == attributeInfo) {
			throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name);
		}
		try {
			return converterService.toDto(attr, attributeInfo);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
	}

	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		try {
			Map<String, Attribute> attribs = new HashMap<String, Attribute>();
			for (AttributeInfo attribute : getFeatureInfo().getAttributes()) {
				String name = attribute.getName();
				if (!name.equals(getGeometryAttributeName())) {
					Attribute value = this.getAttribute(feature, name);
					attribs.put(name, value);
				}
			}
			return attribs;
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public Geometry getGeometry(Object feature) throws LayerException {
		Object geometry = getAttributeRecursively(feature, getGeometryAttributeName());
		if (!wkt || null == geometry) {
			log.debug("bean.getGeometry {}", geometry);
			return (Geometry) geometry;
		} else {
			try {
				WKTReader reader = new WKTReader(new GeometryFactory(new PrecisionModel(), srid));
				Geometry geom = reader.read((String) geometry);
				log.debug("bean.getGeometry {}", geom);
				return geom;
			} catch (Throwable t) {
				throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM, geometry);
			}
		}
	}

	public String getGeometryAttributeName() throws LayerException {
		return getFeatureInfo().getGeometryType().getName();
	}

	public String getId(Object feature) throws LayerException {
		Object value = getAttributeRecursively(feature, getFeatureInfo().getIdentifier().getName());
		if (null == value) {
			return null;
		} else {
			return value.toString();
		}
	}

	public int getSrid() throws LayerException {
		return srid;
	}

	public Object newInstance() throws LayerException {
		try {
			return beanClass.newInstance();
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public Object newInstance(String id) throws LayerException {
		try {
			Object instance = beanClass.newInstance();
			setId(instance, id);
			return instance;
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public void setId(Object instance, String id) throws LayerException {
		PrimitiveAttributeInfo pai = vectorLayerInfo.getFeatureInfo().getIdentifier();
		Object value;
		switch (pai.getType()) {
			case LONG:
				value = Long.parseLong(id);
				break;
			case STRING:
				value = id;
				break;
			default:
				throw new IllegalStateException("BeanFeatureModel only accepts String and long ids.");
		}
		writeProperty(instance, getFeatureInfo().getIdentifier().getName(), value);
	}

	/** Does not support many-to-one and one-to-many.... */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		UpdateFeatureOperation operation = new UpdateFeatureOperation(feature, getFeatureInfo(), (Map) attributes);
		operation.execute();
	}

	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		if (wkt) {
			WKTWriter writer = new WKTWriter();
			String wktStr = null;
			if (null != geometry) {
				wktStr = writer.write(geometry);
			}
			writeProperty(feature, getGeometryAttributeName(), wktStr);
		} else {
			writeProperty(feature, getGeometryAttributeName(), geometry);
		}
	}

	public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		this.vectorLayerInfo = vectorLayerInfo;
	}

	public FeatureInfo getFeatureInfo() {
		return vectorLayerInfo.getFeatureInfo();
	}

	/**
	 * A recursive getAttribute method. In case a one-to-many is passed, an array will be returned.
	 * 
	 * @param feature The feature wherein to search for the attribute
	 * @param name The attribute's full name. (can be attr1.attr2)
	 * @return Returns the value. In case a one-to-many is passed along the way, an array will be returned.
	 * @throws LayerException oops
	 */
	private Object getAttributeRecursively(Object feature, String name) throws LayerException {
		if (feature == null) {
			return null;
		}
		// Split up properties: the first and the rest.
		name = name.replace(XPATH_SEPARATOR, SEPARATOR);
		String[] properties = name.split(SEPARATOR_REGEXP, 2);

		// Get the first property:
		Object tempFeature = readProperty(feature, properties[0]);

		// Detect if the first property is a collection (one-to-many):
		if (tempFeature instanceof Collection<?>) {
			Collection<?> features = (Collection<?>) tempFeature;
			Object[] values = new Object[features.size()];
			int count = 0;
			for (Object value : features) {
				if (properties.length == 1) {
					values[count++] = value;
				} else {
					values[count++] = getAttributeRecursively(value, properties[1]);
				}
			}
			return values;
		} else { // Else first property is not a collection (one-to-many):
			if (properties.length == 1 || tempFeature == null) {
				return tempFeature;
			} else {
				return getAttributeRecursively(tempFeature, properties[1]);
			}
		}
	}

	/**
	 * Generic attribute operation interface.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	interface Operation {

		/**
		 * Execute the operation
		 * 
		 * @throws LayerException
		 */
		void execute() throws LayerException;

	}

	/**
	 * Operation for updating a {@link Feature}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class UpdateFeatureOperation extends AbstractOperation {

		public UpdateFeatureOperation(Object feature, FeatureInfo featureInfo, Map<String, Attribute<?>> attributes)
				throws LayerException {
			super(feature, featureInfo, attributes);
			addChildOperations();
		}

	}

	/**
	 * Operation for creating a {@link ManyToOneAttribute}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class CreateManyToOneOperation extends AbstractOperation {

		private Object parent;

		private String name;

		public CreateManyToOneOperation(Object parent, AssociationAttributeInfo attributeInfo, AssociationValue value)
				throws LayerException {
			super(null, attributeInfo.getFeature(), value.getAllAttributes());
			this.parent = parent;
			this.name = attributeInfo.getName();
			setObject(newInstance());
			addChildOperations();
		}

		public void execute() throws LayerException {
			writeProperty(parent, name, getObject());
			super.execute();
		}
	}

	/**
	 * Operation for updating a {@link ManyToOneAttribute}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class UpdateManyToOneOperation extends AbstractOperation {

		public UpdateManyToOneOperation(AssociationAttributeInfo attributeInfo, Object existing, AssociationValue value)
				throws LayerException {
			super(existing, attributeInfo.getFeature(), value.getAllAttributes());
			addChildOperations();
		}

	}

	/**
	 * Operation for deleting a {@link ManyToOneAttribute}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class DeleteManyToOneOperation extends AbstractOperation {

		private Object parent;

		private String name;

		public DeleteManyToOneOperation(Object parent, AssociationAttributeInfo attributeInfo) throws LayerException {
			super(null, attributeInfo.getFeature(), null);
			this.parent = parent;
			this.name = attributeInfo.getName();
		}

		public void execute() throws LayerException {
			writeProperty(parent, name, null);
		}
	}

	/**
	 * Operation for creating a {@link OneToManyAttribute}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class CreateOneToManyOperation extends AbstractOperation {

		private Object parent;

		private String name;

		public CreateOneToManyOperation(Object parent, AssociationAttributeInfo attributeInfo) throws LayerException {
			super(null, attributeInfo.getFeature(), null);
			this.parent = parent;
			this.name = attributeInfo.getName();
			setObject(new ArrayList());
		}

		public void execute() throws LayerException {
			writeProperty(parent, name, getObject());
		}
	}

	/**
	 * Operation for deleting a {@link OneToManyAttribute}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class DeleteOneToManyOperation extends AbstractOperation {

		private Object parent;

		private String name;

		public DeleteOneToManyOperation(Object parent, AssociationAttributeInfo attributeInfo) throws LayerException {
			super(null, attributeInfo.getFeature(), null);
			this.parent = parent;
			this.name = attributeInfo.getName();
		}

		public void execute() throws LayerException {
			writeProperty(parent, name, null);
		}
	}

	/**
	 * Operation for removing value from a {@link OneToManyAttribute}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class RemoveManyValue extends AbstractOperation {

		private Object toDelete;

		private Collection<?> existing;

		public RemoveManyValue(Collection<?> existing, Object toDelete) throws LayerException {
			super(existing, null, null);
			this.toDelete = toDelete;
			this.existing = existing;
		}

		public void execute() throws LayerException {
			this.existing.remove(toDelete);
		}
	}

	/**
	 * Operation for updating an existing value of a {@link OneToManyAttribute}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class UpdateManyValue extends AbstractOperation {

		public UpdateManyValue(Object object, AssociationAttributeInfo attributeInfo, AssociationValue associationValue)
				throws LayerException {
			super(object, attributeInfo.getFeature(), associationValue.getAllAttributes());
			addChildOperations();
		}
	}

	/**
	 * Operation for adding a value to a {@link OneToManyAttribute}
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class AddManyValue extends AbstractOperation {

		private Collection existing;

		public AddManyValue(Collection<?> existing, AssociationAttributeInfo attributeInfo,
				AssociationValue associationValue) throws LayerException {
			super(null, attributeInfo.getFeature(), associationValue.getAllAttributes());
			this.existing = existing;
			setObject(newInstance());
			addChildOperations();
		}

		public void execute() throws LayerException {
			existing.add(getObject());
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

		private Object object;

		private Map<String, PrimitiveAttribute<?>> primitives = new HashMap<String, PrimitiveAttribute<?>>();

		private Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();

		private FeatureInfo featureInfo;

		private List<Operation> children = new ArrayList<Operation>();

		public AbstractOperation(FeatureInfo featureInfo, Map<String, Attribute<?>> attributes) throws LayerException {
			this(null, featureInfo, attributes);
		}

		public AbstractOperation(Object object, FeatureInfo featureInfo, Map<String, Attribute<?>> attributes)
				throws LayerException {
			this.object = object;
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
				writeProperty(getObject(), entry.getKey(), entry.getValue().getValue());
			}
			for (Operation operation : children) {
				operation.execute();
			}
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public void addChild(Operation child) {
			children.add(child);
		}

		public void addOneToMany(String name, AssociationAttributeInfo attributeInfo, OneToManyAttribute association)
				throws LayerException {
			Collection<?> existing = readCollectionProperty(getObject(), name);
			if (association.getValue() == null) {
				addChild(new DeleteOneToManyOperation(getObject(), attributeInfo));
			} else {
				// sort on create, update, delete
				Map<Object, Object> existingMap = new HashMap<Object, Object>();
				Set<Object> oldIds = new HashSet<Object>();
				if (existing != null) {
					for (Object object : existing) {
						Object id = readProperty(object, featureInfo.getIdentifier().getName());
						oldIds.add(id);
						existingMap.put(id, object);
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
						CreateOneToManyOperation createOp = new CreateOneToManyOperation(getObject(), attributeInfo);
						addChild(createOp);
						addChild(new AddManyValue((Collection) createOp.getObject(), attributeInfo, associationValue));
					}
				}
				for (Object object : deleteIds) {
					addChild(new RemoveManyValue(existing, object));
				}
			}
		}

		public void addManyToOne(String name, AssociationAttributeInfo attributeInfo, ManyToOneAttribute association)
				throws LayerException {
			Object existing = readProperty(getObject(), name);
			if (association.getValue() == null) {
				addChild(new DeleteManyToOneOperation(getObject(), attributeInfo));
			} else if (existing == null) {
				addChild(new CreateManyToOneOperation(getObject(), attributeInfo, association.getValue()));
			} else {
				addChild(new UpdateManyToOneOperation(attributeInfo, existing, association.getValue()));
			}
		}

		public void addPrimitive(String name, PrimitiveAttribute attribute) {
			primitives.put(name, attribute);
		}

		protected Object newInstance() throws LayerException {
			try {
				Class beanClass = Class.forName(featureInfo.getDataSourceName());
				return beanClass.newInstance();
			} catch (Exception e) {
				throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM, "Feature class "
						+ featureInfo.getDataSourceName() + " could not be instantiated", e);
			}
		}

	}

	protected void writeProperty(Object object, String name, Object value) throws LayerException {
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

	protected Object readProperty(Object object, String name) throws LayerException {
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

	protected Collection<?> readCollectionProperty(Object object, String name) throws LayerException {
		Collection<?> value = null;
		if (object != null) {
			PropertyDescriptor d = BeanUtils.getPropertyDescriptor(object.getClass(), name);
			if (d != null && d.getReadMethod() != null) {
				Method m = d.getReadMethod();
				if (!Modifier.isPublic(m.getDeclaringClass().getModifiers())) {
					m.setAccessible(true);
				}
				try {
					value = (Collection<?>) m.invoke(object);
				} catch (Throwable t) {
					throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
				}
			}
		}
		return value;
	}

}
