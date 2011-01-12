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

package org.geomajas.internal.filter;

import org.geomajas.internal.filter.FeatureModelPropertyAccessorFactory.FeatureModelPropertyAccessor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link FeatureModelPropertyAccessorFactory}.
 * 
 * @author Pieter De Graef
 */
public class PropertyAccessorTest {

	@Test
	public void testPattern() {
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa").matches());

		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa/bb").matches());
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa.bb").matches());

		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa/bb/cc").matches());
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa.bb.cc").matches());
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa/bb.cc").matches());

		Assert.assertFalse(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa:bb").matches());
		Assert.assertFalse(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa\bb").matches());
		Assert.assertFalse(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa\\bb").matches());
		Assert.assertFalse(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa+bb").matches());
	}
}
