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
package org.geomajas.plugin.rasterizing.legend;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Symbolizer;
import org.geotools.util.Range;
import org.opengis.style.GraphicalSymbol;

/**
 * Fixed size Swing component that graphically represents an SLD symbolizer.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SymbolizerIcon extends JComponent {

	private int width;

	private int height;

	private Symbolizer symbolizer;

	private SLDStyleFactory styleFactory = new SLDStyleFactory();

	public SymbolizerIcon(Symbolizer symbolizer, int width, int height) {
		this.symbolizer = symbolizer;
		this.width = width;
		this.height = height;
		setSize(width, height);
	}

	protected void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g.create();
		Style2D style = styleFactory.createStyle(null, symbolizer, new Range<Double>(Double.class, 0.0,
				Double.MAX_VALUE));
		// draw symbol
		if (symbolizer instanceof PointSymbolizer) {
			PointSymbolizer ps = (PointSymbolizer) symbolizer;
			GraphicalSymbol symbol = ps.getGraphic().graphicalSymbols().get(0);
			if (symbol instanceof Mark) {
				MarkStyle2D mark = (MarkStyle2D) style;
				String name = ((Mark) symbol).getWellKnownName().evaluate(null, String.class);
				if ("square".equals(name)) {
					graphics.setPaint(mark.getFill());
					graphics.setComposite(mark.getFillComposite());
					graphics.fillRect(0, 0, width - 1, height - 1);
					graphics.setPaint(mark.getContour());
					graphics.setStroke(mark.getStroke());
					graphics.setComposite(mark.getContourComposite());
					graphics.drawRect(0, 0, width - 1, height - 1);
				} else {
					graphics.setPaint(mark.getFill());
					graphics.setComposite(mark.getFillComposite());
					graphics.fillOval(0, 0, width - 1, height - 1);
					graphics.setPaint(mark.getContour());
					graphics.setStroke(mark.getStroke());
					graphics.setComposite(mark.getContourComposite());
					graphics.drawOval(0, 0, width - 1, height - 1);
				}
			}
		} else if (symbolizer instanceof LineSymbolizer) {
			LineStyle2D line = (LineStyle2D) style;
			graphics.setPaint(line.getContour());
			graphics.setStroke(line.getStroke());
			graphics.setComposite(line.getContourComposite());
			drawRelativePath(graphics, new float[] { 0f, 0.75f, 0.25f, 1f }, new float[] { 0f, 0.25f, 0.75f, 1f });
		} else {
			PolygonStyle2D polygon = (PolygonStyle2D) style;
			graphics.setPaint(polygon.getFill());
			graphics.setComposite(polygon.getFillComposite());
			graphics.fillRect(0, 0, width - 1, height - 1);
			graphics.setPaint(polygon.getContour());
			graphics.setStroke(polygon.getStroke());
			graphics.setComposite(polygon.getContourComposite());
			graphics.drawRect(0, 0, width - 1, height - 1);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(width, height);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(width, height);
	}

	private void drawRelativePath(Graphics2D graphics, float[] x, float[] y) {
		Path2D.Double path = new Path2D.Double();
		path.moveTo(x[0] * (width - 1), y[0] * (height - 1));
		for (int i = 1; i < x.length; i++) {
			path.lineTo(x[i] * (width - 1), y[i] * (height - 1));
		}
		graphics.draw(path);
	}

}
