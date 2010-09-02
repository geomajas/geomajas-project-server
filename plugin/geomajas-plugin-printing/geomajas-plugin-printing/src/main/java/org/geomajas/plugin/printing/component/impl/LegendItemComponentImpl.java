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

import java.awt.Color;
import java.awt.Font;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendItemComponentInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Legend item component for printed document.
 *
 * @author Jan De Moerloose
 */
@Component("LegendItemComponentPrototype")
@Scope(value = "prototype")
public class LegendItemComponentImpl extends PrintComponentImpl<LegendItemComponentInfo> {

	public LegendItemComponentImpl() {
		setConstraint(new LayoutConstraint(LayoutConstraint.LEFT, LayoutConstraint.BOTTOM,
				LayoutConstraint.FLOW_X, 0, 0, 5, 5));
	}

	public LegendItemComponentImpl(FeatureStyleInfo def, String label, LayerType layerType, Font font) {
		setConstraint(new LayoutConstraint(LayoutConstraint.LEFT, LayoutConstraint.BOTTOM,
				LayoutConstraint.FLOW_X, 0, 0, 5, 5));
		LegendIconComponentImpl icon = new LegendIconComponentImpl();
		icon.setLabel(label);
		icon.setStyleInfo(def);
		icon.setLayerType(layerType);
		this.addComponent(icon);
		this.addComponent(new LabelComponentImpl(font, Color.black, label));
	}

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
	}
	
	@Override
	public void calculateSize(PdfContext context) {
		super.calculateSize(context);
	}

	protected LegendComponent getLegend() {
		return (LegendComponent) getParent();
	}
	
}
