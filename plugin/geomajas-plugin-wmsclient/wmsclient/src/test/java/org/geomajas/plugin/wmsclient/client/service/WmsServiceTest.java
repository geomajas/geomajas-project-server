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

package org.geomajas.plugin.wmsclient.client.service;

import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.wmsclient.client.WmsClientGinjector;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerConfiguration;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Testcase for the {@link WmsService} interface.
 * 
 * @author Pieter De Graef
 */
public class WmsServiceTest extends GWTTestCase {

	private WmsClientGinjector injector;

	private static final String VALUE_URL = "http://www.geomajas.org/";

	private static final String VALUE_LAYER = "someLayer";

	private static final String VALUE_STYLE = "someStyle";

	private static final String VALUE_CRS = "EPSG:4326";

	private static final int VALUE_SIZE = 342;

	private WmsService wmsService;

	private WmsLayerConfiguration wmsConfig;

	public String getModuleName() {
		return "org.geomajas.plugin.wmsclient.GeomajasWmsClientTest";
	}

	@Test
	public void testGetMapUrl() {
		initialize(); // No Spring in a GWT unit test.
		Bbox bounds = new Bbox(0, 1, 100, 100);
		String getMapUrl = wmsService.getMapUrl(wmsConfig, VALUE_CRS, bounds, VALUE_SIZE, VALUE_SIZE);

		assertEquals(VALUE_URL, getMapUrl.substring(0, getMapUrl.indexOf('?')));
		assertTrue(hasParameter(getMapUrl, "service", "WMS"));
		assertTrue(hasParameter(getMapUrl, "layers", wmsConfig.getLayers()));
		assertTrue(hasParameter(getMapUrl, "width", VALUE_SIZE + ""));
		assertTrue(hasParameter(getMapUrl, "height", VALUE_SIZE + ""));
		assertTrue(hasParameter(getMapUrl, "bbox", "0.0,1.0,100.0,101.0"));
		assertTrue(hasParameter(getMapUrl, "format", wmsConfig.getFormat()));
		assertTrue(hasParameter(getMapUrl, "version", wmsConfig.getVersion().toString()));
		assertTrue(hasParameter(getMapUrl, "crs", VALUE_CRS));
		assertTrue(hasParameter(getMapUrl, "styles", wmsConfig.getStyles()));
		assertTrue(hasParameter(getMapUrl, "transparent", wmsConfig.isTransparent() + ""));
		assertTrue(hasParameter(getMapUrl, "request", "GetMap"));
	}

	@Test
	public void testGetLegendUrl() {
		initialize(); // No Spring in a GWT unit test.
		String getLegendUrl = wmsService.getLegendGraphicUrl(wmsConfig);

		assertEquals(VALUE_URL, getLegendUrl.substring(0, getLegendUrl.indexOf('?')));
		assertTrue(hasParameter(getLegendUrl, "service", "WMS"));
		assertTrue(hasParameter(getLegendUrl, "layer", wmsConfig.getLayers()));
		assertTrue(hasParameter(getLegendUrl, "request", "GetLegendGraphic"));
		assertTrue(hasParameter(getLegendUrl, "format", wmsConfig.getFormat()));
		assertTrue(hasParameter(getLegendUrl, "width", wmsConfig.getLegendWidth() + ""));
		assertTrue(hasParameter(getLegendUrl, "height", wmsConfig.getLegendHeight() + ""));
	}

	private void initialize() {
		injector = GWT.create(WmsClientGinjector.class);
		wmsService = injector.getWmsService();
		wmsConfig = new WmsLayerConfiguration();
		wmsConfig.setBaseUrl(VALUE_URL);
		wmsConfig.setLayers(VALUE_LAYER);
		wmsConfig.setStyles(VALUE_STYLE);
	}

	private boolean hasParameter(String url, String parameter, String value) {
		String paramString = url.substring(url.indexOf('?') + 1);
		String[] parameters = paramString.split("&");

		for (String param : parameters) {
			String paramName = param.substring(0, param.indexOf('='));
			if (paramName.equalsIgnoreCase(parameter)) {
				String paramValue = param.substring(param.indexOf('=') + 1);
				return paramValue.equals(value);
			}
		}
		return false;
	}
}