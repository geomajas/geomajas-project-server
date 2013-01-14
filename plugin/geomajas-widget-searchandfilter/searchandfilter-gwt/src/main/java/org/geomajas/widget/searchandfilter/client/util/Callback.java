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
package org.geomajas.widget.searchandfilter.client.util;

import com.smartgwt.client.core.Function;

/**
 * Use for asynchronous methods to be notified when they are done.
 *
 * @author Kristof Heirwegh
 */
public interface Callback extends Function {
	// Ok, I know I'm nitpicking, but "Function" is no name to call a callback...
}
