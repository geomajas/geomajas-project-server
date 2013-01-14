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
package org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.layer.feature.SearchCriterion;

/**
 * Export handler that uses the featureIds from the grid to
 * request the CSV. This means that if the resultset was cut (eg. there were
 * more features than the grid accepts (default 100) they will also not be in
 * the CSV.
 *
 * @author Kristof Heirwegh
 * @author Oliver May
 */
public class ExportFeatureListToCsvHandler extends ExportSearchToCsvHandler {

	private List<Feature> features;

	public ExportFeatureListToCsvHandler(MapModel model, VectorLayer layer) {
		super(model, layer);
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
		SearchFeatureRequest featReq = new SearchFeatureRequest();
		featReq.setCriteria(buildCriteria());
		featReq.setBooleanOperator("OR");
		featReq.setCrs(model.getCrs());
		featReq.setLayerId(layer.getServerLayerId());
		featReq.setFilter(layer.getFilter());
		featReq.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		setRequest(featReq);
	}

	private SearchCriterion[] buildCriteria() {
		List<SearchCriterion> critters = new ArrayList<SearchCriterion>();
		String idField = layer.getLayerInfo().getFeatureInfo().getIdentifier().getName();
		if (features != null) {
			for (Feature feat : features) {
				critters.add(new SearchCriterion(idField, "=", feat.getId()));
			}
		}
		return critters.toArray(new SearchCriterion[critters.size()]);
	}
}
