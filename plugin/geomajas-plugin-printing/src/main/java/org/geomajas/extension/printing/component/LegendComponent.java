/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.extension.printing.component;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.geomajas.extension.printing.parser.FontAdapter;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.extension.printing.PdfContext;
import org.geomajas.extension.printing.configuration.PrintTemplate;

/**
 * ???
 *
 * @author check subversion
 */
@XmlRootElement
public class LegendComponent extends BaseComponent {

	/**
	 * Application id
	 */
	private String applicationId;
	
	/**
	 * Map id
	 */
	private String mapId;

	public LegendComponent() {
		this("Legend");
	}

	public LegendComponent(String title) {
		this.title = title;
		setConstraint(new LayoutConstraint(LayoutConstraint.RIGHT, LayoutConstraint.BOTTOM,
				LayoutConstraint.FLOW_Y, 0, 0, 20, 20));
		LabelComponent titleLabel = new LabelComponent();
		titleLabel.getConstraint().setAlignmentX(LayoutConstraint.CENTER);
		titleLabel.getConstraint().setMarginY(5);
		titleLabel.setTextOnly(true);
		titleLabel.setText(getTitle());
		titleLabel.setTag(PrintTemplate.TITLE);
		addComponent(titleLabel);
	}

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * The font for the text
	 */
	private Font font = new Font("Dialog", Font.PLAIN, 10);

	/**
	 * Heading text
	 */
	private String title = "Legend";

	@XmlJavaTypeAdapter(FontAdapter.class)
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void calculateSize(PdfContext context) {
		super.calculateSize(context);
	}

	@Override
	public void render(PdfContext context) {
		// border
		context.fillRectangle(getSize());
		super.render(context);
		// border
		context.strokeRectangle(getSize());
	}

	protected MapComponent getMap() {
		return (MapComponent) getParent();
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	public String getApplicationId() {
		return applicationId;
	}

	
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Resets cyclic references like child -> parent relationship.
	 *
	 * @param u
	 * @param parent
	 */
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		setParent((PrintComponent) parent);
	}

	// /**
	// * A legend manages its own children at rendering time, so they shouldn't
	// be
	// * serialized. This magic callback method does the trick !
	// */
	// public void beforeMarshal(Marshaller m) {
	// getChildren().clear();
	// }

	public void clearItems() {
		List<LegendItemComponent> items = new ArrayList<LegendItemComponent>();
		for (PrintComponent child : children) {
			if (child instanceof LegendItemComponent) {
				items.add((LegendItemComponent) child);
			}
		}
		for (LegendItemComponent item : items) {
			removeComponent(item);
		}
	}

	public void addVectorLayer(ClientVectorLayerInfo info) {
		String label = info.getLabel();
		List<FeatureStyleInfo> defs = info.getNamedStyleInfo().getFeatureStyles();
		for (FeatureStyleInfo styleDefinition : defs) {
			String text = "";
			if (defs.size() > 1) {
				text = label + "(" + styleDefinition.getName() + ")";
			} else {
				text = label;
			}
			LegendItemComponent item = new LegendItemComponent(styleDefinition, text, info.getLayerType(), getFont());
			addComponent(item);
		}
	}

	public void addRasterLayer(ClientRasterLayerInfo info) {
		LegendItemComponent item = new LegendItemComponent(null, info.getLabel(), info.getLayerType(),
				getFont());
		addComponent(item);
	}

}
