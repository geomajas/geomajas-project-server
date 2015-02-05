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

package org.geomajas.service.pipeline;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;

/**
 * Special pipeline step which is used fo weaving a base pipeline and extensions steps.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 */
@Api(allMethods = true)
public class PipelineHook implements PipelineStep {

	@NotNull
	private String id;

	/**
	 * Set the id for the hook. This is used for identify the position to include extensions.
	 *
	 * @param id pipeline hook id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the id for the hook. This is used for identify the position to include extensions.
	 *
	 * @return pipeline hook id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Execute this step in the pipeline.
	 * <p/>
	 * This is a no-op as the extension occurs while getting the pipeline, not during execution.
	 *
	 * @param context contains a map of objects which are used as shared memory between the pipeline steps
	 * @param response response object for the pipeline service
	 * @throws GeomajasException any exception which may have been throws during the execution
	 */
	public final void execute(PipelineContext context, Object response) throws GeomajasException {
		// nothing to do, this only needs the id
	}
}
