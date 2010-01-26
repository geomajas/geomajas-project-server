/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.dojo.server.json;

import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.UnmarshallException;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.BigInteger;

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
