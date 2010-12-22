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
package org.geomajas.dojo.server.servlet;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.ParseException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.dojo.server.json.AnnotatedBeanSerializer;
import org.geomajas.dojo.server.json.BigNumberSerializer;
import org.geomajas.dojo.server.json.ColorSerializer;
import org.geomajas.dojo.server.json.DtoGeometrySerializer;
import org.geomajas.dojo.server.json.FontSerializer;
import org.geomajas.dojo.server.json.GeometrySerializer;
import org.geomajas.dojo.server.json.JsonObjectWriter;
import org.geomajas.dojo.server.json.RectangleSerializer;
import org.geomajas.servlet.ApplicationContextUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.metaparadigm.jsonrpc.ArraySerializer;
import com.metaparadigm.jsonrpc.BooleanSerializer;
import com.metaparadigm.jsonrpc.DateSerializer;
import com.metaparadigm.jsonrpc.DictionarySerializer;
import com.metaparadigm.jsonrpc.ErrorInvocationCallback;
import com.metaparadigm.jsonrpc.JSONRPCBridge;
import com.metaparadigm.jsonrpc.JSONRPCResult;
import com.metaparadigm.jsonrpc.ListSerializer;
import com.metaparadigm.jsonrpc.MapSerializer;
import com.metaparadigm.jsonrpc.NumberSerializer;
import com.metaparadigm.jsonrpc.PrimitiveSerializer;
import com.metaparadigm.jsonrpc.SetSerializer;
import com.metaparadigm.jsonrpc.StringSerializer;
import org.springframework.web.context.ContextLoaderListener;

/**
 * This servlet handles JSON-RPC requests over HTTP and hands them to a JSONRPCBridge instance registered in the
 * HttpSession.
 * <p />
 * By default, the GGISJSONServlet places an instance of the JSONRPCBridge object is automatically in the HttpSession
 * object registered under the attribute "JSONRPCBridge".
 * <p />
 * The following can be added to your web.xml to export the servlet under the URI &quot;<code>/JSON-RPC</code>&quot;
 * <p />
 * <code>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;com.metaparadigm.jsonrpc.JSONRPCServlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;com.metaparadigm.jsonrpc.JSONRPCServlet&lt;/servlet-class&gt;
 * &lt;/servlet&gt;
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;com.metaparadigm.jsonrpc.JSONRPCServlet&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/JSON-RPC&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </code>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public class JsonServlet extends HttpServlet implements ErrorInvocationCallback,
		ApplicationListener<ContextRefreshedEvent> {

	private static final int BUFFER_SIZE = 1024;

	private static final String JSON_RPC_BRIDGE_ATTRIBUTE = "JSONRPCBridge";

	private static final long serialVersionUID = -6972738675426509939L;

	private final Logger log = LoggerFactory.getLogger(JsonServlet.class);

	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		log.info("JSON servlet init");
		log.debug("current working directory = {}", System.getProperty("user.dir"));

		ApplicationContext applicationContext = ContextLoaderListener.getCurrentWebApplicationContext();
		if (applicationContext instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) applicationContext).addApplicationListener(this);
		}
		initBridge(config, applicationContext);
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			initBridge(getServletConfig(), event.getApplicationContext());
		} catch (ServletException e) {
			log.error("Could not reinitialize JSON bridge", e);
		}
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Find the JSONRPCBridge for this session or create one
		// if it doesn't exist
		long startTime = System.currentTimeMillis();
		log.debug("Incoming JSON message");

		JSONRPCBridge jsonBridge = (JSONRPCBridge) getServletContext().getAttribute(JSON_RPC_BRIDGE_ATTRIBUTE);

		// updateUserContext(request);

		// Encode using UTF-8, although We are actually ASCII clean as
		// all unicode data is JSON escaped using backslash u. This is
		// less data efficient for foreign character sets but it is
		// needed to support naughty browsers such as Konqueror and Safari
		// which do not honour the charset set in the response
		response.setContentType("text/plain;charset=utf-8");

		JSONObject jsonReq = null;
		JSONRPCResult jsonRes = null;

		// Decode using the charset in the request if it exists otherwise
		// use UTF-8 as this is what all browser implementations use.
		// The JSON-RPC-Java JavaScript client is ASCII clean so it
		// although here we can correctly handle data from other clients
		// that do not escape non ASCII data
		String charset = request.getCharacterEncoding();
		if (charset == null) {
			charset = "UTF-8";
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), charset));

		// Read the request
		CharArrayWriter data = new CharArrayWriter();
		char[] buf = new char[BUFFER_SIZE];
		int ret;
		while ((ret = in.read(buf, 0, BUFFER_SIZE)) != -1) {
			data.write(buf, 0, ret);
		}
		writeReceive(data.toString());
		try {
			jsonReq = new JSONObject(data.toString());
			// Process the request
			jsonRes = jsonBridge.call(new Object[] { request }, jsonReq);
		} catch (ParseException e) {
			log.error("can't parse call: " + data, e);
			jsonRes = new JSONRPCResult(JSONRPCResult.CODE_ERR_PARSE, null, JSONRPCResult.MSG_ERR_PARSE);
		}

		// Write the response
		JsonObjectWriter writer = new JsonObjectWriter(response.getWriter(), 3);
		writer.write(jsonRes);
		writer.flush();

		JSONArray jsonArray = (JSONArray) jsonReq.get("params");
		long millis = System.currentTimeMillis() - startTime;
		log.debug("Command execution time: " + millis + " ms - class=" + jsonArray.toString());
	}

	private void writeReceive(String receive) {
		if (log.isDebugEnabled()) {
			String str = receive.toString();
			int index = 0;
			while (index + 999 < str.length()) {
				log.debug("service: receive [" + index + "] " + str.substring(index, index + 999));
				index += 1000;
			}
			log.debug("service: receive " + str.substring(index));
		}
	}

	public void postInvoke(Object context, Object instance, Method method, Object result) throws Exception {
	}

	public void preInvoke(Object context, Object instance, Method method, Object[] arguments) throws Exception {
	}

	public void invocationError(Object context, Object instance, Method method, Throwable error) {
		log.error("JSON invocation error", error);
	}

	private void initBridge(ServletConfig config, ApplicationContext applicationContext) throws ServletException {
		// Create the JSON RPC Bridge (context-wide object)
		JSONRPCBridge jsonBridge = new JSONRPCBridge(false);
		try {
			// Order is important !!!!
			jsonBridge.registerSerializer(applicationContext.getBean("dojo.server.json.AnnotatedBeanSerializer",
					AnnotatedBeanSerializer.class));
			jsonBridge.registerSerializer(new GeometrySerializer());
			jsonBridge.registerSerializer(new DtoGeometrySerializer());
			jsonBridge.registerSerializer(new BigNumberSerializer());
			jsonBridge.registerSerializer(new ArraySerializer());
			jsonBridge.registerSerializer(new DictionarySerializer());
			jsonBridge.registerSerializer(new MapSerializer());
			jsonBridge.registerSerializer(new SetSerializer());
			jsonBridge.registerSerializer(new ListSerializer());
			jsonBridge.registerSerializer(new DateSerializer());
			jsonBridge.registerSerializer(new StringSerializer());
			jsonBridge.registerSerializer(new NumberSerializer());
			jsonBridge.registerSerializer(new BooleanSerializer());
			jsonBridge.registerSerializer(new PrimitiveSerializer());
			jsonBridge.registerSerializer(new RectangleSerializer());
			jsonBridge.registerSerializer(new ColorSerializer());
			jsonBridge.registerSerializer(new FontSerializer());
		} catch (Exception e) {
			throw new ServletException("json : could not register all serializers", e);
		}

		// register the controller object
		CommandDispatcher commandDispatcher = applicationContext.getBean("command.CommandDispatcher",
				CommandDispatcher.class);
		if (null == commandDispatcher) {
			throw new ServletException(
					"Cannot find CommandDispatcher, the org.geomajas.internal.global.GeomajasContextListener "
							+ "was probably not registered.");
		}
		jsonBridge.registerObject("CommandDispatcher", commandDispatcher);
		jsonBridge.registerCallback(this, HttpServletRequest.class);

		// put in application context
		config.getServletContext().setAttribute(JSON_RPC_BRIDGE_ATTRIBUTE, jsonBridge);
	}

}
