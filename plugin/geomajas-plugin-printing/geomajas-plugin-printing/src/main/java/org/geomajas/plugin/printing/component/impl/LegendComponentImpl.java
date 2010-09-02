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
package org.geomajas.plugin.printing.component.impl;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.geomajas.plugin.printing.parser.FontConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Inclusion of legend in printed document.
 *
 * @author Jan De Moerloose
 */
@Component("LegendComponentPrototype")
@Scope(value = "prototype")
public class LegendComponentImpl extends PrintComponentImpl<LegendComponentInfo> implements LegendComponent {

	/**
	 * Application id
	 */
	private String applicationId;
	
	/**
	 * Map id
	 */
	private String mapId;

	/**
	 * The font for the text
	 */
	@XStreamConverter(FontConverter.class)
	private Font font = new Font("Dialog", Font.PLAIN, 10);

	/**
	 * Heading text
	 */
	private String title = "Legend";

	@Autowired
	@XStreamOmitField
	private PrintDtoConverterService converterService;

	public LegendComponentImpl() {
		this("Legend");
	}

	public LegendComponentImpl(String title) {
		this.title = title;
		setConstraint(new LayoutConstraint(LayoutConstraint.RIGHT, LayoutConstraint.BOTTOM,
				LayoutConstraint.FLOW_Y, 0, 0, 20, 20));
		LabelComponentImpl titleLabel = new LabelComponentImpl();
		titleLabel.getConstraint().setAlignmentX(LayoutConstraint.CENTER);
		titleLabel.getConstraint().setMarginY(5);
		titleLabel.setTextOnly(true);
		titleLabel.setText(getTitle());
		titleLabel.setTag(PrintTemplate.TITLE);
		titleLabel.setFont(font);
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
	

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.LegendComponent#getFont()
	 */
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.LegendComponent#getTitle()
	 */
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

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.LegendComponent#getMapId()
	 */
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


	// /**
	// * A legend manages its own children at rendering time, so they shouldn't
	// be
	// * serialized. This magic callback method does the trick !
	// */
	// public void beforeMarshal(Marshaller m) {
	// getChildren().clear();
	// }

	public void clearItems() {
		List<LegendItemComponentImpl> items = new ArrayList<LegendItemComponentImpl>();
		for (PrintComponent child : children) {
			if (child instanceof LegendItemComponentImpl) {
				items.add((LegendItemComponentImpl) child);
			}
		}
		for (LegendItemComponentImpl item : items) {
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
			LegendItemComponentImpl item = new LegendItemComponentImpl(styleDefinition, text, info.getLayerType(),
					getFont());
			addComponent(item);
		}
	}

	public void addRasterLayer(ClientRasterLayerInfo info) {
		LegendItemComponentImpl item = new LegendItemComponentImpl(null, info.getLabel(), info.getLayerType(),
				getFont());
		addComponent(item);
	}
	
	public void fromDto(LegendComponentInfo legendInfo) {
		super.fromDto(legendInfo);
		setApplicationId(legendInfo.getApplicationId());
		setMapId(legendInfo.getMapId());
		setFont(converterService.toInternal(legendInfo.getFont()));
		setTitle(legendInfo.getTitle());
	}


}
