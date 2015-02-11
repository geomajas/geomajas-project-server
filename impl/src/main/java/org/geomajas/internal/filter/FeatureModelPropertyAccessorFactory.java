/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.filter;

import java.util.regex.Pattern;

import org.geomajas.internal.layer.feature.FeatureModelRegistry;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.FeatureModel;
import org.geotools.factory.Hints;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;

import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * Implementation of the normal GeoTools PropertyAccessorFactory factory. GeoTools can only handle the retrieval of
 * simple attributes, and is therefore not strong enough for the possibilities that Hibernate Spatial gives us. That is
 * where this class comes in. It uses the FeatureModel's of the features to retrieve the correct attributes. This way,
 * we can search for complex attributes in complex objects, still using GeoTools' default Filter.evaluate(Object)
 * function calls.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FeatureModelPropertyAccessorFactory implements PropertyAccessorFactory {

	private static final PropertyAccessor ACCESSOR = new FeatureModelPropertyAccessor();

	public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {
		return ACCESSOR;
	}

	/**
	 * Property accessor for feature models.
	 */
	static class FeatureModelPropertyAccessor implements PropertyAccessor {

		static final Pattern ID_PATTERN = Pattern.compile("@(\\w+:)?id");

		static final Pattern PROPERTY_PATTERN = Pattern.compile("((\\w+)(\\.|/))*((\\w+)|(@(\\w+:)?id))");

		public boolean canHandle(Object object, String xpath, Class target) {
			// there is no need to check against the schema at this point, we will fail later
			FeatureModel fm = FeatureModelRegistry.getRegistry().lookup(object);
			return (fm != null);
		}

		public Object get(Object object, String xpath, Class target) throws IllegalArgumentException {
			FeatureModel fm = FeatureModelRegistry.getRegistry().lookup(object);
			if (null == fm) {
				throw new IllegalArgumentException("Objects of type " + object.getClass().getName() +
						" not registered in FeatureModelRegistry");
			}
			try {
				if (xpath.equals(fm.getGeometryAttributeName())) {
					return fm.getGeometry(object);
				} else if (ID_PATTERN.matcher(xpath).matches()) {
					return fm.getId(object);
				} else if (PROPERTY_PATTERN.matcher(xpath).matches()) {
					return fm.getAttribute(object, xpath).getValue();
				} else if ("".equals(xpath) && Geometry.class.isAssignableFrom(target)) {
					return fm.getGeometry(object);
				} else {
					return null;
				}
			} catch (LayerException e) {
				throw new IllegalArgumentException(e);
			}
		}

		public void set(Object object, String xpath, Object value, Class target) throws IllegalArgumentException {
			throw new IllegalArgumentException("feature is immutable, only use property access for filtering");
		}
	}

}