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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.geomajas.plugin.deskmanager.service.common.FileService;
import org.imgscalr.Scalr;
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
 * Spring MVC controller for uploading files.
 * 
 * @author Kristof Heirwegh
 * @author Jan De Moerloose
 */
@Controller("/fileUpload")
public class FileUploadController {

	private final Logger log = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	protected FileService service;

	/**
	 * Uploads a file and persists it with an id. If a targetWidth or targetHeight is passed, the file is assumed to be
	 * an image and the image will be rescaled to the target size before saving it as png.
	 * However, if the image is smaller than the target size, it will per saved in its original format.
	 * 
	 * @param binaryFile
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public ModelAndView handleFileUpload(@RequestParam("uploadFormElement") MultipartFile binaryFile,
			@RequestParam("targetWidth") Integer targetWidth, @RequestParam("targetHeight") Integer targetHeight)
			throws IOException {

		ModelAndView mav = new ModelAndView();
		mav.setViewName(FileUploadView.VIEW_NAME);
		String message;

		if (binaryFile.isEmpty()) {
			message = FileUploadView.RESPONSE_ERROR;
		} else {
			if (targetWidth != null || targetHeight != null) {
				message = handleImage(binaryFile, targetWidth, targetHeight);
			} else {
				message = handleFile(binaryFile);
			}
		}
		mav.addObject(FileUploadView.MESSAGE_KEY, message);
		return mav;
	}

	private String handleImage(MultipartFile binaryFile, Integer targetWidth, Integer targetHeight) {
		InputStream is = null;
		try {
			// rescale if necessary
			BufferedImage img = ImageIO.read(binaryFile.getInputStream());
			BufferedImage scaled = resize(img, targetWidth, targetHeight);
			if (scaled == img) {
				// no rescale, just use normal handling
				return handleFile(binaryFile);
			} else {
				// rescaled, save result as png
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(scaled, "png", baos);
				is = new ByteArrayInputStream(baos.toByteArray());
				String id = service.persist(is, "image/png");
				return FileUploadView.RESPONSE_OK + "[" + id + "]";
			}

		} catch (Exception e) {
			log.warn("Exception while processing image", e);
			return FileUploadView.RESPONSE_ERROR + " - " + e.getMessage();

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.warn("Fail: " + e.getMessage());
				}
			}
		}
	}

	private String handleFile(MultipartFile binaryFile) {
		InputStream is = null;
		try {
			// it may be more efficient to use .transferto(file) if the store is filebased
			is = binaryFile.getInputStream();
			String mimeType = binaryFile.getContentType();
			if (mimeType == null || "".equals(mimeType)) {
				mimeType = "application/octet-stream";
			}

			String id = service.persist(is, mimeType);
			return FileUploadView.RESPONSE_OK + "[" + id + "]";

		} catch (Exception e) {
			log.warn("Exception while processing binary file", e);
			return FileUploadView.RESPONSE_ERROR + " - " + e.getMessage();

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.warn("Fail: " + e.getMessage());
				}
			}
		}
	}

	protected BufferedImage resize(BufferedImage img, Integer targetWidth, Integer targetHeight) throws IOException {
		BufferedImage scaled = null;
		if (targetWidth != null) {
			if (targetHeight != null) {
				if (targetWidth < img.getWidth() || targetHeight < img.getHeight()) {
					scaled = Scalr.resize(img, targetWidth, targetHeight);
				} else {
					scaled = img;
				}
			} else {
				if (targetWidth < img.getWidth()) {
					scaled = Scalr.resize(img, targetWidth, img.getHeight());
				} else {
					scaled = img;
				}
			}
		} else {
			if (targetHeight < img.getHeight()) {
				scaled = Scalr.resize(img, img.getWidth(), targetHeight);
			} else {
				scaled = img;
			}
		}
		return scaled;
	}
}
