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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.junit.Test;

/**
 * Tests for ScaleInfo.
 * 
 * @author Joachim Van der Auwera
 */
public class ScaleInfoTest {

	private static final double DELTA = 1e-50;

	@Test
	public void testNoZeroPixelsPerUnit() {
		ScaleInfo scaleInfo;
		scaleInfo = new ScaleInfo();
		Assert.assertTrue(scaleInfo.getPixelPerUnit() > 0);
		scaleInfo = new ScaleInfo(0);
		Assert.assertTrue(scaleInfo.getPixelPerUnit() > 0);
	}

	@Test
	public void testPixelsPerUnitRange() {
		ScaleInfo scaleInfo;
		scaleInfo = new ScaleInfo(-100);
		Assert.assertEquals(ScaleInfo.MINIMUM_PIXEL_PER_UNIT, scaleInfo.getPixelPerUnit(), DELTA);
		scaleInfo = new ScaleInfo(1e100);
		Assert.assertEquals(ScaleInfo.MAXIMUM_PIXEL_PER_UNIT, scaleInfo.getPixelPerUnit(), DELTA);
	}

	@Test
	public void testCopyConstructor() throws IOException, ClassNotFoundException {
		ScaleInfo info = new ScaleInfo(1, 100);
		Assert.assertEquals(37.7952, info.getPixelPerUnit(), 0.0001);
		ScaleInfo copy = new ScaleInfo(info);
		Assert.assertEquals(37.7952, copy.getPixelPerUnit(), 0.0001);
		Assert.assertEquals(100, copy.getDenominator(), 0.0001);
		Assert.assertEquals(1, copy.getNumerator(), 0.0001);
	}
	
	@Test
	public void testSerializeAndBack() throws IOException, ClassNotFoundException {
		ScaleInfo info = new ScaleInfo(1, 100);
		Assert.assertEquals(37.7952, info.getPixelPerUnit(), 0.0001);
		ScaleInfo copy = new ScaleInfo(info);
		Assert.assertEquals(37.7952, copy.getPixelPerUnit(), 0.0001);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JBossObjectOutputStream out = new JBossObjectOutputStream(baos);
		out.writeObject(info);
		out.flush();
		out.close();
		JBossObjectInputStream in = new JBossObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
		ScaleInfo clone = (ScaleInfo) in.readObject();
		Assert.assertNotSame(info, clone);
		Assert.assertEquals(37.7952, clone.getPixelPerUnit(), 0.0001);

	}
}
