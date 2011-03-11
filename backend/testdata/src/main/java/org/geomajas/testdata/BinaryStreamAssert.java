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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
	 * @param resourceName
	 *            name part of resource
	 * @param baae
	 *            accessor to actual and expected binary data
	 * @param rewrite
	 *            if true, the expected will be rewritten
	 * @throws Exception 
	 */
	public void assertEqual(String resourceName, boolean rewrite) throws Exception {
		assertEqual("resource " + resourceName + " not equal or writable", resourceName, rewrite);
	}

	/**
	 * Assert that the generated stream is binary equal to the specified binary resource.
	 * 
	 * @param message
	 *            message for the world if assert fails
	 * @param resourceName
	 *            name part of resource
	 * @param baae
	 *            accessor to actual and expected binary data
	 * @param rewrite
	 *            if true, the expected will be rewritten
	 * @throws Exception 
	 */
	public void assertEqual(String message, String resourceName, boolean rewrite) throws Exception {
		if (rewrite) {
			File file;
				file = getExpected(resourceName, rewrite).getFile();
				FileOutputStream fos;
				fos = new FileOutputStream(file);
				generateActual(fos);
				fos.flush();
				fos.close();
				Assert.fail("could not write expected");
		} else {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				generateActual(baos);
				baos.flush();
				baos.close();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				InputStream is = getExpected(resourceName, false).getInputStream();
				byte[] buf = new byte[1024];
				int len;
				while ((len = is.read(buf, 0, 1024)) != -1) {
					bos.write(buf, 0, len);
				}
				is.close();
				byte[] expecteds = bos.toByteArray();
				Assert.assertArrayEquals(message, expecteds, baos.toByteArray());
		}
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
	 * @param resourceName the resource name (file name)
	 * @param rewrite true if the resource should be rewritten
	 * @return
	 */
	public abstract Resource getExpected(String resourceName, boolean rewrite);

}
