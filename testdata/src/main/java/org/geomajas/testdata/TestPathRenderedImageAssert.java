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
package org.geomajas.testdata;

import java.awt.image.RenderedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.springframework.util.StringUtils;

/**
 * Assertion for comparing {@link RenderedImage} images in the test source/classpath. Use as follows:
 * 
 * <pre>
 * new TestPathRenderedImageAssert(&quot;org/geomajas/myimages&quot;).
 *    assertEqual(&quot;test.png&quot;, myRenderedImage, 0.05, false);
 * </pre>
 * 
 * @author Jan De Moerloose
 */
public class TestPathRenderedImageAssert {

	private String classPath;

	public TestPathRenderedImageAssert(String classPath) {
		this.classPath = classPath;
	}

	public void assertEquals(final String fileName, final RenderedImage image, double percentageDelta, boolean rewrite)
			throws Exception {
		TestPathBinaryStreamAssert t = new TestPathBinaryStreamAssert(classPath) {

			@Override
			public void generateActual(OutputStream out) throws Exception {
				ImageIO.write(image, StringUtils.getFilenameExtension(fileName), out);
			}
		};
		t.assertEqualImage(fileName, rewrite, percentageDelta);
	}

}
