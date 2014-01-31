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

package org.geomajas.service.pipeline;

import org.geomajas.annotation.Api;
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
	 * Returns true if the context contains the specified key.
	 *
	 * @param key key which needs to be checked.
	 * @return true if the context contains the key, false otherwise
	 * @since 1.9.0
	 */
	boolean containsKey(String key);

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
	 * Get the value for a key.
	 * <p/>
	 * These values can be used to pass values between the pipeline steps.
	 *
	 * @param key key for which the value needs to be obtained.
	 * @param type class which needs to be used for the parameter
	 * @param defaultValue default value to be returned in case the value is missing
	 * @param <TYPE> type for the object which needs to be get
	 * @return value for key or null
	 * @since 1.10.0
	 */
	<TYPE> TYPE getOptional(String key, Class<TYPE> type, TYPE defaultValue);

	/**
	 * Put context value which may be accessed by later pipeline steps.
	 * <p/>
	 * The put needs to be ignored if the key is null.
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
