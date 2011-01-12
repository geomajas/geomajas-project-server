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
package org.geomajas.dojo.server.json;

import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.UnmarshallException;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test for BigNumberSerializer.
 *
 * @author Pieter De Graef
 */
public class BigNumberSerializerTest extends TestCase {

	public void testBigInteger() throws MarshallException {
		BigNumberSerializer ser = new BigNumberSerializer();
		BigInteger bi = new BigInteger("100");
		Object o = ser.marshall(null, bi);
		assertEquals(new Long(100), o);

		try {
			BigInteger bi2 = (BigInteger) ser.unmarshall(null, BigInteger.class, new Long(100));
			assertEquals(bi, bi2);
		} catch (UnmarshallException e) {
			e.printStackTrace();
		}
	}

	public void testBigDecimal() throws MarshallException {
		BigNumberSerializer ser = new BigNumberSerializer();
		BigDecimal bd = new BigDecimal("100.5");
		Object o = ser.marshall(null, bd);
		assertEquals(new Double(100.5), o);

		try {
			BigDecimal bd2 = (BigDecimal) ser.unmarshall(null, BigDecimal.class, new Double(100.5));
			assertEquals(bd, bd2);
		} catch (UnmarshallException e) {
			e.printStackTrace();
		}
	}
}
