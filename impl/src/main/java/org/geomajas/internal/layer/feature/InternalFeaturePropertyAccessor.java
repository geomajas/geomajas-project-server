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
package org.geomajas.internal.layer.feature;

import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

/**
 * {@link PropertyAccessor} that provides read access to {@link InternalFeature} attributes/id and
 * {@link AssociationValue} attributes/id. After adding this {@link PropertyAccessor} to the SpEL evaluation context,
 * {@link InternalFeature} attributes/id (including nested attributes/id) can be referenced as if they were JavaBean
 * properties:
 * <ul>
 * <li><code>myAttr</code> : evaluates to the value of this primitive attribute
 * <li><code>country.code</code> : evaluates to the nested code value of the many-to-one attribute country
 * <li><code>id</code> (case insensitive) : evaluates to the identifier
 * </ul>
 * 
 * @author Jan De Moerloose
 */
public class InternalFeaturePropertyAccessor implements PropertyAccessor {

	public static final String ID_PROPERTY_NAME = "id";

	/**
	 * {@inheritDoc}
	 */
	public Class[] getSpecificTargetClasses() {
		return new Class[] { InternalFeature.class, AssociationValue.class };
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
		if (null == target) {
			return false;
		}
		if (target instanceof InternalFeature) {
			InternalFeature feature = (InternalFeature) target;
			return feature.getAttributes().containsKey(name) || ID_PROPERTY_NAME.equalsIgnoreCase(name);
		} else if (target instanceof AssociationValue) {
			AssociationValue associationValue = (AssociationValue) target;
			return associationValue.getAllAttributes().containsKey(name) || ID_PROPERTY_NAME.equalsIgnoreCase(name);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
		if (target == null) {
			throw new AccessException("Cannot read property of null target");
		}
		if (target instanceof InternalFeature) {
			InternalFeature feature = (InternalFeature) target;
			if (feature.getAttributes().containsKey(name)) {
				Attribute<?> attribute = feature.getAttributes().get(name);
				return new TypedValue(attribute.getValue());
			} else if (ID_PROPERTY_NAME.equalsIgnoreCase(ID_PROPERTY_NAME)) {
				return new TypedValue(feature.getId());
			} else {
				throw new AccessException("Unknown attribute " + name + "for layer " + feature.getLayer().getId());
			}
		} else if (target instanceof AssociationValue) {
			AssociationValue associationValue = (AssociationValue) target;
			if (associationValue.getAllAttributes().containsKey(name)) {
				Attribute<?> attribute = associationValue.getAllAttributes().get(name);
				return new TypedValue(attribute.getValue());
			} else if (ID_PROPERTY_NAME.equalsIgnoreCase(ID_PROPERTY_NAME)) {
				Attribute<?> attribute = associationValue.getId();
				return new TypedValue(attribute.getValue());
			} else {
				throw new AccessException("Unknown attribute " + name + " for association " + target);
			}
		} else {
			throw new AccessException("Cannot read property " + name + "from class " + target.getClass());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
		throw new AccessException("InternalFeaturePropertyAccess is read-only");
	}

}
