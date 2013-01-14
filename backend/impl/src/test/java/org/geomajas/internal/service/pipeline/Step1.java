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

package org.geomajas.internal.service.pipeline;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * First step in pipeline for testing.
 *
 * @author Joachim Van der Auwera
 */
public class Step1 implements PipelineStep<StringAttribute> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, StringAttribute response) throws GeomajasException {
		context.put("Step1", id);
		context.put("test", "bla");
		Assert.assertNotNull(context.get("Step1"));

		String start = context.get("start", String.class);

		response.setValue(start + id);
	}
}
