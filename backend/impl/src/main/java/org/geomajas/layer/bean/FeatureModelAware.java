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
package org.geomajas.layer.bean;

import org.geomajas.layer.feature.FeatureModel;

/**
 * Implemented by {@link BeanLayer} beans to retrieve the correct {@link FeatureModel} instance when defining multiple
 * {@link BeanLayer} layers with different {@link org.geomajas.configuration.FeatureInfo} configuration.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface FeatureModelAware {

	FeatureModel getFeatureModel();

	void setFeatureModel(FeatureModel featureModel);
}
