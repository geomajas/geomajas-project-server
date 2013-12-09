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

package org.geomajas.internal.configuration;

import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.spring.ScaleInfoHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for ScaleInfoEditor.
 * 
 * @author Jan De Moerloose
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/spring/editorContext.xml" })
public class ScaleInfoEditorTest {

	@Autowired
	private ScaleInfoHolder holder;

	ConfigurationDtoPostProcessor cdpp = new ConfigurationDtoPostProcessor();

	@Test
	public void testSetAsText() {
		ScaleInfoEditor editor = new ScaleInfoEditor();
		editor.setAsText("1:2000");
		Object o = editor.getValue();
		Assert.assertTrue(o instanceof ScaleInfo);
		ScaleInfo info = (ScaleInfo)o;
		Assert.assertEquals(1, info.getNumerator(), 0.001);
		Assert.assertEquals(2000, info.getDenominator(), 0.001);
		Assert.assertEquals(1.8890, info.getPixelPerUnit(), 0.001);
		cdpp.completeScale(info, 10);
		Assert.assertEquals(0.005, info.getPixelPerUnit(), 0.00001);
	}

	@Test
	public void testIncontext() {
		ScaleInfo info = holder.getScaleInfo();
		Assert.assertEquals(1, info.getNumerator(), 0.001);
		Assert.assertEquals(2500, info.getDenominator(), 0.001);
		Assert.assertEquals(1.512, info.getPixelPerUnit(), 0.001);
		cdpp.completeScale(info, 100);
		Assert.assertEquals(0.04, info.getPixelPerUnit(), 0.00001);
	}
}
