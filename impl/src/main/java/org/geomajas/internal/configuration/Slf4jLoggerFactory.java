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
package org.geomajas.internal.configuration;

import java.util.logging.Logger;

import org.geotools.util.logging.LoggerFactory;

/**
 * A factory for loggers that redirect all Java logging events to the <A HREF="http://www.slf4j.org/">SLF4J</A>
 * framework.
 * 
 * 
 * @author Jan De Moerloose
 */
public class Slf4jLoggerFactory extends LoggerFactory<org.slf4j.Logger> {

	/**
	 * The unique instance of this factory.
	 */
	private static Slf4jLoggerFactory factory;

	/**
	 * Constructs a default factory.
	 * 
	 * @throws NoClassDefFoundError if SLF4J's {@code Logger} class was not found on the classpath.
	 */
	protected Slf4jLoggerFactory() throws NoClassDefFoundError {
		super(org.slf4j.Logger.class);
	}

	/**
	 * Returns the unique instance of this factory.
	 * 
	 * @throws NoClassDefFoundError if SLF4J's {@code Logger} class was not found on the classpath.
	 */
	public static synchronized Slf4jLoggerFactory getInstance() throws NoClassDefFoundError {
		if (factory == null) {
			factory = new Slf4jLoggerFactory();
		}
		return factory;
	}

	/**
	 * Returns the implementation to use for the logger of the specified name, or {@code null} if the logger would
	 * delegates to Java logging anyway.
	 */
	protected org.slf4j.Logger getImplementation(final String name) {
		return org.slf4j.LoggerFactory.getLogger(name);
	}

	/**
	 * Wraps the specified {@linkplain #getImplementation implementation} in a Java logger.
	 */
	protected Logger wrap(String name, org.slf4j.Logger implementation) {
		return new Slf4jLogger(name, implementation);
	}

	/**
	 * Returns the {@linkplain #getImplementation implementation} wrapped by the specified logger, or {@code null} if
	 * none.
	 */
	protected org.slf4j.Logger unwrap(final Logger logger) {
		if (logger instanceof Slf4jLogger) {
			return ((Slf4jLogger) logger).getLogger();
		}
		return null;
	}
}
