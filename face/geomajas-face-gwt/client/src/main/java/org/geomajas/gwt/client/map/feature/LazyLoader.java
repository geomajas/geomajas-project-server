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

package org.geomajas.gwt.client.map.feature;

import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.layer.feature.SearchCriterion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class which handles the lazy loading of features.
 *
 * @author Joachim Van der Auwera
 */
public final class LazyLoader {

	private LazyLoader() {
		// don't allow construction
	}

	/**
	 * Assure the specified aspects are set in the list of features.
	 * <p/>
	 * This will update attributes and/or geometry.
	 * <p/>
	 * All features which are passed have to be from the same layer.
	 *
	 * @param features list of features
	 * @param featureIncludes data to include
	 * @param callback callback to call when lazy loading is complete.
	 */
	public static void lazyLoad(final List<Feature> features, final int featureIncludes,
			final LazyLoadCallback callback) {
		List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();
		final boolean incAttr = (featureIncludes & GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES) != 0;
		final boolean incGeom = (featureIncludes & GeomajasConstant.FEATURE_INCLUDE_GEOMETRY) != 0;
		if (null != features) {
			for (Feature feature : features) {
				if ((incAttr && !feature.isAttributesLoaded()) || (incGeom && !feature.isGeometryLoaded())) {
					criteria.add(new SearchCriterion(SearchFeatureRequest.ID_ATTRIBUTE, "=", feature.getId()));
				}
			}
		}
		if (criteria.size() > 0) {
			Feature first = features.get(0);
			SearchFeatureRequest request = new SearchFeatureRequest();
			request.setBooleanOperator("OR");
			request.setCriteria(criteria.toArray(new SearchCriterion[criteria.size()]));
			request.setCrs(first.getLayer().getMapModel().getCrs());
			request.setLayerId(first.getLayer().getServerLayerId());
			request.setMax(0);
			request.setFilter(first.getLayer().getFilter());
			request.setFeatureIncludes(featureIncludes);

			GwtCommand command = new GwtCommand(SearchFeatureRequest.COMMAND);
			command.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<SearchFeatureResponse>() {

				public void execute(SearchFeatureResponse response) {
					if (null != response.getFeatures() && response.getFeatures().length > 0) {
						// build map of features to allow fast update
						Map<String, Integer> map = new HashMap<String, Integer>();
						for (int i = 0 ; i < features.size() ; i++) {
							Feature feature = features.get(i);
							map.put(feature.getId(), i);
						}

						for (org.geomajas.layer.feature.Feature dto : response.getFeatures()) {
							Feature feature = features.get(map.get(dto.getId()));
							if (incAttr) {
								feature.setAttributes(dto.getAttributes());
							}
							if (incGeom) {
								Geometry geometry = GeometryConverter.toGwt(dto.getGeometry());
								feature.setGeometry(geometry);
							}
						}
					}
					callback.execute(features);
				}
			});
		} else {
			callback.execute(features);
		}
	}
}
