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

package org.geomajas.layer.geotools;

import org.apache.commons.collections.functors.ExceptionClosure;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base for GeoTools tests.
 * 
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/geotools/dataStoreCannotStart.xml"})
@Transactional(rollbackFor = { java.lang.Throwable.class })
public class DataStoreStartWfsTest {

	@Autowired
	private GeoToolsLayer layer;

	@Test
	public void testRead() throws Exception {
		try {
		SimpleFeature f = (SimpleFeature) layer.read("whatever"); // there is nothing there, exception should come now
		} catch (GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.FEATURE_MODEL_PROBLEM, ge.getExceptionCode());
		}
	}

}
