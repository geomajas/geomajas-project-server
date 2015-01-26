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

package org.geomajas.configuration.client;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests for {@link ClientToolInfo}.
 *
 * @author Emiel Ackermann
 */
public class ClientToolInfoTest {

	@Test
	public void testGetId1() {
		ClientToolInfo info = new ClientToolInfo();
		String toolId = "zoomIn";
		info.setToolId(toolId);
		Assert.assertTrue(info.getToolId().equals(toolId));
	}

	@Test
	public void testGetId2() {
		ClientToolInfo info = new ClientToolInfo();
		String id = "firstZoomIn";
		info.setId(id);
		Assert.assertTrue(info.getToolId().equals(id));
	}
	
	@Test
	public void testGetId3() {
		ClientToolInfo info = new ClientToolInfo();
		String id = "firstZoomIn";
		String toolId = "zoomIn";
		info.setId(id);
		info.setToolId(toolId);
		Assert.assertTrue(info.getToolId().equals(toolId));
	}
}
