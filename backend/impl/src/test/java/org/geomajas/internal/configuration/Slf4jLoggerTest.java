package org.geomajas.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;
import org.junit.Test;

public class Slf4jLoggerTest {

    @Test
	public void testSlf4j() throws ClassNotFoundException {
		try {
			Logging.GEOTOOLS.setLoggerFactory("org.geomajas.internal.configuration.Slf4jLoggerFactory");
			Logger log = Logging.getLogger("org.geotools.SLF4JLoggerTestTrace");
			assertTrue(log instanceof Slf4jLogger);
			assertEquals(Level.FINEST, log.getLevel());
			log = Logging.getLogger("org.geotools.SLF4JLoggerTestDebug");
			assertTrue(log instanceof Slf4jLogger);
			assertEquals(Level.FINE, log.getLevel());
			log = Logging.getLogger("org.geotools.SLF4JLoggerTestInfo");
			assertTrue(log instanceof Slf4jLogger);
			assertEquals(Level.INFO, log.getLevel());
			log = Logging.getLogger("org.geotools.SLF4JLoggerTestWarn");
			assertTrue(log instanceof Slf4jLogger);
			assertEquals(Level.WARNING, log.getLevel());
			log = Logging.getLogger("org.geotools.SLF4JLoggerTestError");
			assertTrue(log instanceof Slf4jLogger);
			assertEquals(Level.SEVERE, log.getLevel());
			log = Logging.getLogger("org.geotools.SLF4JLoggerTestOff");
			assertTrue(log instanceof Slf4jLogger);
			assertEquals(Level.OFF, log.getLevel());
		} finally {
			Logging.GEOTOOLS.setLoggerFactory((String) null);
			assertEquals(Logger.class, Logging.getLogger("org.geotools").getClass());
		}
	}
}
