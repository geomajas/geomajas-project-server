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
package org.geomajas.testdata;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.springframework.core.io.Resource;

/**
 * Abstract class to check binary stream equality. Implementors should provide access to actual and expected data by
 * implementing the methods generateActual() and getExpected().
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class BinaryStreamAssert {

	/**
	 * Assert that the generated stream is binary equal to the specified binary resource.
	 * 
	 * @param message
	 *            message for the world if assert fails
	 * @param resourceName
	 *            name part of resource
	 * @param rewrite
	 *            if true, the expected will be rewritten
	 * @throws Exception
	 */
	public void assertEqual(String message, String resourceName, boolean rewrite) throws Exception {
		if (rewrite) {
			writeToFile(resourceName);
		} else {
			Assert.assertArrayEquals(message, getExpectedBytes(resourceName), getActualBytes());
		}
	}

	/**
	 * Assert that the generated stream is binary equal to the specified binary resource.
	 * 
	 * @param resourceName
	 *            name part of resource
	 * @param rewrite
	 *            if true, the expected will be rewritten
	 * @throws Exception
	 */
	public void assertEqual(String resourceName, boolean rewrite) throws Exception {
		assertEqual("resource " + resourceName + " not equal or writable", resourceName, rewrite);
	}

	/**
	 * Assert that the generated image is binary equal to the specified binary image resource.
	 * 
	 * @param message
	 *            message for the world if assert fails
	 * @param resourceName
	 *            name part of resource
	 * @param rewrite
	 *            if true, the expected will be rewritten
	 * @param relativeDelta
	 *            relative difference between the 2 images (0 <= relativeDelta <= 1)
	 * @throws Exception
	 */
	public void assertEqualImage(String message, String resourceName, boolean rewrite, double relativeDelta)
			throws Exception {
		if (rewrite) {
			writeToFile(resourceName);
		} else {
			InputStream is = getExpected(resourceName, false).getInputStream();
			BufferedImage origImg = ImageIO.read(new ByteArrayInputStream(getActualBytes()));
			BufferedImage expimg = ImageIO.read(is);
			double sum = 0;
			double diff = 0;
			for (int x = 0; x < expimg.getWidth(); x++) {
				for (int y = 0; y < expimg.getHeight(); y++) {
					Color c1 = new Color(expimg.getRGB(x, y), true);
					Color c2 = new Color(origImg.getRGB(x, y), true);
					diff += square(diff(c1, c2));
					sum += square(c1) + square(c2);
				}
			}
			double delta = Math.sqrt(diff) / Math.sqrt(sum);
			Assert.assertTrue(message + ", delta = " + delta, delta <= relativeDelta);
		}
	}

	/**
	 * Assert that the generated stream is binary equal to the specified binary resource.
	 * 
	 * @param resourceName
	 *            name part of resource
	 * @param rewrite
	 *            if true, the expected will be rewritten
	 * @param relativeDelta
	 *            relative difference between the 2 images (0 <= relativeDelta <= 1)
	 * @throws Exception
	 */
	public void assertEqualImage(String resourceName, boolean rewrite, double relativeDelta) throws Exception {
		assertEqualImage("resource " + resourceName + " not equal or writable", resourceName, rewrite, relativeDelta);
	}

	/**
	 * Generates the actual binary stream to the specified outputstream.
	 * 
	 * @param out
	 * @throws Exception
	 */
	public abstract void generateActual(OutputStream out) throws Exception;

	/**
	 * Returns the expected binary stream as a Spring resource. Should be a writable file if rewrite is true.
	 * 
	 * @param resourceName
	 *            the resource name (file name)
	 * @param rewrite
	 *            true if the resource should be rewritten
	 * @return
	 */
	public abstract Resource getExpected(String resourceName, boolean rewrite);

	private void writeToFile(String resourceName) throws IOException, FileNotFoundException, Exception {
		File file;
		file = getExpected(resourceName, true).getFile();
		FileOutputStream fos;
		fos = new FileOutputStream(file);
		generateActual(fos);
		fos.flush();
		fos.close();
	}

	private Color diff(Color c1, Color c2) {
		int r = Math.abs(c1.getRed() - c2.getRed());
		int g = Math.abs(c1.getGreen() - c2.getGreen());
		int b = Math.abs(c1.getBlue() - c2.getBlue());
		int a = Math.abs(c1.getAlpha() - c2.getAlpha());
		return new Color(r, g, b, a);
	}

	private double square(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = c.getAlpha();
		return r * r + g * g + b * b + a * a;
	}

	private byte[] getExpectedBytes(String resourceName) throws Exception, IOException {
		InputStream is = getExpected(resourceName, false).getInputStream();
		byte[] buf = new byte[1024];
		int len;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = is.read(buf, 0, 1024)) != -1) {
			bos.write(buf, 0, len);
		}
		is.close();
		return bos.toByteArray();
	}

	private byte[] getActualBytes() throws Exception, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		generateActual(baos);
		baos.flush();
		baos.close();
		return baos.toByteArray();
	}

}
