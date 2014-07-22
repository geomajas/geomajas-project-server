/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.manager;

import org.geomajas.layer.LayerException;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;

/**
 * Service for creating Geomajas info objects from other sources.
 *
 * @author Jan Venstermans
 */
public interface DtoFactoryService {

	RasterCapabilitiesInfo buildRasterCapabilitesInfoFromWms(WebMapServer wmsMapServer,
															 Layer owsLayer, String toCrs)
			throws LayerException;

}