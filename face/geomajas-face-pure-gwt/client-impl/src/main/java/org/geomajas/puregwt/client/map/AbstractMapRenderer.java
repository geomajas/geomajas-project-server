/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.spatial.Matrix;

/**
 * Abstract {@link MapRenderer} implementation that provides protected methods for transformation handling. These
 * transformations will come in handy when actually rendering objects on the map.
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractMapRenderer implements MapRenderer {

	protected MapModel mapModel;

	protected AbstractMapRenderer(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	protected Coordinate worldToPan(Coordinate coordinate) {
		return ((TransformationServiceImpl) mapModel.getViewPort().getTransformationService()).worldToPan(coordinate);
	}

	protected Matrix getWorldToPanTransformation() {
		return ((TransformationServiceImpl) mapModel.getViewPort().getTransformationService())
				.getWorldToPanTransformation();
	}

	protected Matrix getWorldToPanTranslation() {
		return ((TransformationServiceImpl) mapModel.getViewPort().getTransformationService())
				.getWorldToPanTranslation();
	}

	protected Matrix getPanToViewTranslation() {
		return ((TransformationServiceImpl) mapModel.getViewPort().getTransformationService())
				.getPanToViewTranslation();
	}
}