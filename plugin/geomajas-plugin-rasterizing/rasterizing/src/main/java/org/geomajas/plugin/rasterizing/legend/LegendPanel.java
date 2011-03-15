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
import java.awt.BorderLayout;
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
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.layer.LayerType;

/**
 * Swing panel for a simple legend.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendPanel extends JPanel {

	private static final long serialVersionUID = 100;

	private static final int MAX_SIZE = 10000;

	public LegendPanel(String title) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder(title));
	}

	public void addLayer(ClientLayerInfo layer) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		FeatureStyleInfo defaultStyle = new FeatureStyleInfo();
		defaultStyle.applyDefaults();
		VectorStyleIcon icon = new VectorStyleIcon(LayerType.POLYGON, defaultStyle, 15, 15);
		icon.setBounds(5, 5, 15, 15);
		JPanel iconPanel = new JPanel();
		iconPanel.setLayout(null);
		iconPanel.setMinimumSize(new Dimension(25, 25));
		iconPanel.setPreferredSize(new Dimension(25, 25));
		iconPanel.setMaximumSize(new Dimension(25, 25));
		iconPanel.add(icon, BorderLayout.CENTER);
		panel.add(iconPanel);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel itemText = new JLabel(layer.getLabel());
		panel.add(itemText);
		panel.setAlignmentX(LEFT_ALIGNMENT);
		add(panel);
	}

	public void addLayer(ClientRasterLayerInfo layer) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		ImageIcon icon = createImageIcon("/org/geomajas/plugin/rasterizing/layer-raster.png");
		panel.add(new JLabel(icon));
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel itemText = new JLabel(layer.getLabel());
		panel.add(itemText);
		panel.setAlignmentX(LEFT_ALIGNMENT);
		add(panel);
	}

	public void addLayer(ClientVectorLayerInfo vectorLayer, FeatureStyleInfo style) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		VectorStyleIcon icon = new VectorStyleIcon(vectorLayer.getLayerType(), style, 15, 15);
		icon.setBounds(5, 5, 15, 15);
		JPanel iconPanel = new JPanel();
		iconPanel.setLayout(null);
		iconPanel.setMinimumSize(new Dimension(25, 25));
		iconPanel.setPreferredSize(new Dimension(25, 25));
		iconPanel.setMaximumSize(new Dimension(25, 25));
		iconPanel.add(icon, BorderLayout.CENTER);
		panel.add(iconPanel);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel itemText = new JLabel(style.getName());
		itemText.setAlignmentX(LEFT_ALIGNMENT);
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
	 */
	public static class VectorStyleIcon extends JComponent {

		private LayerType layerType;

		private int width;

		private int height;

		private FeatureStyleInfo styleInfo;

		public VectorStyleIcon(LayerType layerType, FeatureStyleInfo styleInfo, int width, int height) {
			this.layerType = layerType;
			this.styleInfo = styleInfo;
			this.width = width;
			this.height = height;
			setSize(width, height);
		}

		protected void paintComponent(Graphics g) {
			Color fillColor = Color.white;
			Color strokeColor = Color.black;
			BasicStroke dashStroke = null;
			Graphics2D graphics = (Graphics2D) g.create();
			if (styleInfo != null) {
				fillColor = getColor(styleInfo.getFillColor(), styleInfo.getFillOpacity(), Color.white);
				strokeColor = getColor(styleInfo.getStrokeColor(), styleInfo.getStrokeOpacity(), Color.black);
				dashStroke = getDashStroke(styleInfo.getDashArray());
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
					graphics.setColor(strokeColor);
					if (dashStroke != null) {
						graphics.setStroke(dashStroke);
					}
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
			return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (opacity * 255));
		}

		private void drawRelativePath(Graphics2D graphics, float[] x, float[] y) {
			Path2D.Double path = new Path2D.Double();
			path.moveTo(x[0] * (width - 1), y[0] * (height - 1));
			for (int i = 1; i < x.length; i++) {
				path.lineTo(x[i] * (width - 1), y[i] * (height - 1));
			}
			graphics.draw(path);
		}

		private BasicStroke getDashStroke(String dashArrayString) {
			if (dashArrayString == null || "".equals(dashArrayString.trim()) || "none".equals(dashArrayString)) {
				return null;
			}

			try {
				String[] res = dashArrayString.split(",");
				float[] dasharr = new float[res.length];
				for (int i = 0; i < res.length; i++) {
					dasharr[i] = Float.parseFloat(res[i]);
				}
				return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dasharr, 0.0f);
			} catch (Exception e) {
				return null;
			}
		}

	}

}
