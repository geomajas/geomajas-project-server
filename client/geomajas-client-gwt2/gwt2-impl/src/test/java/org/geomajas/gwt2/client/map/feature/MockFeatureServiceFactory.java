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

package org.geomajas.gwt2.client.map.feature;

import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.FeatureFactory;
import org.geomajas.gwt2.client.map.feature.FeatureServiceFactory;
import org.geomajas.gwt2.client.map.feature.FeatureServiceImpl;

import com.google.inject.Inject;

/**
 * Test implementation of the {@link FeatureServiceFactory}.
 * 
 * @author Jan De Moerloose
 */
public class MockFeatureServiceFactory implements FeatureServiceFactory {

	@Inject
	FeatureFactory featureFactory;

	public FeatureService create(MapPresenter mapPresenter) {
		return new FeatureServiceImpl(mapPresenter, featureFactory);
	}
}