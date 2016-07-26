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

package org.geomajas.internal.service.pipeline;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * Second step in pipeline for testing.
 *
 * @author Joachim Van der Auwera
 */
public class Step2 implements PipelineStep<StringAttribute> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, StringAttribute response) throws GeomajasException {
		context.put("Step2", id);
		Assert.assertEquals("bla", context.get("test", String.class));
		Assert.assertNotNull(context.get("Step1"));
		Assert.assertNotNull(context.get("Step2"));
		
		response.setValue(response.getValue() + id);
	}
}
