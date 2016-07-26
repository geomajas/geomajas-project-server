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
package org.geomajas.testdata;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Abstract assertion for comparing images in the test source/classpath.
 * Use as follows: 
 * <pre>
 *  new TestPathBinaryStreamAssert("org/geomajas/myimages") {
 *		public void generateActual(OutputStream out) throws Exception {
 *				// override to generate image
 *			}
 *		}.assertEqual("test.png", false);
 * }
 * </pre>
 * 
 * @author Jan De Moerloose
 */
public abstract class TestPathBinaryStreamAssert extends BinaryStreamAssert {

	private String classPath;

	/**
	 * Create an assert for the specified class path directory.
	 * 
	 * @param classPath
	 *            the classpath directory (no preceding slash needed)
	 */
	public TestPathBinaryStreamAssert(String classPath) {
		this.classPath = classPath;
	}

	public Resource getExpected(String resourceName, boolean rewrite) {
		if (rewrite) {
			return new FileSystemResource("src/test/resources/" + classPath + "/" + resourceName);
		} else {
			return new ClassPathResource(classPath + "/" + resourceName);
		}
	}

}