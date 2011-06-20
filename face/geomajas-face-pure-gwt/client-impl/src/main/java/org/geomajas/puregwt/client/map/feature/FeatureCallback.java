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

package org.geomajas.puregwt.client.map.feature;

import java.util.List;

/**
 * Call-back interface for implementing methods on lists of features.
 * 
 * @author Pieter De Graef
 */
public interface FeatureCallback {

	void execute(List<Feature> features);
}