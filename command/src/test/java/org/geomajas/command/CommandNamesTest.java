package org.geomajas.command;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml" })
public class CommandNamesTest {

	@Autowired
	Map<String, Command> commands;

	@Test
	public void testNames() throws Exception {
		for (Map.Entry<String,Command> entry : commands.entrySet()) {
			String className = entry.getValue().getClass().getSimpleName();
			Class requestClass = Class.forName("org.geomajas.command.dto."+className.substring(0, className.length()-"Command".length())+"Request");
			Assert.assertEquals(entry.getKey(), requestClass.getField("COMMAND").get(null));
		}
	}
}
