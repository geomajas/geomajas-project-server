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
 * 
 */
public class ReconfigurableClassPathApplicationContext extends ClassPathXmlApplicationContext implements
		ReconfigurableApplicationContext {

	private String[] previousConfigLocations;

	/**
	 * Create a context for the specified location (string with multiple locations separated by white space or ,;).
	 * 
	 * @param configLocation
	 * @throws BeansException
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
