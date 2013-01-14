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

package org.geomajas.service.pipeline;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.global.GeomajasException;

/**
 * Definition of one execution step in a pipeline.
 *
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface PipelineStep<RESPONSE> {

	/**
	 * Get the id for the step. This is used for possible skipping and looping in the pipeline.
	 *
	 * @return pipeline step id
	 */
	String getId();

	/**
	 * Execute this step in the pipeline.
	 * <p/>
	 * This is expected to modify and transform both the parameters and response objects.
	 *
	 * @param context contains a map of objects which are used as shared memory between the pipeline steps
	 * @param response response object for the pipeline service
	 * @throws GeomajasException any exception which may have been throws during the execution
	 */
	void execute(PipelineContext context, RESPONSE response) throws GeomajasException;
}
