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

package org.geomajas.layer.osm;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test for {@link RoundRobinUrlSelectionStrategy}.
 *
 * @author Joachim Van der Auwera
 */
public class RoundRobinUrlSelectionStrategyTest {

	@Test
	public void testStrategy() throws Exception {
		RoundRobinUrlSelectionStrategy strategy = new RoundRobinUrlSelectionStrategy();
		ArrayList<String> urls = new ArrayList<String>();
		urls.add("a");
		urls.add("b");
		urls.add("c");
		strategy.setUrls(urls);
		Assert.assertEquals("a", strategy.next());
		Assert.assertEquals("b", strategy.next());
		Assert.assertEquals("c", strategy.next());
		Assert.assertEquals("a", strategy.next());
	}
}
