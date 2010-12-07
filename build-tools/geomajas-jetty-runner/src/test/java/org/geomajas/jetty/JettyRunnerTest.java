package org.geomajas.jetty;

import org.eclipse.jetty.server.Server;
import org.junit.Test;

public class JettyRunnerTest {
	@Test
	public void testStartStop() throws Exception {
		Server server = JettyRunner.startServer();
		server.stop();
	}
}
