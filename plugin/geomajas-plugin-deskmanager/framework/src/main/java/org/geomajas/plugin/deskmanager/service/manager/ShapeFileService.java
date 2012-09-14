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
package org.geomajas.plugin.deskmanager.service.manager;

import java.io.File;
import java.util.List;

import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;

/**
 * 
 * @author Oliver May
 *
 */
public interface ShapeFileService {

	boolean doGeoToolsImport(String shpFileName, String layerName);

	File toShapeFile(File shapeFile, VectorLayer layer, List<InternalFeature> features) throws Exception;
}