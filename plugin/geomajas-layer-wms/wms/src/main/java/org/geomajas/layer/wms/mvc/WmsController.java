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
package org.geomajas.layer.wms.mvc;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.wms.WmsHttpService;
import org.geomajas.layer.wms.WmsLayer;
import org.geomajas.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.StringTokenizer;

/**
 * Spring MVC controller that maps a WMS request so it can be proxied to the real URL with authentication parameters.
 * 
 * @author Pieter De Graef
 */
@Controller("/wms/**")
public class WmsController {

	private final Logger log = LoggerFactory.getLogger(WmsController.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private WmsHttpService httpService;

	// method provided for testing
	protected void setHttpService(WmsHttpService httpService) {
		this.httpService = httpService;
	}

	@RequestMapping(value = "/wms/**", method = RequestMethod.GET)
	public void getWms(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Search for the WMS layer:
		String layerId = parseLayerId(request);
		WmsLayer layer = getLayer(layerId);
		if (layer == null) {
			throw new LayerException(ExceptionCode.LAYER_NOT_FOUND, layerId);
		}
		RasterLayerInfo layerInfo = layer.getLayerInfo();

		String url = layer.getBaseWmsUrl() + "?" + request.getQueryString();
		InputStream stream = null;

		try {
			response.setContentType(layer.getFormat());
			stream = httpService.getStream(url, layer.getAuthentication());
			ServletOutputStream out = response.getOutputStream();
			int b;
			while ((b = stream.read()) >= 0 ) {
				out.write(b);
			}
		} catch (Exception e) {
			log.error("Cannot get original WMS image", e);
			// Create an error image to make the reason for the error visible:
			byte[] b = createErrorImage(layerInfo.getTileWidth(), layerInfo.getTileHeight(), e);
			response.setContentType("image/png");
			response.getOutputStream().write(b);
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (IOException ioe) {
					// ignore, closing anyway
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/** Get the layer ID out of the request URL.
	 *
	 * @param request servlet request
	 * @return layer id
	 */
	private String parseLayerId(HttpServletRequest request) {
		StringTokenizer tokenizer = new StringTokenizer(request.getRequestURI(), "/");
		String token = "";
		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
		}
		return token;
	}

	/**
	 * Given a layer ID, search for the WMS layer.
	 *
	 * @param layerId layer id
	 * @return WMS layer or null if layer is not a WMS layer
	 */
	private WmsLayer getLayer(String layerId) {
		RasterLayer layer = configurationService.getRasterLayer(layerId);
		if (layer instanceof WmsLayer) {
			return (WmsLayer) layer;
		}
		return null;
	}

	/**
	 * Create an error image should an error occur while fetching a WMS map.
	 *
	 * @param width image width
	 * @param height image height
	 * @param e exception
	 * @return error image
	 * @throws java.io.IOException oops
	 */
	private byte[] createErrorImage(int width, int height, Exception e) throws IOException {
		String error = e.getMessage();
		if (null == error) {
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			error = result.toString();
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.setColor(Color.RED);
		g.drawString(error, 10, height / 2);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "PNG", out);
		out.flush();
		byte[] result = out.toByteArray();
		out.close();

		return result;
	}
}