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

package org.geomajas.plugin.rasterizing.step;

import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * Abstract base class for rasterizing pipeline steps.
 *
 * @author Joachim Van der Auwera
 */
public abstract class AbstractRasterizingStep implements PipelineStep<RasterizingContainer> {

	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
