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

package org.geomajas.internal.rendering;

import junit.framework.Assert;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.rendering.StyleFilter;
import org.junit.Test;
import org.opengis.filter.Filter;

/**
 * Tests the default style filter.
 * 
 * @author Jan De Moerloose
 *
 */
public class StyleFilterTest {

	@Test
	public void testConstructor() {
		StyleFilter styleFilter = new StyleFilterImpl();
		Assert.assertEquals(Filter.INCLUDE, styleFilter.getFilter());
		Assert.assertNotNull(styleFilter.getStyleDefinition());
		Assert.assertEquals(FeatureStyleInfo.DEFAULT_STYLE_INDEX, styleFilter.getStyleDefinition().getIndex());
	}

}
