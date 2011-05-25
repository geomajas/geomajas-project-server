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

package org.geomajas.command.dto;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.tile.TileCode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test to verify correct functioning of {@link GetVectorTileRequest}.
 *
 * @author Joachim Van der Auwera
 */
public class GetVectorTileRequestTest {

	@Test
	public void testEquals() {
		GetVectorTileRequest left = new GetVectorTileRequest();
		set(left);
		GetVectorTileRequest right = new GetVectorTileRequest();
		set(right);
		Assert.assertTrue(left.equals(left));
		Assert.assertTrue(left.equals(right));
		Assert.assertTrue(right.equals(left));
		Assert.assertFalse(left.equals("bla"));
		Assert.assertFalse(left.equals(null));
	}

	private void set(GetVectorTileRequest obj) {
		obj.setCode(new TileCode(1, 0, 0));
		obj.setCrs("EPSG:900913");
		obj.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		obj.setFilter("bla");
		obj.setLayerId("beans");
		obj.setPaintGeometries(true);
		obj.setPaintLabels(false);
		obj.setPanOrigin(new Coordinate(10, 20));
		obj.setRenderer("SVG");
		obj.setScale(2.5);
		NamedStyleInfo nsi = new NamedStyleInfo();
		nsi.setName("nsi");
		obj.setStyleInfo(nsi);
	}
}
