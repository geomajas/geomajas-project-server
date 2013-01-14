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
		set(left, true, false);
		GetVectorTileRequest right = new GetVectorTileRequest();
		set(right, true, false);
		Assert.assertTrue(left.equals(left));
		Assert.assertTrue(left.equals(right));
		Assert.assertTrue(right.equals(left));
		Assert.assertFalse(left.equals("bla"));
		Assert.assertFalse(left.equals(null));
	}

	@Test
	public void testIsPartOf() {
		GetVectorTileRequest labels = new GetVectorTileRequest();
		GetVectorTileRequest geometries = new GetVectorTileRequest();
		GetVectorTileRequest labelsAndGeometries = new GetVectorTileRequest();
		GetVectorTileRequest nothing = new GetVectorTileRequest();
		set(labels, false, true);
		set(geometries, true, false);
		set(labelsAndGeometries, true, true);
		set(nothing, false, false);
		
		Assert.assertTrue(nothing.isPartOf(labels));
		Assert.assertTrue(nothing.isPartOf(geometries));
		Assert.assertTrue(nothing.isPartOf(labelsAndGeometries));
		
		Assert.assertTrue(labels.isPartOf(labels));
		Assert.assertFalse(labels.isPartOf(geometries));
		Assert.assertTrue(labels.isPartOf(labelsAndGeometries));
		
		Assert.assertFalse(geometries.isPartOf(labels));
		Assert.assertTrue(geometries.isPartOf(geometries));
		Assert.assertTrue(geometries.isPartOf(labelsAndGeometries));

		Assert.assertFalse(labelsAndGeometries.isPartOf(labels));
		Assert.assertFalse(labelsAndGeometries.isPartOf(geometries));
		Assert.assertTrue(labelsAndGeometries.isPartOf(labelsAndGeometries));
		
		labels.setFilter("foo");
		Assert.assertFalse(labels.isPartOf(labelsAndGeometries));
		
}

	private void set(GetVectorTileRequest obj, boolean paintGeometries, boolean paintLabels) {
		obj.setCode(new TileCode(1, 0, 0));
		obj.setCrs("EPSG:900913");
		obj.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		obj.setFilter("bla");
		obj.setLayerId("beans");
		obj.setPaintGeometries(paintGeometries);
		obj.setPaintLabels(paintLabels);
		obj.setPanOrigin(new Coordinate(10, 20));
		obj.setRenderer("SVG");
		obj.setScale(2.5);
		NamedStyleInfo nsi = new NamedStyleInfo();
		nsi.setName("nsi");
		obj.setStyleInfo(nsi);
	}
}
