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

import java.util.ListIterator;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Applies the security filter and removes features that are not allowed.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DelaySecurityFilterPostStep implements DelaySecurityFilterStep {

	private String id;
	
	@Autowired
	private TestRecorder recorder;

	@Override
	public void execute(PipelineContext context, GetFeaturesContainer response) throws GeomajasException {
		Filter filter = context.getOptional(SAVED_FILTER_KEY, Filter.class);
		context.put(PipelineCode.FILTER_KEY, filter);
		Filter securityFilter = context.getOptional(SECURITY_FILTER_KEY, Filter.class);
		if (securityFilter != null) {
			for (ListIterator<InternalFeature> it = response.getFeatures().listIterator(); it.hasNext();) {
				if (!securityFilter.evaluate(it.next())) {
					it.remove();
				}
			}
			recorder.record("layer", "applied security filter after layer");
		}
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
