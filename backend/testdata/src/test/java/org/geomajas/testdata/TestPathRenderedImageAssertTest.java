package org.geomajas.testdata;

import java.awt.image.RenderedImage;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

public class TestPathRenderedImageAssertTest extends TestPathRenderedImageAssert {

	public TestPathRenderedImageAssertTest() {
		super("org/geomajas/testdata");
	}

	@Test
	public void testAssertEqual() throws Exception {
		RenderedImage image = ImageIO.read(getClass().getResourceAsStream("test.png"));
		//ImageIO.write(image, "png", new FileOutputStream("test1.png"));
		assertEquals("test.png", image, 0.0001, false);
	}

	@Test
	public void testAssertEqualAssertionError() {
		try {
			RenderedImage image = ImageIO.read(getClass().getResourceAsStream("test.png"));
			assertEquals("wrong.png", image, 0.0001, false);
			Assert.fail("expected assertion error for non-equal streams");
		} catch (AssertionError e) {
		} catch (Exception e) {
			Assert.fail("expected assertion error for non-equal streams");
		}
	}

	@Test
	public void testAssertEqualFailure() {
		try {
			RenderedImage image = ImageIO.read(getClass().getResourceAsStream("test.png"));
			assertEquals("missing.png", image, 0.0001, false);
			Assert.fail("expected failure for non-existing file");
		} catch (AssertionError e) {
			Assert.fail("expected failure for non-existing file");
		} catch (Exception e) {
		}
	}

}
