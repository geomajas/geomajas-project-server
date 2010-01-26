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

package org.geomajas.internal.filter;

import org.geomajas.internal.layer.feature.FeatureModelRegistry;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.FeatureModel;
import org.geotools.factory.Hints;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;

import java.util.regex.Pattern;

/**
 * <p>
 * Implementation of the normal geotools PropertyAccessorFactory factory.
 * Geotools can only handle the retrieval of simple attributes, and is therefore
 * not strong enough for the possibilities that Hibernate Spatial gives us. That
 * is where this class comes in. It uses the FeatureModel's of the features to
 * retrieve the correct attributes. This way, we can search for complex
 * attributes in complex objects, still using the default geotools
 * Filter.evaluate(Object) function calls.
 * </p>
 *
 * @author Pieter De Graef
 */
public class FeatureModelPropertyAccessorFactory implements PropertyAccessorFactory {

	private static final PropertyAccessor ATTRIBUTE_ACCESS = new FeatureModelAttributeAccessor();

	private static final PropertyAccessor GEOMETRY_ACCESS = new FeatureModelGeometryAccessor();

	private static final PropertyAccessor FID_ACCESS = new FeatureModelFidAccessor();

	private static final Pattern ID_PATTERN = Pattern.compile("@(\\w+:)?id");

	public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {

		if (xpath == null) {
			return null;
		}

		if ("".equals(xpath)) {
			return GEOMETRY_ACCESS;
		}

		// check for fid access
		if (ID_PATTERN.matcher(xpath).matches()) {
			return FID_ACCESS;
		}

		return ATTRIBUTE_ACCESS;
	}

	/**
	 * ???
	 */
	static class FeatureModelAttributeAccessor implements PropertyAccessor {

		public boolean canHandle(Object object, String xpath, Class target) {
			FeatureModel fm = FeatureModelRegistry.getRegistry().lookup(object);
			return (fm != null);
		}

		public Object get(Object object, String xpath, Class target) throws IllegalArgumentException {
			FeatureModel fm = FeatureModelRegistry.getRegistry().lookup(object);
			try {
				return fm.getAttribute(object, xpath);
			} catch (LayerException e) {
				throw new IllegalArgumentException(e);
			}
		}

		public void set(Object object, String xpath, Object value, Class target)
				throws IllegalAttributeException, IllegalArgumentException {
		}
	}

	/**
	 * ???
	 */
	static class FeatureModelGeometryAccessor implements PropertyAccessor {

		public boolean canHandle(Object object, String xpath, Class target) {
			FeatureModel fm = FeatureModelRegistry.getRegistry().lookup(object);
			return (fm != null);
		}

		public Object get(Object object, String xpath, Class target) throws IllegalArgumentException {
			FeatureModel fm = FeatureModelRegistry.getRegistry().lookup(object);
			try {
				return fm.getGeometry(object);
			} catch (LayerException e) {
				throw new IllegalArgumentException(e);
			}
		}

		public void set(Object object, String xpath, Object value, Class target)
				throws IllegalAttributeException, IllegalArgumentException {
		}
	}

	/**
	 * ???
	 */
	static class FeatureModelFidAccessor implements PropertyAccessor {

		public boolean canHandle(Object object, String xpath, Class target) {
			FeatureModel fm = FeatureModelRegistry.getRegistry().lookup(object);
			return (fm != null);
		}

		public Object get(Object object, String xpath, Class target) throws IllegalArgumentException {
			FeatureModel fm = FeatureModelRegistry.getRegistry().lookup(object);
			try {
				return fm.getId(object);
			} catch (LayerException e) {
				throw new IllegalArgumentException(e);
			}
		}

		public void set(Object object, String xpath, Object value, Class target)
				throws IllegalAttributeException, IllegalArgumentException {
		}
	}
}