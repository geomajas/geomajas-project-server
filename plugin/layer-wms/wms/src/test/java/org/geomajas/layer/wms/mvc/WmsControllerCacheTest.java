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

package org.geomajas.layer.wms.mvc;

import org.geomajas.layer.common.proxy.CachingLayerHttpService;
import org.geomajas.layer.wms.WmsLayer;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.TestRecorder;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.geomajas.testdata.rule.SecurityRule;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for {@link org.geomajas.layer.wms.mvc.WmsProxyController}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml", "/wmsContext.xml",
		"/org/geomajas/spring/testRecorder.xml", "/org/geomajas/testdata/allowAll.xml"})
public class WmsControllerCacheTest {

	private static final String IMAGE_CLASS_PATH = "reference";

	private static final double DELTA = 1E-6;

	@Autowired
	private WmsProxyController wmsController;
	
	@Autowired
	private TestRecorder testRecorder;

	@Autowired
	@Rule
	public SecurityRule securityRule;

	@Autowired
	private CacheManagerService cacheManagerService;

	@Autowired
	@Qualifier("cachedBlue")
	private WmsLayer cachedWms;

	@After
	public void clearCache() throws Exception {
		// clear side effects to assure the next test run works
		cacheManagerService.drop(cachedWms);
		Thread.sleep(5000);
	}

	@Test
	//@Ignore // test fails as the passivated state is not removed, see CACHE-33
	public void testReadCachedImage() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("SERVICE", "WMS");
		parameters.put("layers", "bluemarble");
		parameters.put("WIDTH", "512");
		parameters.put("HEIGHT", "512");
		parameters.put("bbox", "-52.01245495052001,-28.207099921352835,11.947593278789554,35.75294830795673");
		parameters.put("format", "image/jpeg");
		parameters.put("version", "1.1.1");
		parameters.put("srs", "EPSG:4326");
		parameters.put("styles", "");
		parameters.put("request", "GetMap");
		request.setParameters(parameters);
		request.setRequestURI("d/wms/cachedBlue/");
		request.setQueryString("SERVICE=WMS&layers=bluemarble&" +
				"WIDTH=512&HEIGHT=512&bbox=-52.01245495052001,-28.207099921352835,11.947593278789554," +
				"35.75294830795673&format=image/jpeg&version=1.1.1&srs=EPSG:4326&styles=&request=GetMap");
		request.setMethod("GET");

		testRecorder.clear();
		wmsController.getWms(request, response);
		new ImageAssert(response).assertEqualImage("wms.jpg", false, DELTA);
		assertThat(testRecorder.matches(CachingLayerHttpService.TEST_RECORDER_GROUP,
				CachingLayerHttpService.TEST_RECORDER_PUT_IN_CACHE)).isEmpty();

		testRecorder.clear();
		wmsController.getWms(request, response);
		new ImageAssert(response).assertEqualImage("wms.jpg", false, DELTA);
		assertThat(testRecorder.matches(CachingLayerHttpService.TEST_RECORDER_GROUP,
				CachingLayerHttpService.TEST_RECORDER_GET_FROM_CACHE)).isEmpty();
	}

	class ImageAssert extends TestPathBinaryStreamAssert {

		private MockHttpServletResponse response;

		public ImageAssert(MockHttpServletResponse response) {
			super(IMAGE_CLASS_PATH);
			this.response = response;
		}

		public void generateActual(OutputStream out) throws Exception {
			out.write(response.getContentAsByteArray());
		}
	}

}
