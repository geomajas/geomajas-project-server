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

package org.geomajas.plugin.editing.client.snap;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.editing.client.snap.SnapAlgorithm;
import org.geomajas.plugin.editing.client.snap.SnapService;
import org.geomajas.plugin.editing.client.snap.SnapSourceProvider;
import org.junit.Test;

/**
 * Testcase for the methods of the {@link SnapService}.
 * 
 * @author Pieter De Graef
 */
public class SnapServiceTest {

	private SnapService service = new SnapService();

	private SnapSourceProvider sourceProvider = new StaticSnapSourceProvider();

	private SnapAlgorithm algorithm = new FirstCoordinateSnapAlgorithm();

	@Test
	public void testSnapping() {
		service.addSnappingRule(algorithm, sourceProvider, 10, true);
		service.update(null);
		Coordinate result = service.snap(new Coordinate(5, 5));
		Assert.assertEquals(0.0, result.getX());
		Assert.assertEquals(0.0, result.getY());
	}
}