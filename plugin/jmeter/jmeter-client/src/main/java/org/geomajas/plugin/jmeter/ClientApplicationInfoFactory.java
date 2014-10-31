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
package org.geomajas.plugin.jmeter;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.geomajas.configuration.client.ClientApplicationInfo;


public interface ClientApplicationInfoFactory {

	ClientApplicationInfo create(PerformanceCommandService commandDispatcher, JavaSamplerContext context);
}
