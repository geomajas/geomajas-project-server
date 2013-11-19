package org.geomajas.testdata;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

public class TestPathBinaryStreamAssertImageTest extends TestPathBinaryStreamAssert {

	public TestPathBinaryStreamAssertImageTest() {
		super("org/geomajas/testdata");
	}

	@Test
	public void testAssertEqual() throws Exception {
		assertEqualImage("blank.png", false, 0);
		assertEqualImage("1pix.png", false, 0.01);
	}

	@Test
	public void testAssertEqualAssertionError() {
		try {
			assertEqualImage("1pix.png", false, 0);
			Assert.fail("expected assertion error for non-equal streams");
		} catch (AssertionError e) {
		} catch (Exception e) {
			Assert.fail("expected assertion error for non-equal streams");
		}
	}

	@Test
	public void testAssertEqualFailure() {
		try {
			assertEqual("missing.png", false);
			Assert.fail("expected failure for non-existing file");
		} catch (AssertionError e) {
			Assert.fail("expected failure for non-existing file");
		} catch (Exception e) {
		}
	}

	@Override
	public void generateActual(OutputStream out) throws Exception {
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setRGB(x, y, 0xffffffff);
			}
		}
		ImageIO.write(image, "PNG", out);
	}
}
