/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.spring;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A classpath application context that supports rollback after a refresh.
 * 
 * @author Jan De Moerloose
 */
public class ReconfigurableClassPathApplicationContext extends ClassPathXmlApplicationContext implements
		ReconfigurableApplicationContext {

	private String[] previousConfigLocations;

	/**
	 * Create a context for the specified location (string with multiple locations separated by white space or ,;).
	 * 
	 * @param configLocation string containing configuration locations
	 * @throws BeansException oops
	 */
	public ReconfigurableClassPathApplicationContext(String configLocation) throws BeansException {
		super();
		setConfigLocation(configLocation);
		refresh();
		// only now we can add the listener !
		addListener(new RollBackListener());
		// but we missed the first event !
		previousConfigLocations = getConfigLocations();
	}

	/**
	 * Refresh this context with the specified configuration locations.
	 * 
	 * @param configLocations
	 *            list of configuration resources (see implementation for specifics)
	 * @throws GeomajasException
	 *             indicates a problem with the new location files (see cause)
	 */
	public void refresh(String[] configLocations) throws GeomajasException {
		try {
			setConfigLocations(configLocations);
			refresh();
		} catch (Exception e) {
			throw new GeomajasException(e, ExceptionCode.REFRESH_CONFIGURATION_FAILED);
		}
	}

	/**
	 * Roll back to the previous configuration.
	 * 
	 * @throws GeomajasException
	 *             indicates an unlikely problem with the rollback (see cause)
	 */
	public void rollback() throws GeomajasException {
		try {
			setConfigLocations(previousConfigLocations);
			refresh();
		} catch (Exception e) {
			throw new GeomajasException(e, ExceptionCode.REFRESH_CONFIGURATION_FAILED);
		}
	}

	/**
	 * Keeps a copy the last configLocation on stop.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class RollBackListener implements ApplicationListener<ContextRefreshedEvent> {

		/**
		 * we have a fresh working context, copy the locations.
		 */
		public void onApplicationEvent(ContextRefreshedEvent event) {
			previousConfigLocations = getConfigLocations();
		}
	}

}
