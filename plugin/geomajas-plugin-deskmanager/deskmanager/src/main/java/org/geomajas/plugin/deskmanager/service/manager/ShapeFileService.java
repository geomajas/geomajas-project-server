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
package org.geomajas.plugin.deskmanager.service.manager;

import java.io.File;
import java.util.List;

import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;

/**
 * Service that handles import and export of shapefiles.
 * 
 * @author Oliver May
 *
 */
public interface ShapeFileService {

	/**
	 * Import a shape file to the deskmanager datastore.
	 * The shape file will always be converted to the default application CRS.
	 * 
	 * @param shpFileName
	 *            Fully qualified name of the shape file
	 * @param layerName the target name of the layer created from the shapefile in the target datastore
	 * @return true if import succeeded
	 */
	boolean importShapeFile(String shpFileName, String layerName);

	
	/**
	 * Write a collection of features to a shapefile.
	 * 
	 * @param shapeFile the target file
	 * @param layer the layer to write
	 * @param features the features to write
	 * @throws Exception
	 */
	void toShapeFile(File shapeFile, VectorLayer layer, List<InternalFeature> features) throws Exception;
}