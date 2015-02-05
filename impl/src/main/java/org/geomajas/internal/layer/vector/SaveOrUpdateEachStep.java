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

package org.geomajas.internal.layer.vector;

import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineInfo;
import org.geomajas.service.pipeline.PipelineService;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Execute the vectorLayer.saveOrUpdateOne" pipeline for each of the features to saveOrUpdate.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateEachStep implements PipelineStep {

	private String id;
	private String pipelineName;

	@Autowired
	private PipelineService pipelineService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPipelineName(String pipelineName) {
		this.pipelineName = pipelineName;
	}

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
		PipelineInfo pipelineInfo = pipelineService.getPipeline(pipelineName, layerId);
		List<InternalFeature> oldFeatures = context.get(PipelineCode.OLD_FEATURES_KEY, List.class);
		List<InternalFeature> newFeatures = context.get(PipelineCode.NEW_FEATURES_KEY, List.class);

		int count = oldFeatures.size();
		for (int i = 0; i < count; i++) {
			context.put(PipelineCode.INDEX_KEY, i);
			context.put(PipelineCode.OLD_FEATURE_KEY, oldFeatures.get(i));
			InternalFeature newFeature = newFeatures.get(i);
			context.put(PipelineCode.FEATURE_KEY, newFeature);

			pipelineService.execute(pipelineInfo, context, newFeature);
		}
	}
	
}
