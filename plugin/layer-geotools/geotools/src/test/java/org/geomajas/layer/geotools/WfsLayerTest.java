/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.geotools;

import java.util.Date;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test for GeoTools layer. Run manually.
 *
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/geotools/wfsLayer.xml"})
@Transactional(rollbackFor = {Throwable.class})
public class WfsLayerTest extends AbstractGeoToolsTest {

	@Autowired
	@Qualifier("wfsLayer")
	private GeoToolsLayer layer;

	@Test
	@Ignore
	public void testRead() throws Exception {
		System.out.println("Start : " + new Date());
		int count;
		for (Iterator<?> it = layer.getElements(Filter.INCLUDE, 0, 2); it.hasNext();) {
			SimpleFeature sf = (SimpleFeature) it.next();
			System.out.println(sf.getID());
		}

		System.out.println("Stop : " + new Date());
	}

}
