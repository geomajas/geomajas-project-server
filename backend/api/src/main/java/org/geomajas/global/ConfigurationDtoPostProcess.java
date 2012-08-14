/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.global;

import org.geomajas.annotation.Api;


/**
 * Beans that implement this interface will be called by the geomajas configuration postprocessor. This allows
 * post processing of configuration by plugins or projects after the geomajas postprocessor has run, thus everything
 * is initialized correctly (Spring and Geomajas). 
 * 
 * @author Oliver May
 * @since 1.11.1
 */
@Api(allMethods = true)
public interface ConfigurationDtoPostProcess {
	
	/**
	 * This method is called by the geomajas postprocessor after it's own processing.
	 * 
	 * @param configurationHelper helper for common post-processing tasks.
	 */
	void processConfiguration(ConfigurationHelper configurationHelper);
}
