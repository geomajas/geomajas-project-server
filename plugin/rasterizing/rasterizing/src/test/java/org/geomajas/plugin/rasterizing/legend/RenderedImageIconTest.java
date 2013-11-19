package org.geomajas.plugin.rasterizing.legend;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

public class RenderedImageIconTest {

	@Test
	public void testPaint() throws IOException {
		BufferedImage img = ImageIO.read(getClass().getResourceAsStream(
				"/org/geomajas/plugin/rasterizing/images/testIcon.png"));
		RenderedImageIcon icon = new RenderedImageIcon(img, 24, 24);

		BufferedImage copy = new BufferedImage(24, 24, BufferedImage.TYPE_4BYTE_ABGR);
		icon.paintIcon(null, copy.getGraphics(), 0, 0);
		copy.getGraphics().dispose();

		//ImageIO.write(copy, "png", new FileOutputStream("my.png"));
		
		Raster r1 = img.getData();
		Raster r2 = copy.getData();
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				int[] p1 = r1.getPixel(i, j, (int[]) null);
				int[] p2 = r2.getPixel(i, j, (int[]) null);
				Assert.assertArrayEquals(p2, p1);
			}
		}

	}
}
