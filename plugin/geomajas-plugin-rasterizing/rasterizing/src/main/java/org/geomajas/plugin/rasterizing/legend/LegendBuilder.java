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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geotools.styling.Rule;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Builder class for legend.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendBuilder {

	private static final long serialVersionUID = 100;

	private static final int MAX_SIZE = 10000;

	private JPanel legendPanel;

	private TitledBorder border;

	private Dimension dimension;

	public LegendBuilder() {
		legendPanel = new JPanel();
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
		border = BorderFactory.createTitledBorder("Legend");
		legendPanel.setBorder(border);
	}

	public JComponent buildComponent() {
		pack();
		return legendPanel;
	}

	public void setSize(int width, int height) {
		dimension = new Dimension(width, height);
	}

	public void setTitle(String title, Font font) {
		border.setTitle(title);
		border.setTitleFont(font);
	}

	public void addVectorLayer(SimpleFeatureType schema, String title, Rule rule, Font font) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		FeatureStyleInfo defaultStyle = new FeatureStyleInfo();
		defaultStyle.applyDefaults();
		int width = 16;
		int height = 16;
		int margin = 5;
		RuleIcon icon = new RuleIcon(schema, rule, width, height);
		icon.setBounds(margin, margin, margin + width, margin + height);
		JPanel iconPanel = new JPanel();
		iconPanel.setLayout(null);
		iconPanel.setMinimumSize(new Dimension(2 * margin + width, 2 * margin + height));
		iconPanel.setPreferredSize(new Dimension(2 * margin + width, 2 * margin + height));
		iconPanel.setMaximumSize(new Dimension(2 * margin + width, 2 * margin + height));
		iconPanel.add(icon, BorderLayout.CENTER);
		panel.add(iconPanel);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel itemText = new JLabel(title);
		itemText.setFont(font);
		panel.add(itemText);
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		legendPanel.add(panel);
	}

	public void addRasterLayer(String title, Font font) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		ImageIcon icon = createImageIcon("/org/geomajas/plugin/rasterizing/layer-raster.png");
		panel.add(new JLabel(icon));
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel itemText = new JLabel(title);
		itemText.setFont(font);
		panel.add(itemText);
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		legendPanel.add(panel);
	}

	private void pack() {
		JPanel panel = new JPanel();
		if (dimension != null) {
			panel.setSize(dimension);
			panel.setLayout(new BorderLayout());
			panel.add(legendPanel, BorderLayout.CENTER);
		} else {
			panel.setSize(MAX_SIZE, MAX_SIZE);
			panel.setLayout(new FlowLayout());
			panel.add(legendPanel);
		}
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

}
