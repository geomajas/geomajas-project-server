/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.profiling.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.geomajas.annotation.Api;
import org.geomajas.layer.Layer;
import org.geomajas.project.profiling.service.ProfilingContainer;

/**
 * Interceptor which automatically handles the profiling for a method.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class LayerProfilingInterceptor implements MethodInterceptor {
	
	private ProfilingContainer profilingContainer;

	/**
	 * Set the {@link ProfilingContainer} to use for the profiling.
	 *
	 * @param profilingContainer profiling container
	 * @since 1.0.0
	 */
	@Api
	public void setProfilingContainer(ProfilingContainer profilingContainer) {
		this.profilingContainer = profilingContainer;
	}

	@Override
	// CHECKSTYLE THROWS_THROWABLE: OFF
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = invocation.proceed();
		Object thisObject = invocation.getThis();
		if (thisObject instanceof Layer)  {
			String group = ((Layer) thisObject).getId();
			profilingContainer.register(group, System.currentTimeMillis() - start);
		}
		return result;
	}
	// CHECKSTYLE THROWS_THROWABLE: ON

}
