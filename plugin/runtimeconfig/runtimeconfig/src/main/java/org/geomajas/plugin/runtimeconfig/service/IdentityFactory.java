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
package org.geomajas.plugin.runtimeconfig.service;

/**
 * Dummy factory that just returns the object. Can be used in application context to re-autowire beans. The trick is to
 * introduce to re(create) an existing object in a child context as a prototype bean. This results in rewiring the
 * object without destroying it.
 * 
 * @author Jan De Moerloose
 * 
 */
public final class IdentityFactory {

	public static final String FACTORY_METHOD = "createIdentical";

	private IdentityFactory() {
	}

	/**
	 * Factory method. Does not really create anything, just passes along the original object (which is passed as a
	 * generic parameter).
	 * 
	 * @param o the original object
	 * @return the object itself
	 */
	public static Object createIdentical(Object o) {
		return o;
	}
}
