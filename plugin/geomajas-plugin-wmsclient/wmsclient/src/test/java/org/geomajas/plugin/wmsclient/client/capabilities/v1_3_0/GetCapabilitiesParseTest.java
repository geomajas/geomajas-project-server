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

package org.geomajas.plugin.wmsclient.client.capabilities.v1_3_0;

import java.io.IOException;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.wmsclient.client.WmsClientGinjector;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerLegendUrlInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerMetadataUrlInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerStyleInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsOnlineResourceInfo;
import org.geomajas.plugin.wmsclient.client.service.WmsService;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsVersion;
import org.junit.Test;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT test that fetches capabilities documents from a servlet and tries to parse them. We test WMS versions 1.1.1 and
 * 1.3.0.
 * 
 * @author Pieter De Graef
 */
public class GetCapabilitiesParseTest extends GWTTestCase {

	private static final String CAPA_FILE = "/GetCapabilities";

	private static WmsClientGinjector INJECTOR;

	private WmsService wmsService;

	public String getModuleName() {
		return "org.geomajas.plugin.wmsclient.GeomajasWmsClientTest";
	}

	@Test
	public void testCapabilities111() throws IOException {
		INJECTOR = GWT.create(WmsClientGinjector.class);
		wmsService = INJECTOR.getWmsService();
		wmsService.getCapabilities(GWT.getModuleBaseURL() + CAPA_FILE, WmsVersion.v1_1_1,
				new Callback<WmsGetCapabilitiesInfo, String>() {

					public void onSuccess(WmsGetCapabilitiesInfo result) {
						assertNotNull(result);

						List<WmsLayerInfo> layers = result.getLayers();
						assertNotNull(layers);
						assertEquals(2, layers.size());

						checkLayer(1, layers.get(0), WmsVersion.v1_1_1);
						checkLayer(2, layers.get(1), WmsVersion.v1_1_1);

						finishTest();
					}

					public void onFailure(String reason) {
						fail(reason);
						finishTest();
					}
				});
		delayTestFinish(10000);
	}

	@Test
	public void testCapabilities130() throws IOException {
		INJECTOR = GWT.create(WmsClientGinjector.class);
		wmsService = INJECTOR.getWmsService();
		wmsService.getCapabilities(GWT.getModuleBaseURL() + CAPA_FILE, WmsVersion.v1_3_0,
				new Callback<WmsGetCapabilitiesInfo, String>() {

					public void onSuccess(WmsGetCapabilitiesInfo result) {
						assertNotNull(result);

						List<WmsLayerInfo> layers = result.getLayers();
						assertNotNull(layers);
						assertEquals(2, layers.size());

						checkLayer(1, layers.get(0), WmsVersion.v1_3_0);
						checkLayer(2, layers.get(1), WmsVersion.v1_3_0);

						finishTest();
					}

					public void onFailure(String reason) {
						fail(reason);
						finishTest();
					}
				});
		delayTestFinish(10000);
	}

	// ------------------------------------------------------------------------
	// Private methods for testing the parsed capabilities documents:
	// ------------------------------------------------------------------------

	private void checkLayer(int index, WmsLayerInfo layer, WmsVersion version) {
		if (index == 1) {
			assertFalse(layer.isQueryable());
		} else {
			assertTrue(layer.isQueryable());
		}
		assertEquals("layer" + index + "Name", layer.getName());
		assertEquals("layer" + index + "Title", layer.getTitle());
		assertEquals("layer" + index + "Abstract", layer.getAbstract());

		List<String> keywords = layer.getKeywords();
		assertNotNull(keywords);
		assertEquals(2, keywords.size());
		assertEquals("keyword1", keywords.get(0));
		assertEquals("keyword2", keywords.get(1));

		List<String> crs = layer.getCrs();
		assertNotNull(crs);
		if (version.equals(WmsVersion.v1_1_1)) {
			assertEquals(1, crs.size());
			assertEquals("EPSG:31370", crs.get(0));
		} else {
			assertEquals(2, crs.size());
			assertEquals("EPSG:31370", crs.get(0));
			assertEquals("EPSG:4326", crs.get(1));
		}

		Bbox latlonBox = layer.getLatlonBoundingBox();
		assertEquals(0.0, latlonBox.getX());
		assertEquals(1.0, latlonBox.getMaxX());
		assertEquals(2.0, latlonBox.getY());
		assertEquals(3.0, latlonBox.getMaxY());

		assertEquals("EPSG:31370", layer.getBoundingBoxCrs());
		Bbox bounds = layer.getBoundingBox();
		assertEquals(0.0, bounds.getX());
		assertEquals(200000.0, bounds.getMaxX());
		assertEquals(0.0, bounds.getY());
		assertEquals(200000.0, bounds.getMaxY());

		checkMetadataUrl(layer.getMetadataUrl());
		List<WmsLayerStyleInfo> styles = layer.getStyleInfo();
		assertNotNull(styles);
		assertEquals(1, styles.size());
		checkLayerStyle(index, styles.get(0));
	}

	private void checkMetadataUrl(WmsLayerMetadataUrlInfo info) {
		assertEquals("ISO19115:2003", info.getType());
		assertEquals("application/xml", info.getFormat());
		checkOnlineResource(info.getOnlineResource());
	}

	private void checkOnlineResource(WmsOnlineResourceInfo info) {
		assertEquals("http://www.geomajas.org/", info.getHref());
		assertEquals("simple", info.getType());
		assertEquals("xlink", info.getXLink());
	}

	private void checkLayerStyle(int index, WmsLayerStyleInfo info) {
		assertEquals("layer" + index + "StyleName", info.getName());
		assertEquals("layer" + index + "StyleTitle", info.getTitle());
		assertEquals("layer" + index + "StyleAbstract", info.getAbstract());
		checkLayerLegendUrl(info.getLegendUrl());
	}

	private void checkLayerLegendUrl(WmsLayerLegendUrlInfo info) {
		assertEquals("image/png", info.getFormat());
		assertEquals(20, info.getWidth());
		assertEquals(20, info.getHeight());
		checkOnlineResource(info.getOnlineResource());
	}
}