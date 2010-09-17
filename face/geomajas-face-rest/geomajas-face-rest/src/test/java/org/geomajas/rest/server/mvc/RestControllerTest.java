package org.geomajas.rest.server.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rest.server.RestException;
import org.geomajas.security.SecurityManager;
import org.geotools.geojson.GeoJSONUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.vividsolutions.jts.geom.Envelope;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml" })
public class RestControllerTest {

	private final Logger log = LoggerFactory.getLogger(RestControllerTest.class);

	@Autowired
	@Qualifier("/rest/**")
	RestController restController;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	@Qualifier("GeoJsonView")
	private View view;

	private HandlerAdapter adapter;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
		adapter = new AnnotationMethodHandlerAdapter();
	}

	@Test
	public void testReadOneFeature() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/rest/beans/1.json");
		request.setMethod("GET");

		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mav = adapter.handle(request, response, restController);

		Object o = mav.getModel().get(RestController.FEATURE_COLLECTION);
		Assert.assertTrue(o instanceof InternalFeature);
		InternalFeature feature = (InternalFeature) o;
		Assert.assertTrue(feature instanceof InternalFeature);

		Assert.assertEquals("bean1", feature.getAttributes().get("stringAttr").getValue());
		Assert.assertEquals(true, feature.getAttributes().get("booleanAttr").getValue());
		Assert.assertEquals("100,23", feature.getAttributes().get("currencyAttr").getValue());
		Calendar c = Calendar.getInstance();
		c.set(2010, 1, 23, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		Assert.assertEquals(c.getTime(), feature.getAttributes().get("dateAttr").getValue());
		Assert.assertEquals(123.456, feature.getAttributes().get("doubleAttr").getValue());
		Assert.assertEquals(456.789F, feature.getAttributes().get("floatAttr").getValue());
		Assert.assertEquals("http://www.geomajas.org/image1", feature.getAttributes().get("imageUrlAttr").getValue());
		Assert.assertEquals(789, feature.getAttributes().get("integerAttr").getValue());
		Assert.assertEquals(123456789L, feature.getAttributes().get("longAttr").getValue());
		Assert.assertEquals((short) 123, feature.getAttributes().get("shortAttr").getValue());
		Assert.assertEquals("http://www.geomajas.org/url1", feature.getAttributes().get("urlAttr").getValue());

		view.render(mav.getModel(), request, response);
		response.flushBuffer();
		Object json = new JSONParser().parse(response.getContentAsString());
		String isodate = GeoJSONUtil.DATE_FORMAT.format(c.getTime());
		Assert.assertTrue(json instanceof JSONObject);
		Assert
				.assertEquals(
						"{\"type\":\"Feature\"," +
						"\"geometry\":{\"type\":\"MultiPolygon\"," +
						"\"coordinates\":[[[[0.0,0.0],[1,0.0],[1,1],[0.0,1],[0.0,0.0]]]]}," +
						"\"properties\":{" +
						"\"stringAttr\":\"bean1\"," +
						"\"booleanAttr\":true," +
						"\"currencyAttr\":\"100,23\"," +
						"\"dateAttr\":\""+isodate+"\"," +
						"\"doubleAttr\":123.456,\"floatAttr\":456.789," +
						"\"imageUrlAttr\":\"http://www.geomajas.org/image1\"," +
						"\"integerAttr\":789,\"longAttr\":123456789," +
						"\"shortAttr\":123," +
						"\"urlAttr\":\"http://www.geomajas.org/url1\"}," +
						"\"id\":\"1\"}",
						response.getContentAsString());

	}

	@Test
	public void testBboxFilter() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/rest/beans");
		request.addParameter("bbox", "4,6,0,3");
		request.setMethod("GET");

		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mav = adapter.handle(request, response, restController);
		Object o = mav.getModel().get(RestController.FEATURE_COLLECTION);
		Assert.assertTrue(o instanceof List<?>);
		for (Object f : (List<?>) o) {
			Assert.assertTrue(f instanceof InternalFeature);
			Assert.assertTrue(new Envelope(4, 6, 0, 3).intersects(((InternalFeature) f).getGeometry()
					.getEnvelopeInternal()));
		}

	}

	@Test
	public void testFeaturePaging() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/rest/beans");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		// check all
		ModelAndView mav = adapter.handle(request, response, restController);
		Assert.assertEquals(Arrays.asList("1", "2", "3"), getIdsFromModel(mav.getModel()));
		// check first 1
		request.setParameter("maxFeatures", "1");
		mav = adapter.handle(request, response, restController);
		Assert.assertEquals(Arrays.asList("1"), getIdsFromModel(mav.getModel()));
		// check first 2
		request.setParameter("maxFeatures", "2");
		mav = adapter.handle(request, response, restController);
		Assert.assertEquals(Arrays.asList("1", "2"), getIdsFromModel(mav.getModel()));
		// check 1 -3
		request.setParameter("maxFeatures", "2");
		request.setParameter("offset", "1");
		mav = adapter.handle(request, response, restController);
		Assert.assertEquals(Arrays.asList("2", "3"), getIdsFromModel(mav.getModel()));
	}

	@Test
	public void testOrdering() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/rest/beans");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		// check descending on string attribute
		request.setParameter("order_by", "stringAttr");
		request.setParameter("dir", "DESC");
		ModelAndView mav = adapter.handle(request, response, restController);
		Assert.assertEquals(Arrays.asList("3", "2", "1"), getIdsFromModel(mav.getModel()));
	}

	@Test
	public void testAttributeFiltering() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/rest/beans");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		// check attribute equality
		request.setParameter("queryable", "stringAttr");
		request.setParameter("stringAttr_eq", "bean2");
		ModelAndView mav = adapter.handle(request, response, restController);
		Assert.assertEquals(Arrays.asList("2"), getIdsFromModel(mav.getModel()));
		// check range
		request.removeAllParameters();
		request.setParameter("queryable", "doubleAttr");
		request.setParameter("doubleAttr_lt", "200");
		request.setParameter("doubleAttr_gt", "100");
		Assert.assertEquals(Arrays.asList("2"), getIdsFromModel(mav.getModel()));
	}

	@Test
	public void testWrongLayerId() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/rest/badlayer");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		try {
			adapter.handle(request, response, restController);
			Assert.fail("layer badlayer should not exist");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof RestException);
			Assert.assertEquals(RestException.PROBLEM_READING_LAYERSERVICE, ((RestException) e).getExceptionCode());
		}
	}

	@Test
	public void testWrongFeatureId() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/rest/beans/200.json");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		try {
			adapter.handle(request, response, restController);
			Assert.fail("feature 200 should not exist");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof RestException);
			Assert.assertEquals(RestException.FEATURE_NOT_FOUND, ((RestException) e).getExceptionCode());
		}
	}

	@Test
	public void testMissingAttribute() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/rest/beans");
		request.setMethod("GET");
		request.setParameter("queryable", "noSuchAttr");
		request.setParameter("noSuchAttr_eq", "200");
		MockHttpServletResponse response = new MockHttpServletResponse();
		try {
			adapter.handle(request, response, restController);
			Assert.fail("attribute noSuchAttr should not exist");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof RestException);
			Assert.assertEquals(RestException.PROBLEM_READING_LAYERSERVICE, ((RestException) e).getExceptionCode());
		}
	}

	private List<String> getIdsFromModel(Map<String, Object> model) {
		Object o = model.get(RestController.FEATURE_COLLECTION);
		Assert.assertTrue(o instanceof List<?>);
		List<?> ff = (List<?>) o;
		ArrayList<String> ids = new ArrayList<String>();
		for (Object f : ff) {
			Assert.assertTrue(f instanceof InternalFeature);
			ids.add(((InternalFeature) f).getId());
		}
		return ids;
	}

}
