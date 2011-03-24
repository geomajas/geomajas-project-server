package org.geomajas.internal.service;

import org.geotools.factory.Hints;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;

/**
 * PropertyAccessorFactory for testing. Works for all features that implement TestPropertyAccess.
 * 
 * @author Jan De Moerloose
 * 
 */
public class FilterServiceTestPropertyAccessorFactory implements PropertyAccessorFactory {

	public class FilterServicePropertyAccessor implements PropertyAccessor {

		public boolean canHandle(Object object, String xpath, Class target) {
			return object instanceof TestPropertyAccess;
		}

		public Object get(Object object, String xpath, Class target) throws IllegalArgumentException {
			return ((TestPropertyAccess) object).get(xpath, target);
		}

		public void set(Object object, String xpath, Object value, Class target) throws IllegalArgumentException {
		}

	}

	public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {
		return new FilterServicePropertyAccessor();
	}

}
