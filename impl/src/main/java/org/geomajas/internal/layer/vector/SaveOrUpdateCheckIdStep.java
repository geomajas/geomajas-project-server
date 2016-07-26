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

package org.geomajas.internal.layer.vector;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * Sanity check to assure that the id of old and new feature value are th same before we start to update.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateCheckIdStep implements PipelineStep {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		InternalFeature oldFeature = context.getOptional(PipelineCode.OLD_FEATURE_KEY, InternalFeature.class);
		if (null != oldFeature) {
			InternalFeature newFeature = context.get(PipelineCode.FEATURE_KEY, InternalFeature.class);
			if (null == oldFeature.getId() || !oldFeature.getId().equals(newFeature.getId())) {
				int index = context.get(PipelineCode.INDEX_KEY, Integer.class);
				throw new GeomajasException(ExceptionCode.FEATURE_ID_MISMATCH, index);
			}
		}
	}
}
