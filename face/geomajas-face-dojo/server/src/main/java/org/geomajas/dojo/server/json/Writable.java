/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.dojo.server.json;

import java.io.IOException;
import java.io.Writer;

/**
 * Mechanism for assuring custom types can be JSON serialized.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public interface Writable {

	void write(Writer writer) throws IOException;

}
