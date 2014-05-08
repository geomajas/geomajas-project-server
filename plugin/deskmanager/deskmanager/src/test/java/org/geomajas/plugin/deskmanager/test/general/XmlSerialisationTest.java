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
package org.geomajas.plugin.deskmanager.test.general;

import junit.framework.Assert;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType;
import org.junit.Test;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Oliver May
 */
public class XmlSerialisationTest {

	@Test
	public void testXmlSerialisation() {
		ClientLayerInfo cli = new ClientVectorLayerInfo();

		cli.setMinimumScale(new ScaleInfo(1, 100));
		cli.setMaximumScale(new ScaleInfo(1, 342));

		XmlSerialisationType xmlSerialisationType = new XmlSerialisationType();
		ClientLayerInfo cli2 = (ClientLayerInfo) xmlSerialisationType.deepCopy(cli);

		Assert.assertEquals(cli.getMinimumScale().getDenominator(), cli2.getMinimumScale().getDenominator());
		Assert.assertEquals(cli.getMinimumScale().getNumerator(), cli2.getMinimumScale().getNumerator());
		Assert.assertEquals(cli.getMinimumScale().getPixelPerUnit(), cli2.getMinimumScale().getPixelPerUnit());

		Assert.assertEquals(cli.getMaximumScale().getDenominator(), cli2.getMaximumScale().getDenominator());
		Assert.assertEquals(cli.getMaximumScale().getNumerator(), cli2.getMaximumScale().getNumerator());
		Assert.assertEquals(cli.getMaximumScale().getPixelPerUnit(), cli2.getMaximumScale().getPixelPerUnit());
	
}

	@Test
	public void testXmlEncoder2() throws IOException {

		MySerializable mySer = new MySerializable();
		mySer.setTest(342);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);
		encoder.writeObject(mySer);
		encoder.close();
		String result = baos.toString();
		baos.close();
//		System.out.println(result);

		XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(baos.toByteArray()));
		MySerializable mySer1 = (MySerializable) decoder.readObject();

		Assert.assertEquals(mySer1.getTest(), mySer.getTest());
	}


	public static class MySerializable implements Serializable {
		private double test;

		public double getTest() {
			return test;
		}

		public void setTest(double test) {
			this.test = test;
		}

	};


}
