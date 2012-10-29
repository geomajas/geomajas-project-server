/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.vendorspecificpipeline.step;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.opengis.filter.And;
import org.opengis.filter.Filter;

/**
 * Saves the combined layer filter in the context and replaces it by the non-security filter (tile bounds) before the
 * layer is queried. The security component (visible area) is also saved.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DelaySecurityFilterPreStep implements DelaySecurityFilterStep {

	public static final String SAVED_FILTER_KEY = "savedFilter";

	public static final String SECURITY_FILTER_KEY = "securityFilter";

	private String id;

	@Override
	public void execute(PipelineContext context, GetFeaturesContainer response) throws GeomajasException {
		Filter filter = context.getOptional(PipelineCode.FILTER_KEY, Filter.class);
		context.put(SAVED_FILTER_KEY, filter);
		if (filter instanceof And) {
			And and = (And) filter;
			if (and.getChildren().size() == 2) {
				context.put(PipelineCode.FILTER_KEY, and.getChildren().get(0));
				context.put(SECURITY_FILTER_KEY, and.getChildren().get(1));
			}
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