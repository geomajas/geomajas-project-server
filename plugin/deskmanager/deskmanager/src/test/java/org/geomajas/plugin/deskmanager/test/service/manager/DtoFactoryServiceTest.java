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
package org.geomajas.plugin.deskmanager.test.service.manager;

import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.service.manager.DtoFactoryService;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.OperationType;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.ows.WMSRequest;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for {@link DtoFactoryService}.
 *
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
public class DtoFactoryServiceTest {

	@Autowired
	private DtoFactoryService dtoFactoryService;

	private Bbox maxExtend3857 = new Bbox();

	public DtoFactoryServiceTest() {
		maxExtend3857.setX(-20037508.34);
		maxExtend3857.setY(-20037508.34);
		maxExtend3857.setMaxX(20037508.34);
		maxExtend3857.setMaxY(20037508.34);
	}

	@Test
	public void testBuildRasterInfo4327MaxExtend() throws Exception {
		// mock WebMapServer
		WebMapServer webMapServerMock = Mockito.mock(WebMapServer.class);
		GetMapRequest getMapRequestMock = Mockito.mock(GetMapRequest.class);
		WMSCapabilities wmsCapabilitiesMock = Mockito.mock(WMSCapabilities.class);
		WMSRequest wmsRequestMock = Mockito.mock(WMSRequest.class);
		OperationType operationTypeMock = Mockito.mock(OperationType.class);
		List<String> featureInfoFormats = Arrays.asList(new String[]{"format1", "format2"});
		String url = "http://apps.geomajas.org/geoserver/ows?service=WMS&request=GetCapabilities&version=1.3.0";
		Mockito.stub(webMapServerMock.getCapabilities()).toReturn(wmsCapabilitiesMock);
		Mockito.stub(wmsCapabilitiesMock.getRequest()).toReturn(wmsRequestMock);
		Mockito.stub(wmsRequestMock.getGetFeatureInfo()).toReturn(operationTypeMock);
		Mockito.stub(operationTypeMock.getFormats()).toReturn(featureInfoFormats);
		Mockito.stub(webMapServerMock.createGetMapRequest()).toReturn(getMapRequestMock);
		Mockito.stub(getMapRequestMock.getFinalURL()).toReturn(new URL(url));

		// create and fill Layer
		Layer owsLayer = new Layer();
		owsLayer.setName("owsLayerName");
		CRSEnvelope latLonBoundingBox = new CRSEnvelope("CRS:84", -180, -90, 180, 90);
		owsLayer.setLatLonBoundingBox(latLonBoundingBox);

		String toCrs = "EPSG:3857";

		RasterCapabilitiesInfo rasterCapabilitiesInfo = dtoFactoryService.buildRasterCapabilitesInfoFromWms(
				webMapServerMock, owsLayer, toCrs);

		Assert.assertEquals(owsLayer.getName(), rasterCapabilitiesInfo.getName());
		Assert.assertEquals(toCrs, rasterCapabilitiesInfo.getCrs());
		Assert.assertEquals(featureInfoFormats, rasterCapabilitiesInfo.getGetFeatureInfoFormats());

		// bounds are EPSG:3875 extend max bounds
		Bbox extend = rasterCapabilitiesInfo.getExtent();
		Assert.assertEquals(maxExtend3857.getX(), extend.getX(), 0.1);
		Assert.assertEquals(-21396567.25, extend.getY(), 0.1);
		Assert.assertEquals(maxExtend3857.getMaxX(), extend.getMaxX(), 0.1);
		Assert.assertEquals(21396567.25, extend.getMaxY(), 0.1);
	}

	@Test
	public void testBuildRasterInfoUrlFormation() throws Exception {
		String baseUrl = "http://apps.geomajas.org/geoserver/ows";
		String baseUrlWithPort = "http://apps.geomajas.org:80/geoserver/ows";
		URL url = new URL(baseUrl + "?service=WMS&request=GetCapabilities&version=1.3.0");
		SimpleHttpClient httpClient = new SimpleHttpClient();
		WebMapServer wms = new WebMapServer(url, httpClient);

		// create and fill Layer
		Layer owsLayer = new Layer();
		owsLayer.setName("owsLayerName");
		CRSEnvelope latLonBoundingBox = new CRSEnvelope("CRS:84", 0, 0, 50, 50);
		owsLayer.setLatLonBoundingBox(latLonBoundingBox);

		String toCrs = "EPSG:3857";

		RasterCapabilitiesInfo rasterCapabilitiesInfo = dtoFactoryService.buildRasterCapabilitesInfoFromWms(wms, owsLayer, toCrs);

		Assert.assertEquals(owsLayer.getName(), rasterCapabilitiesInfo.getName());
		Assert.assertEquals(toCrs, rasterCapabilitiesInfo.getCrs());

		// urls
		Assert.assertEquals(baseUrlWithPort, rasterCapabilitiesInfo.getBaseUrl());
		String baseUrlResult = rasterCapabilitiesInfo.getBaseUrl();
		String previewUrl = rasterCapabilitiesInfo.getPreviewUrl();
		Assert.assertTrue(previewUrl.startsWith(baseUrlResult + "?"));
		previewUrl = previewUrl.substring(baseUrlResult.length() + 1);
		String[] urlElements = previewUrl.split("&");
		Map<String, String> urlElementMap = new HashMap<String, String>();
		for (String element : urlElements) {
			String[] elementKeyValue = element.split("=");
			urlElementMap.put(elementKeyValue[0], elementKeyValue.length > 1 ? elementKeyValue[1] : "");
		}
		Assert.assertTrue(urlElementMap.containsKey("SERVICE"));
		Assert.assertTrue("wms".equalsIgnoreCase(urlElementMap.get("SERVICE")));
		Assert.assertTrue(urlElementMap.containsKey("VERSION"));
		Assert.assertTrue("1.3.0".equalsIgnoreCase(urlElementMap.get("VERSION")));
		Assert.assertTrue(urlElementMap.containsKey("REQUEST"));
		Assert.assertTrue("GetMap".equalsIgnoreCase(urlElementMap.get("REQUEST")));

		Assert.assertTrue(urlElementMap.containsKey("STYLES"));
		Assert.assertTrue("".equalsIgnoreCase(urlElementMap.get("STYLES")));
		Assert.assertTrue(urlElementMap.containsKey("LAYERS"));
		Assert.assertTrue(owsLayer.getName().equalsIgnoreCase(urlElementMap.get("LAYERS")));
		Assert.assertTrue(urlElementMap.containsKey("FORMAT"));
		Assert.assertTrue("image%2Fpng".equalsIgnoreCase(urlElementMap.get("FORMAT")));
		Assert.assertTrue(urlElementMap.containsKey("TRANSPARENT"));
		Assert.assertTrue("true".equalsIgnoreCase(urlElementMap.get("TRANSPARENT")));
		Assert.assertTrue(urlElementMap.containsKey("CRS"));
		Assert.assertTrue(toCrs.equalsIgnoreCase(urlElementMap.get("CRS")));

		Bbox extend = rasterCapabilitiesInfo.getExtent();
		Assert.assertTrue(urlElementMap.containsKey("BBOX"));
		String[] extendInPreview = urlElementMap.get("BBOX").split(",");
		Assert.assertEquals(4, extendInPreview.length);
		Assert.assertEquals(extend.getX(), Double.parseDouble(extendInPreview[0]), 0.1);
		Assert.assertEquals(extend.getY(), Double.parseDouble(extendInPreview[1]), 0.1);
		Assert.assertEquals(extend.getMaxX(), Double.parseDouble(extendInPreview[2]), 0.1);
		Assert.assertEquals(extend.getMaxY(), Double.parseDouble(extendInPreview[3]), 0.1);
	}

	@Test
	public void testNoGetFeatureInfoFromWms() throws Exception {
		// mock WebMapServer
		WebMapServer webMapServerMock = Mockito.mock(WebMapServer.class);
		GetMapRequest getMapRequestMock = Mockito.mock(GetMapRequest.class);
		WMSCapabilities wmsCapabilitiesMock = Mockito.mock(WMSCapabilities.class);
		WMSRequest wmsRequestMock = Mockito.mock(WMSRequest.class);
		String url = "http://apps.geomajas.org/geoserver/ows?service=WMS&request=GetCapabilities&version=1.3.0";
		Mockito.stub(webMapServerMock.getCapabilities()).toReturn(wmsCapabilitiesMock);
		Mockito.stub(wmsCapabilitiesMock.getRequest()).toReturn(wmsRequestMock);
		Mockito.stub(wmsRequestMock.getGetFeatureInfo()).toReturn(null);
		Mockito.stub(webMapServerMock.createGetMapRequest()).toReturn(getMapRequestMock);
		Mockito.stub(getMapRequestMock.getFinalURL()).toReturn(new URL(url));

		// create and fill Layer
		Layer owsLayer = new Layer();
		owsLayer.setName("owsLayerName");
		CRSEnvelope latLonBoundingBox = new CRSEnvelope("CRS:84", -180, -90, 180, 90);
		owsLayer.setLatLonBoundingBox(latLonBoundingBox);

		String toCrs = "EPSG:3857";

		RasterCapabilitiesInfo rasterCapabilitiesInfo = dtoFactoryService.buildRasterCapabilitesInfoFromWms(
				webMapServerMock, owsLayer, toCrs);

		Assert.assertNull(rasterCapabilitiesInfo.getGetFeatureInfoFormats());
	}


}
