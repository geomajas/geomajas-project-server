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

import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * Assure the lists of old and new features are the same size.
 *
 * @author Joachim Van der Auwera
 */
public class FeatureListEqualSizeStep implements PipelineStep {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		List<InternalFeature> oldFeatures = context.get(PipelineCode.OLD_FEATURES_KEY, List.class);
		List<InternalFeature> newFeatures = context.get(PipelineCode.NEW_FEATURES_KEY, List.class);
		int count = Math.max(oldFeatures.size(), newFeatures.size());
		while (oldFeatures.size() < count) {
			oldFeatures.add(null);
		}
		while (newFeatures.size() < count) {
			newFeatures.add(null);
		}
		context.put(PipelineCode.OLD_FEATURES_KEY, oldFeatures);
		context.put(PipelineCode.NEW_FEATURES_KEY, newFeatures);
	}
}
