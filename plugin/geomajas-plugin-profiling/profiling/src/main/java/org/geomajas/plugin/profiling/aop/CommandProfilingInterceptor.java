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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.geomajas.annotation.Api;
import org.geomajas.project.profiling.service.ProfilingContainer;

/**
 * Interceptor which automatically handles the profiling for a method.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class CommandProfilingInterceptor implements MethodInterceptor {

	private ProfilingContainer profilingContainer;

	/**
	 * Set the {@link org.geomajas.project.profiling.service.ProfilingContainer} to use for the profiling.
	 *
	 * @param profilingContainer profiling container
	 * @since 1.0.0
	 */
	@Api
	public void setProfilingContainer(ProfilingContainer profilingContainer) {
		this.profilingContainer = profilingContainer;
	}

	/** {@inheritDoc} */
	// CHECKSTYLE THROWS_THROWABLE: OFF
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = invocation.proceed();
		Object[] arguments = invocation.getArguments();
		if (null != arguments && arguments.length > 0)  {
			String group = "";
			if (null != arguments[0]) {
				group = arguments[0].toString();
			}
			profilingContainer.register(group, System.currentTimeMillis() - start);
		}
		return result;
	}
	// CHECKSTYLE THROWS_THROWABLE: ON

}
