/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.configuration;

import java.util.logging.Level;

import org.geotools.util.logging.LoggerAdapter;

/**
 * An adapter that redirect all Java logging events to the
 * <A HREF="http://www.slf4j.org">SLF4J</A> framework.
 *
 * @author Jan De Moerloose
 *
 * @see Slf4jLoggerFactory
 * @see org.geotools.util.logging.Logging
 */
public class Slf4jLogger extends LoggerAdapter {

	/**
	 * The SLF4J logger to use.
	 */
	private final org.slf4j.Logger logger;

	/**
	 * Creates a new logger.
	 * 
	 * @param name The logger name.
	 * @param logger The result of {@code LoggerFactory.getLogger(name)}.
	 */
	public Slf4jLogger(final String name, final org.slf4j.Logger logger) {
		super(name);
		this.logger = logger;
	}

	/**
	 * Set the level for this logger.
	 */
	public void setLevel(final Level level) {
		// not implemented by SLF4J
	}
	
	public org.slf4j.Logger getLogger() {
		return logger;
	}

	/**
	 * Returns the level for this logger.
	 */
	public Level getLevel() {
		if (logger.isTraceEnabled()) {
			return Level.FINEST;
		} else if (logger.isDebugEnabled()) {
			return Level.FINE;
		} else if (logger.isInfoEnabled()) {
			return Level.INFO;
		} else if (logger.isWarnEnabled()) {
			return Level.WARNING;
		} else if (logger.isErrorEnabled()) {
			return Level.SEVERE;
		} else {
			return Level.OFF;
		}
	}

	/**
	 * Returns {@code true} if the specified level is loggable.
	 */
	public boolean isLoggable(final Level level) {
		if (logger.isTraceEnabled()) {
			return level.intValue() >= Level.FINEST.intValue();
		} else if (logger.isDebugEnabled()) {
			return level.intValue() >= Level.FINE.intValue();
		} else if (logger.isInfoEnabled()) {
			return level.intValue() >= Level.INFO.intValue();
		} else if (logger.isWarnEnabled()) {
			return level.intValue() >= Level.WARNING.intValue();
		} else if (logger.isErrorEnabled()) {
			return level.intValue() >= Level.SEVERE.intValue();
		} else {
			return false;
		}
	}

	public void severe(String message) {
		logger.error(message);
	}

	public void warning(String message) {
		logger.warn(message);
	}

	public void info(String message) {
		logger.info(message);
	}

	public void config(String message) {
		logger.info(message);
	}

	public void fine(String message) {
		logger.debug(message);
	}

	public void finer(String message) {
		logger.debug(message);
	}

	public void finest(String message) {
		logger.trace(message);
	}
}