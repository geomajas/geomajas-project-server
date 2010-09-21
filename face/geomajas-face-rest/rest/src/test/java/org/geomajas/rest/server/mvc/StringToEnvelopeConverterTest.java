package org.geomajas.rest.server.mvc;

import org.junit.Assert;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;

public class StringToEnvelopeConverterTest {
	@Test
	public void testConvertWithoutWhitespace() {
		StringToEnvelopeConverter converter= new StringToEnvelopeConverter();
		Envelope result = converter.convert("1.1,2.2,3.3333,4");
		Assert.assertEquals(new Envelope(1.1,2.2,3.3333,4), result);
	}
	
	@Test
	public void testConvertWithWhitespace() {
		StringToEnvelopeConverter converter= new StringToEnvelopeConverter();
		Envelope result = converter.convert(" \n  1.1,     2.2\t,\n3.3333  ,4 \n \t ");
		Assert.assertEquals(new Envelope(1.1,2.2,3.3333,4), result);
	}
}
