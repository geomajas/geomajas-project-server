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
package org.geomajas.puregwt.client.map.feature;

import org.geomajas.puregwt.client.map.MapPresenter;

/**
 * GIN factory for {@link Feature} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface FeatureServiceFactory {

	/**
	 * creates a {@link FeatureService} using the given {@link MapPresenter}.
	 * 
	 * @param mapPresenter the map presenter
	 * @return a new {@link FeatureService}
	 */
	FeatureService create(MapPresenter mapPresenter);
}
