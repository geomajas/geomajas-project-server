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
package org.geomajas.plugin.deskmanager.servlet.mvc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.geomajas.plugin.deskmanager.service.manager.ShapeFileService;
import org.geomajas.plugin.deskmanager.utility.FileUtils;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC controller for uploading shapefiles.
 * 
 * @author Frank Wynants
 * @author Kristof Heirwegh
 */
@Controller("/shapefileUpload")
public class ShapeFileUploadController {

	private final Logger log = LoggerFactory.getLogger(ShapeFileUploadController.class);

	private static final String DB_LAYERNAME_PREFIX = "layer_";

	private static final String PARAM_LAYERNAME = "layername";

	@Autowired
	protected ShapeFileService service;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private LayerModelService layerModelService;

	@RequestMapping(value = "/shapefileUpload", method = RequestMethod.POST)
	public ModelAndView handleUpload(@RequestParam(PARAM_LAYERNAME) String layerName,
			@RequestParam("uploadFormElement") MultipartFile shapeFile) throws IOException {

		ModelAndView mav = new ModelAndView();
		mav.setViewName(ShapeFileUploadView.VIEW_NAME);
		String message = null;
		String dataSourceName = null;

		if (shapeFile.isEmpty()) {
			message = ShapeFileUploadView.RESPONSE_INVALID_FILE;
		}

		if (((DeskmanagerSecurityContext) securityContext).isShapeFileUploadAllowed(layerName)) {
			if (layerName == null || "".equals(layerName)) { // new
				dataSourceName = DB_LAYERNAME_PREFIX + UUID.randomUUID().toString().replaceAll("-", "");
			} else { // existing
				LayerModel lm = null;
				try {
					lm = layerModelService.getLayerModelByClientLayerId(layerName);
				} catch (Exception e) {
				}
				if (lm != null) {
					try {
						dataSourceName = ((VectorLayerInfo) lm.getDynamicLayerConfiguration().getServerLayerInfo())
								.getFeatureInfo().getDataSourceName();
					} catch (Exception e) {
					}
				} else {
					message = ShapeFileUploadView.RESPONSE_INVALID_LAYER;
				}
			}

			if (dataSourceName != null) {
				message = handleFile(shapeFile, dataSourceName);
			} else {
				message = ShapeFileUploadView.RESPONSE_INVALID_LAYER;
			}
		} else {
			message = ShapeFileUploadView.RESPONSE_NO_RIGHTS;
		}

		if (ShapeFileUploadView.RESPONSE_OK.equals(message)) {
			message += "_" + dataSourceName;
		}

		mav.addObject(ShapeFileUploadView.MESSAGE_KEY, message);
		return mav;
	}

	private String handleFile(MultipartFile shapeFile, String layerName) {
		String tmpDir = "/tmp/" + new Date().getTime();
		try {
			new File(tmpDir).mkdir();
			String tmpName = tmpDir + "/input.zip";
			File f = new File(tmpName);
			shapeFile.transferTo(f);

			// unzip the temporary file
			if (unzip(tmpName, tmpDir)) {
				// check if shp is available
				String shpFileName = getShpFileName(tmpDir);
				if (shpFileName != null) {
					boolean ok = service.importShapeFile(shpFileName, layerName);
					if (ok) {
						// upload succeeded; now fill the mapping table
						return ShapeFileUploadView.RESPONSE_OK;
					}
				}
			}
		} catch (Exception e) {
			log.warn("Exception while processing shapefile", e);
		} finally {
			FileUtils.deleteFolder(new File(tmpDir)); // always clean up temporary files
		}
		return ShapeFileUploadView.RESPONSE_INVALID_FILE;
	}

	/**
	 * Unzips a zipfile
	 * 
	 * @param zipFileName The fully qualified name of the zipfile to unzip
	 * @param tmpDir The dir to which write the unzipped files
	 * @return true if all succeeded
	 */
	private boolean unzip(String zipFileName, String tmpDir) {
		Enumeration<?> entries;
		ZipFile zipFile = null;

		try {
			zipFile = new ZipFile(zipFileName);
			entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (!entry.getName().contains("__MACOSX")) {
					copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(tmpDir
							+ "/" + entry.getName())));
				}
			}

			zipFile.close();
		} catch (IOException e) {
			log.warn("Failed unzipping shapefile archive.", e);
			return false;
		} finally {
			try {
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (Exception e) { // ignore
			}
		}
		return true;
	}

	/**
	 * Copys an inputstream to an outputstream
	 * 
	 * @param in The inputstream to read from
	 * @param out The outputstream to write to
	 * @throws IOException
	 */
	private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		try {
			byte[] buffer = new byte[1024];
			int len;

			while ((len = in.read(buffer)) >= 0) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			// caveat: the method that opens the stream should also close it
			in.close();
			out.close();
		}
	}

	/**
	 * Get fully qualified .shp filename
	 * 
	 * @param tmpDir
	 * @return The fully qualified name of the .shp file in the given dir, or null if no .shp file is found
	 */
	private String getShpFileName(String dir) {
		File fDir = new File(dir);
		File[] filesInDir = fDir.listFiles();
		for (File file : filesInDir) {
			if (file.getName().toLowerCase().endsWith(".shp")) {
				return file.getAbsolutePath();
			}
		}
		return null;
	}
}
