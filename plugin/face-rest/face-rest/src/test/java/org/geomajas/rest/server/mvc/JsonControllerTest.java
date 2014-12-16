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

import org.apache.commons.beanutils.BeanUtils;
import org.geomajas.command.Command;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.rest.server.command.CommandUtils;
import org.geomajas.security.SecurityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
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
	public void TestReflection() throws Exception {
	 	String commandName = GetConfigurationRequest.COMMAND;
		//create json configuration command request map
		Map<String, String> jsonRequest = new HashMap<String, String>();
		jsonRequest.put("applicationId", "rest-app");

		Command command = (Command) applicationContext.getBean(commandName);
		CommandRequest commandRequest = CommandUtils.createCommandRequest(command);
		BeanUtils.populate(commandRequest, jsonRequest);
		commandDispatcher.execute(commandName, commandRequest, null, "en");
	}
}
