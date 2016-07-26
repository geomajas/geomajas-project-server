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
package org.geomajas.internal.filter;

import java.util.Map;
import java.util.regex.Pattern;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geotools.factory.Hints;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;

import com.vividsolutions.jts.geom.Geometry;

/**
 * {@link PropertyAccessorFactory} for accessing {@link InternalFeature} objects. This allows to evaluate Geotools
 * filters on internal Geomajas features. This factory is registered through the SPI mechanism (see
 * META-INF/services/org.geotools.filter.expression.PropertyAccessorFactory).
 * 
 * @author Jan De Moerloose
 * 
 */
public class InternalFeaturePropertyAccessorFactory implements PropertyAccessorFactory {

	private static final PropertyAccessor ACCESSOR = new InternalFeatureAccessor();

	private static final String XPATH_SEPARATOR = "/";

	private static final Pattern ID_PATTERN = Pattern.compile("@(\\w+:)?id");

	private static final Pattern PROPERTY_PATTERN = Pattern.compile("((\\w+)(\\.|/))*(\\w+)");

	@SuppressWarnings("rawtypes")
	public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {
		return ACCESSOR;
	}

	/**
	 * {@link InternalFeature} property accessor. Supports the following xpath expressions for association attributes:
	 * <ul>
	 * <li>
	 * <code>manyToOne/stringAttr</code> : returns the nested primitive attribute <code>stringAttr</code> of the
	 * many-to-one attribute <code>manyToOne</code></li>
	 * <li>
	 * <code>oneToMany[1]/stringAttr</code> : returns the nested primitive attribute <code>stringAttr</code> of the
	 * second attribute value of one-to-many attribute <code>oneToMany</code></li>
	 * </ul>
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private static final class InternalFeatureAccessor implements PropertyAccessor {

		private static final String LEFT_INDEX_BRACKET = "[";

		@SuppressWarnings("rawtypes")
		public boolean canHandle(Object object, String xpath, Class target) {
			return object instanceof InternalFeature;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Object get(Object object, String xpath, Class target) throws IllegalArgumentException {
			AssociationValue assoc = null;
			if (object != null) {
				InternalFeature feature = (InternalFeature) object;
				String[] paths = xpath.split(XPATH_SEPARATOR);
				for (int pathIndex = 0; pathIndex < paths.length - 1; pathIndex++) {
					if (assoc != null) {
						assoc = extractAssociation(assoc.getAllAttributes(), paths[pathIndex]);
					} else if (feature.getAttributes() != null) {
						assoc = extractAssociation((Map) feature.getAttributes(), paths[pathIndex]);
					}
					if (assoc == null) {
						break;
					}
				}
				String lastPath = paths[paths.length - 1];
				if (assoc != null) {
					if (ID_PATTERN.matcher(lastPath).matches()) {
						return assoc.getId().getValue();
					} else {
						return assoc.getAttributeValue(lastPath);
					}
				} else if (paths.length == 1) {
					FeatureModel fm = feature.getLayer().getFeatureModel();
					try {
						if (lastPath.equals(fm.getGeometryAttributeName())) {
							return feature.getGeometry();
						} else if (ID_PATTERN.matcher(lastPath).matches()) {
							return feature.getId();
						} else if (PROPERTY_PATTERN.matcher(lastPath).matches()) {
							return feature.getAttributes().get(lastPath).getValue();
						} else if ("".equals(lastPath) && Geometry.class.isAssignableFrom(target)) {
							return feature.getGeometry();
						} else {
							return null;
						}
					} catch (LayerException e) {
						throw new IllegalArgumentException(e);
					}
				}
			}
			return null;
		}

		private AssociationValue extractAssociation(Map<String, Attribute<?>> attrs, String path) {
			Attribute<?> attribute;
			String name = null;
			int index = 0;
			if (path.contains(LEFT_INDEX_BRACKET)) {
				name = path.substring(0, path.indexOf(LEFT_INDEX_BRACKET));
				index = Integer.parseInt(path.substring(path.indexOf(LEFT_INDEX_BRACKET) + 1, path.length() - 1));
			} else {
				name = path;
			}
			attribute = attrs.get(name);
			if (attribute instanceof OneToManyAttribute) {
				return ((OneToManyAttribute) attribute).getValue().get(index);
			} else if (attribute instanceof ManyToOneAttribute) {
				return ((ManyToOneAttribute) attribute).getValue();
			} else {
				return null;
			}
		}

		@SuppressWarnings("rawtypes")
		public void set(Object object, String xpath, Object value, Class target) throws IllegalArgumentException {
			// TODO: implement me...
		}
	}
}