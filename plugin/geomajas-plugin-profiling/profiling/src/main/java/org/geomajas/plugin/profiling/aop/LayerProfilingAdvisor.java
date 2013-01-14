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

package org.geomajas.plugin.profiling.aop;

import org.geomajas.layer.Layer;
import org.geomajas.layer.feature.FeatureModel;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;

/**
 * Pointcut to indicate which layer/feature model methods need to be advised.
 *
 * @author Joachim Van der Auwera
 */
public class LayerProfilingAdvisor extends StaticMethodMatcherPointcutAdvisor {

	public LayerProfilingAdvisor() {
		setClassFilter(new ImplementsLayerFilter());
	}
	
	public boolean matches(Method method, Class<?> targetClass) {
		String name = method.getName();
		return "paint".equals(name) // RasterLayers
				|| "create".equals(name) // VectorLayer methods...
				|| "saveOrUpdate".equals(name)
				|| "read".equals(name)
				|| "delete".equals(name)
				|| "getElements".equals(name)
				|| "getBounds".equals(name)
				|| "getAttributes".equals(name) // FeatureModel or VectorLayerAssociationSupport
				|| "getAttribute".equals(name) // FeatureModel methods...
				|| "getGeometry".equals(name)
				|| "setAttributes".equals(name)
				|| "setGeometry".equals(name)
				|| "newInstance".equals(name)
				;
	}

	/**
	 * Class filter which selects classes which implement {@link Layer}.
	 */
	private static class ImplementsLayerFilter implements ClassFilter {

		/**{@inheritDoc} */
		public boolean matches(Class<?> clazz) {
			return Layer.class.isAssignableFrom(clazz) || FeatureModel.class.isAssignableFrom(clazz);
		}
	}
}
