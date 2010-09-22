/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.map.feature;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.CommandCallback;
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

			GwtCommand command = new GwtCommand("command.feature.Search");
			command.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

				public void execute(CommandResponse response) {
					if (response instanceof SearchFeatureResponse) {
						SearchFeatureResponse resp = (SearchFeatureResponse) response;
						if (null != resp.getFeatures() && resp.getFeatures().length > 0) {
							// build map of features to allow fast update
							Map<String, Integer> map = new HashMap<String, Integer>();
							for (int i = 0 ; i < features.size() ; i++) {
								Feature feature = features.get(i);
								map.put(feature.getId(), i);
							}

							for (org.geomajas.layer.feature.Feature dto : resp.getFeatures()) {
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
				}
			});
		} else {
			callback.execute(features);
		}
	}
}
