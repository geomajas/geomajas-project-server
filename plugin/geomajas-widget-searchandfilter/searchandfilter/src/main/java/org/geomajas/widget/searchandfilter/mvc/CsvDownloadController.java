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
package org.geomajas.widget.searchandfilter.mvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.http.HttpServletResponse;

import org.geomajas.widget.searchandfilter.service.FileDownloadService;
import org.geomajas.widget.searchandfilter.service.FileDownloadService.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller part of Spring WebMCV.
 * 
 * @author Kristof Heirwegh
 */
@Controller("/csvDownload")
public class CsvDownloadController {

	private final Logger log = LoggerFactory.getLogger(CsvDownloadController.class);

	@Autowired
	private FileDownloadService service;

	@RequestMapping(value = "/csvDownload", method = RequestMethod.GET)
	public void handleCsvDownload(@RequestParam("id") String id, HttpServletResponse response) throws IOException {
		try {
			Item item = service.getFile(id);
			if (item == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			File f = item.getFile();
			if (f == null || !f.isFile() || !f.canRead()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			// Set response headers
			response.setContentType("text/csv");
			response.setContentLength((int) f.length());
			String filename = (item.getDescription());
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);

			FileInputStream fis = new FileInputStream(f);
			FileChannel in = fis.getChannel();
			WritableByteChannel out = Channels.newChannel(response.getOutputStream());
			try {
				in.transferTo(0, in.size(), out);
				service.removeFile(id);
			} catch (Exception e) {
				throw e;
			} finally {
				in.close();
				// make sure servlet doesn't append anything unnecessary:
				out.close();
			}
		} catch (FileNotFoundException e) {
			log.warn("Error retrieving file: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);

		} catch (Exception e) {
			log.warn("Error retrieving file: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
