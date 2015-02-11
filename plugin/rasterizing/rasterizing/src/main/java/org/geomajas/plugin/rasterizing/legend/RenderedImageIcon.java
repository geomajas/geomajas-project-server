/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.legend;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;

import javax.swing.Icon;

/**
 * Fixed size Swing component that graphically represents an SLD rule.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RenderedImageIcon implements Icon {

	private RenderedImage renderedImage;

	private int width;

	private int height;

	public RenderedImageIcon(RenderedImage renderedImage, int width, int height) {
		this.renderedImage = renderedImage;
		this.width = width;
		this.height = height;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (!(g instanceof Graphics2D)) {
			throw new IllegalStateException("paintIcon() only works on Graphics2D.");
		}
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform scale = AffineTransform.getScaleInstance((double) (width) / renderedImage.getWidth(),
				(double) (height) / renderedImage.getHeight());
		AffineTransform translate = AffineTransform.getTranslateInstance(x, y);
		translate.concatenate(scale);
		g2d.drawRenderedImage(renderedImage, translate);
	}

	public int getIconWidth() {
		return width;
	}

	public int getIconHeight() {
		return height;
	}

}
