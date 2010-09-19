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

package org.geomajas.service.pipeline;

import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;

/**
 * Context which is provided to a pipeline context to help execute.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface PipelineContext {

	/**
	 * Get the value for a key.
	 * <p/>
	 * These values can be used to pass values between the pipeline steps.
	 *
	 * @param key key for which the value needs to be obtained.
	 * @return value for key
	 * @throws GeomajasException no value found for key
	 */
	Object get(String key) throws GeomajasException;

	/**
	 * Get the value for a key.
	 * <p/>
	 * These values can be used to pass values between the pipeline steps.
	 *
	 * @param key key for which the value needs to be obtained.
	 * @return value for key or null
	 */
	Object getOptional(String key);

	/**
	 * Get the value for a key.
	 * <p/>
	 * These values can be used to pass values between the pipeline steps.
	 *
	 * @param key key for which the value needs to be obtained.
	 * @param type class which needs to be used for the parameter
	 * @param <TYPE> type for the object which needs to be get
	 * @return value for key
	 * @throws GeomajasException no value of correct type found for key
	 */
	<TYPE> TYPE get(String key, Class<TYPE> type) throws GeomajasException;

	/**
	 * Get the value for a key.
	 * <p/>
	 * These values can be used to pass values between the pipeline steps.
	 *
	 * @param key key for which the value needs to be obtained.
	 * @param type class which needs to be used for the parameter
	 * @param <TYPE> type for the object which needs to be get
	 * @return value for key or null
	 */
	<TYPE> TYPE getOptional(String key, Class<TYPE> type);

	/**
	 * Put context value which may be accessed by later pipeline steps.
	 *
	 * @param key key for value
	 * @param value value for key
	 * @return previous value stored for this key
	 */
	Object put(String key, Object value);

	/**
	 * Has the pipeline finished execution?
	 * <p/>
	 * Has a pipeline step indicated that the pipeline should finish executing?
	 *
	 * @return true when pipeline should finish
	 */
	boolean isFinished();

	/**
	 * By setting this to true, you can indicate that following steps in the pipeline should not be executed.
	 *
	 * @param finished indicates whether the pipeline should stop
	 */
	void setFinished(boolean finished);

}
