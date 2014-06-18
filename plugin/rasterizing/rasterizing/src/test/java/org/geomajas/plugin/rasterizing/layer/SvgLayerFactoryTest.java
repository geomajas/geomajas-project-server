package org.geomajas.plugin.rasterizing.layer;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml" })
public class SvgLayerFactoryTest {

	@Autowired
	private SvgLayerFactory layerFactory;

	private String svgExample = "<svg>\n"
			+ "  <path d=\"M10 10 L90 10 L90 90 L10 90 Z\" style=\"fill:lime;stroke:purple;stroke-width:3px\"/>\n"
			+ "</svg>";

	private String expectedHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"";
	private String keyValueSeparator = "=\"";
	private String valueFishedMarker = "\"";

	/**
	 * Tests whether {@link SvgLayerFactory#addAttributesToSvg(String, int, int, int, int)} adds correct headers
	 * and attributes to the svg xml element.
	 *
	 * @throws Exception
	 */
	@Test
	public void addAttributesToSvgTest() throws Exception {
		int width = 100;
		int height = 120;
		int xDiff = 50;
		int yDiff = 40;
		String result = layerFactory.addAttributesToSvg(svgExample, width, height, xDiff, yDiff);
		Assert.assertTrue(result.startsWith(expectedHeader));
		// remove header
		result = result.substring(expectedHeader.length());
		String svgAttributeString = result.substring(0, result.indexOf(">")).trim();
		Map<String, String> attributeMap = new HashMap<String, String>();
		while (svgAttributeString.contains(keyValueSeparator)) {
			int i = svgAttributeString.indexOf(keyValueSeparator);
			String key = svgAttributeString.substring(0, i);
			svgAttributeString = svgAttributeString.substring(i + keyValueSeparator.length());
			int j = svgAttributeString.indexOf(valueFishedMarker);
			String value = svgAttributeString.substring(0, j);
			svgAttributeString = svgAttributeString.substring(j + valueFishedMarker.length()).trim();
			attributeMap.put(key, value);
		}
		Assert.assertTrue(attributeMap.containsKey("width"));
		Assert.assertEquals(width, Integer.parseInt(attributeMap.get("width")));
		Assert.assertTrue(attributeMap.containsKey("height"));
		Assert.assertEquals(height, Integer.parseInt(attributeMap.get("height")));
		Assert.assertTrue(attributeMap.containsKey("viewBox"));
		String[] viewBoxElements = attributeMap.get("viewBox").split(" ");
		Assert.assertTrue(viewBoxElements.length == 4);
		Assert.assertEquals(-xDiff/2, Integer.parseInt(viewBoxElements[0]));
		Assert.assertEquals(-yDiff/2, Integer.parseInt(viewBoxElements[1]));
		Assert.assertEquals(width, Integer.parseInt(viewBoxElements[2]));
		Assert.assertEquals(height, Integer.parseInt(viewBoxElements[3]));
	}
}
