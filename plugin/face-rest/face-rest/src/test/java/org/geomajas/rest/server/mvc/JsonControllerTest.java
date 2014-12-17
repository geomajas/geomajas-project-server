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

package org.geomajas.rest.server.mvc;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.geomajas.command.Command;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rest.server.RestException;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.GeoService;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/rest/dummySecurity.xml" })
public class JsonControllerTest {


	@Autowired
	private SecurityManager securityManager;

	private HandlerAdapter adapter;

	@Autowired
	protected CommandDispatcher commandDispatcher;

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void TestReflection() {
	 	String commandName = "command.configuration.Get";
		//create json configuration command request map
		Map<String, String> jsonRequest = new HashMap<String, String>();
		jsonRequest.put("applicationId", "rest-app");

		Command command = (Command) applicationContext.getBean(commandName);

		Type requestObjectType = command.getClass().getGenericInterfaces()[0];
		try {
			CommandRequest commandRequest = (CommandRequest) requestObjectType.getClass().newInstance();
			org.apache.commons.beanutils.BeanUtils.populate(commandRequest, jsonRequest);
			commandDispatcher.execute(commandName, commandRequest, null, "en");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}
}
