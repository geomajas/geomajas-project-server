/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.json;

import junit.framework.TestCase;
import org.geomajas.global.Json;

/**
 * Verify usability of the @Json annotation
 *
 * @author Jan De Moerloose
 */
public class AnnotationTest extends TestCase {

	@Json(serialize = true)
	public void testAnnotation() throws SecurityException, NoSuchMethodException {
		assertTrue(getClass().getMethod("testAnnotation").isAnnotationPresent(Json.class));
		assertTrue((getClass().getMethod("testAnnotation").getAnnotation(Json.class)).serialize());
	}

}
