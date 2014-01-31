/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.legend;

import org.geomajas.service.legend.LegendGraphicMetadata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.RenderedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Builder class for legend.
 * 
 * @author Jan De Moerloose
 */
public class LegendBuilder {

	private static final int MEMBER_MARGIN = 5;

	private static final int ICON_PADDING = 2;

	private static final int MAX_SIZE = 10000;

	private final JPanel legendPanel;

	private final TitledBorder border;

	private Dimension dimension;

	/**
	 * No -arguments constructor.
	 */
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

	public void addLayer(String title, Font font, RenderedImage image) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		RenderedImageIcon icon = new RenderedImageIcon(image,
				LegendGraphicMetadata.DEFAULT_WIDTH, LegendGraphicMetadata.DEFAULT_HEIGHT);
		JLabel label = new JLabel(icon);
		label.setBorder(new EmptyBorder(ICON_PADDING, ICON_PADDING, ICON_PADDING, ICON_PADDING));
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(MEMBER_MARGIN, 0)));
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

}
