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
package org.geomajas.plugin.deskmanager.command.manager;

import com.google.common.io.Files;
import org.geomajas.command.Command;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.ProcessShapeFileRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.ProcessShapeFileResponse;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.geomajas.plugin.deskmanager.service.manager.GenericFileService;
import org.geomajas.plugin.deskmanager.service.manager.ShapeFileService;
import org.geomajas.plugin.deskmanager.servlet.mvc.GenericFileUploadView;
import org.geomajas.plugin.deskmanager.utility.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.UUID;

/**
 * Command that imports a shapefile (zipped) into the deskmanager database.
 *
 * @author Oliver May
 */
@Component(ProcessShapeFileRequest.COMMAND)
@Transactional
public class ProcessShapeFileCommand implements Command<ProcessShapeFileRequest, ProcessShapeFileResponse> {

	@Autowired
	private GenericFileService fileService;

	@Autowired
	private ShapeFileService shapeFileService;

	@Autowired
	private LayerModelService layerModelService;

	private final Logger log = LoggerFactory.getLogger(ProcessShapeFileCommand.class);

	private static final String DB_LAYERNAME_PREFIX = "layer_";

	@Override
	public ProcessShapeFileResponse getEmptyCommandResponse() {
		return new ProcessShapeFileResponse();
	}

	@Override
	public void execute(ProcessShapeFileRequest request, ProcessShapeFileResponse response) throws Exception {
		File shapeZipFile = fileService.getFile(request.getFileId());
		String layerName = request.getLayerName();

		if (shapeZipFile == null) {
			Exception e = new IllegalArgumentException(GenericFileUploadView.RESPONSE_INVALID_FILE);
			log.warn(e.getLocalizedMessage());
			throw e;
		}

		String dataSourceName = null;
		if (layerName == null || "".equals(layerName)) { // new
			//Generate a new layerName
			dataSourceName = DB_LAYERNAME_PREFIX + UUID.randomUUID().toString().replaceAll("-", "");
		} else {
			// Use existing
			LayerModel lm = null;
			try {
				lm = layerModelService.getLayerModelByClientLayerId(layerName);
			} catch (Exception e) {
				//FIXME
			}
			if (lm != null) {
				try {
					dataSourceName = ((VectorLayerInfo) lm.getDynamicLayerConfiguration().getServerLayerInfo())
							.getFeatureInfo().getDataSourceName();
				} catch (Exception e) {
					//FIXME
				}
			} else {
				throw new Exception(GenericFileUploadView.RESPONSE_INVALID_LAYER);
			}
		}

		if (dataSourceName != null) {
			try {
				File shapeFile = unzipFile(shapeZipFile);
				//this will add the data of the shapefile to the database
				shapeFileService.importShapeFile(shapeFile, dataSourceName);
				response.setLayerName(dataSourceName);
			} finally {
				FileUtils.deleteFolder(new File(shapeZipFile.getParent())); // always clean up temporary files
			}

		} else {
			throw new Exception(GenericFileUploadView.RESPONSE_INVALID_LAYER);
		}
	}

	/**
	 * Unzip the shapefile and return the .shp file.
	 *
	 * @param shapeFile the zipped shapefile
	 * @return the .shp file
	 */
	private File unzipFile(File shapeFile) {
		File tmpDirFile = Files.createTempDir();
		try {
			File f = new File(tmpDirFile, "input.zip");
			org.apache.commons.io.FileUtils.copyFile(shapeFile, f);

			// unzip the temporary file
			if (shapeFileService.unzip(f.getPath(), tmpDirFile.getPath())) {
				// check if shp is available
				String shpFileName = shapeFileService.getShpFileName(tmpDirFile.getPath());
				if (shpFileName != null) {
					return new File(shpFileName);
				}
			}
		} catch (Exception e) {
			log.warn("Exception while processing shapefile", e);
		} finally {
			tmpDirFile.delete();
		}
		return null;
	}
}
