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
package org.geomajas.rest.server.command;

import org.geomajas.command.Command;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

/**
 * Geomajas Command API Utils.
 *
 * @author  Dosi Bingov
 */
public final class CommandUtils {

	private CommandUtils() {
	}

	/**
	 * Creates an empty {@link CommandRequest} object for a given command.
	 *
	 * @param command a {@link Command} instance.
	 * @return {@link CommandRequest}
	 */
	public static CommandRequest createCommandRequest(Command command) {
		ParameterizedType requestObjectType = (ParameterizedType) command.getClass().getGenericInterfaces()[0];
		Class commandRequestClazz = (Class) requestObjectType.getActualTypeArguments()[0];
		return  (CommandRequest) createInstance(commandRequestClazz);
	}


	/**
	 * Creates an empty {@link CommandResponse} object for a given command.
	 *
	 * @param command a {@link Command} instance.
	 * @return {@link CommandResponse}
	 */
	public static CommandResponse createCommandResponse(Command command) {
		ParameterizedType responseObjectType = (ParameterizedType) command.getClass().getGenericInterfaces()[0];
		Class commandResponseClazz = (Class) responseObjectType.getActualTypeArguments()[1];
		return   (CommandResponse) createInstance(commandResponseClazz);
	}

	/**
	 * Silently creates an instance of a class.
	 *
	 * @param clazz type of the class we want to create.
	 *
	 * @return instance the object that belongs to the given class.
	 */
	private static <T> T createInstance(Class<T> clazz) throws IllegalStateException {
		try {
			ReflectionFactory rf =
					ReflectionFactory.getReflectionFactory();
			Constructor objDef = Object.class.getDeclaredConstructor();
			Constructor defaultConstructor = rf.newConstructorForSerialization(clazz, objDef);
			return clazz.cast(defaultConstructor.newInstance());
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new IllegalStateException("Cannot create object for class " + clazz.getName(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Cannot create object for class " + clazz.getName(), e);
		}
	}

}
