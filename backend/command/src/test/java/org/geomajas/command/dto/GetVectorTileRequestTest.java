/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
		obj.setCode(new TileCode(1,0,0));
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
