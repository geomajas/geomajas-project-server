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
package org.geomajas.plugin.vendorspecificpipeline.step;

import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * Common interface for filter step pair that delays the application of the security filter until the layer query has
 * been performed. This avoids passing a complex geographical filter to layers that are not capable of handling it.
 * Notable examples are ESRI WFS servers and Oracle databases.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface DelaySecurityFilterStep extends PipelineStep<GetFeaturesContainer> {

	String SAVED_FILTER_KEY = "savedFilter";

	String SECURITY_FILTER_KEY = "securityFilter";

}
