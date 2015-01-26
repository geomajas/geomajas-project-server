/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer;

import org.geomajas.geometry.Crs;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerService;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link LayerService}.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class LayerServiceImpl implements LayerService {

	@Autowired
	private GeoService geoService;

	@Override
	public Crs getCrs(Layer layer) throws LayerException {
		CoordinateReferenceSystem check = layer.getCrs();
		if (check instanceof Crs) {
			return (Crs) check;
		}
		return geoService.getCrs2(layer.getLayerInfo().getCrs());
	}

}
