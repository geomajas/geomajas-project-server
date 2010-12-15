/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.layer.wms;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.GetMethod;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

	private static final int TIMEOUT = 5000;

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(value = "/wms/**", method = RequestMethod.GET)
	public void getWms(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Search for the WMS layer:
		String layerId = parseLayerId(request);
		WmsLayer layer = getLayer(layerId);
		if (layer == null) {
			throw new LayerException(ExceptionCode.LAYER_NOT_FOUND, layerId);
		}

		// Create a HTTP client object, which will initiate the connection:
		HttpClient client = new HttpClient();
		client.setConnectionTimeout(TIMEOUT);

		// Preemptive: In this mode HttpClient will send the basic authentication response even before the server gives
		// an unauthorized response in certain situations, thus reducing the overhead of making the connection.
		client.getState().setAuthenticationPreemptive(true);

		// Set up the WMS credentials:
		Credentials credentials = new UsernamePasswordCredentials(layer.getAuthentication().getUser(), layer
				.getAuthentication().getPassword());
		client.getState().setCredentials(layer.getAuthentication().getRealm(), parseDomain(layer.getBaseWmsUrl()),
				credentials);

		// Create the GET method with the correct URL:
		GetMethod get = new GetMethod(layer.getBaseWmsUrl() + "?" + request.getQueryString());
		get.setDoAuthentication(true);

		try {
			// Execute the GET:
			client.executeMethod(get);

			// Prepare the response:
			response.setContentType(layer.getFormat());
			response.getWriter().print(get.getResponseBodyAsString());
		} catch (Exception e) {
			// Create an error image to make the reason for the error visible:
			byte[] b = createErrorImage(layer.getLayerInfo().getTileWidth(), layer.getLayerInfo().getTileHeight(), e);
			response.setContentType("image/png");
			response.getOutputStream().write(b);
		} finally {
			// Release any connection resources used by the method:
			get.releaseConnection();
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
		if (null != layer && layer instanceof WmsLayer) {
			return (WmsLayer) layer;
		}
		return null;
	}

	/**
	 * Get the domain out of a full URL.
	 *
	 * @param url base url
	 * @return domain name
	 */
	private String parseDomain(String url) {
		int index = url.indexOf("://");
		String domain = url.substring(index + 3);
		return domain.substring(0, domain.indexOf('/'));
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