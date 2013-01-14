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
package org.geomajas.widget.featureinfo.client.widget;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
/**
 * Definition of an interface to specify a handler that has to be called when the user clicks on a 
 * org.geomajas.gwt.client.map.feature.Feature feature.
 *
 * @author An Buyle
 */
public interface FeatureClickHandler {

	void onClick(Feature feat, Layer layer);
}
