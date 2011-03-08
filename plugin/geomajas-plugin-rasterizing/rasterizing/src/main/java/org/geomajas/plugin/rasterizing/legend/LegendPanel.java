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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.rasterizing.dto.LayerMetadata;
import org.geomajas.plugin.rasterizing.dto.RasterLayerMetadata;
import org.geomajas.plugin.rasterizing.dto.VectorLayerMetadata;

/**
 * Swing panel for a simple legend.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendPanel extends JPanel {

	private static int MAX_SIZE = 10000;

	public LegendPanel(String title) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder(title));
	}

	public void addLayer(LayerMetadata layer) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		FeatureStyleInfo defaultStyle = new FeatureStyleInfo();
		defaultStyle.applyDefaults();
		panel.add(new VectorStyleIcon(LayerType.POLYGON, defaultStyle));
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel itemText = new JLabel(layer.getLayerId());
		panel.add(itemText);
		panel.setAlignmentX(LEFT_ALIGNMENT);
		add(panel);
	}

	public void addLayer(RasterLayerMetadata layer) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		ImageIcon icon = createImageIcon("/org/geomajas/plugin/rasterizing/layer-raster.png");
		panel.add(new JLabel(icon));
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel itemText = new JLabel(layer.getLayerId());
		panel.add(itemText);
		panel.setAlignmentX(LEFT_ALIGNMENT);
		add(panel);
	}

	public void addLayer(VectorLayerMetadata vectorLayer, FeatureStyleInfo style) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(new VectorStyleIcon(vectorLayer.getLayertype(), style));
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel itemText = new JLabel(style.getName());
		panel.add(itemText);
		panel.setAlignmentX(LEFT_ALIGNMENT);
		add(panel);
	}

	public void addItem(JPanel panel) {
		panel.setAlignmentX(LEFT_ALIGNMENT);
		add(panel);
	}

	public void pack() {
		JPanel panel = new JPanel();
		panel.setSize(MAX_SIZE, MAX_SIZE);
		panel.setLayout(new FlowLayout());
		panel.add(this);
		panel.addNotify();
		panel.validate();
	}

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			return null;
		}
	}

	/**
	 * Creates a suitable icon for vector layers.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class VectorStyleIcon extends JComponent {

		private LayerType layerType;

		private int width = 15;

		private int height = 15;

		private FeatureStyleInfo styleInfo;

		public VectorStyleIcon(LayerType layerType, FeatureStyleInfo styleInfo) {
			this.layerType = layerType;
			this.styleInfo = styleInfo;
			setSize(width, height);
		}

		protected void paintComponent(Graphics g) {
			Color fillColor = Color.white;
			Color strokeColor = Color.black;
			float[] dashArray = null;
			Graphics2D graphics = (Graphics2D) g.create();
			if (styleInfo != null) {
				fillColor = getColor(styleInfo.getFillColor(), styleInfo.getFillOpacity(), Color.white);
				strokeColor = getColor(styleInfo.getStrokeColor(), styleInfo.getStrokeOpacity(), Color.black);
				dashArray = getDashArray(styleInfo.getDashArray());
			}
			// draw symbol
			switch (layerType) {
				case POINT:
				case MULTIPOINT:
					SymbolInfo symbol = styleInfo.getSymbol();
					if (symbol.getRect() != null) {
						graphics.setColor(fillColor);
						graphics.fillRect(0, 0, width - 1, height - 1);
						graphics.setColor(strokeColor);
						graphics.drawRect(0, 0, width - 1, height - 1);
					} else {
						graphics.setColor(fillColor);
						graphics.fillOval(0, 0, width - 1, height - 1);
						graphics.setColor(strokeColor);
						graphics.drawOval(0, 0, width - 1, height - 1);
					}
					break;
				case LINESTRING:
				case MULTILINESTRING:
					BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
							dashArray, 0.0f);
					graphics.setColor(strokeColor);
					graphics.setStroke(dashed);
					drawRelativePath(graphics, new float[] { 0f, 0.75f, 0.25f, 1f },
							new float[] { 0f, 0.25f, 0.75f, 1f });
					break;
				case POLYGON:
				case MULTIPOLYGON:
					graphics.setColor(fillColor);
					graphics.fillRect(0, 0, width - 1, height - 1);
					graphics.setColor(strokeColor);
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

		public Color getColor(String css, float opacity, Color defaultColor) {
			Color color;
			if (null == css) {
				color = defaultColor;
			} else {
				color = Color.decode(css);
			}
			Color opaque = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (opacity * 255));
			return opaque;
		}

		private void drawRelativePath(Graphics2D graphics, float[] x, float[] y) {
			Path2D.Double path = new Path2D.Double();
			path.moveTo(x[0] * (width - 1), y[0] * (height - 1));
			for (int i = 1; i < x.length; i++) {
				path.lineTo(x[i] * (width - 1), y[i] * (height - 1));
			}
			graphics.draw(path);
		}

		private float[] getDashArray(String dashArrayString) {
			if (dashArrayString == null || "".equals(dashArrayString.trim()) || "none".equals(dashArrayString)) {
				return new float[0];
			}

			try {
				String[] res = dashArrayString.split(",");
				float[] dasharr = new float[res.length];
				for (int i = 0; i < res.length; i++) {
					dasharr[i] = Float.parseFloat(res[i]);
				}
				return dasharr;
			} catch (Exception e) {
				return new float[0];
			}
		}

	}

}
