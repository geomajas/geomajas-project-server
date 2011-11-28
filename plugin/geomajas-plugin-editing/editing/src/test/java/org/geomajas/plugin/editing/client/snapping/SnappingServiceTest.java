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

package org.geomajas.plugin.editing.client.snapping;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.junit.Test;

/**
 * Testcase for the methods of the {@link SnappingService}.
 * 
 * @author Pieter De Graef
 */
public class SnappingServiceTest {

	private SnappingService service = new SnappingService();

	private SnappingSourceProvider sourceProvider = new StaticSnappingSourceProvider();

	private SnappingAlgorithm algorithm = new FirstCoordinateSnappingAlgorithm();

	@Test
	public void testSnapping() {
		service.addSnappingRule(algorithm, sourceProvider, 10, true);
		service.update(null);
		Coordinate result = service.snap(new Coordinate(5, 5));
		Assert.assertEquals(0.0, result.getX());
		Assert.assertEquals(0.0, result.getY());
	}
}