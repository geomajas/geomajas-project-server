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

import com.lowagie.text.Rectangle;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.extension.printing.PdfContext;
import org.geomajas.layer.LayerType;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.Color;
import java.awt.Font;

/**
 * ???
 *
 * @author check subversion
 */
@XmlRootElement
public class LegendItemComponent extends BaseComponent {

	public LegendItemComponent() {
		setConstraint(new LayoutConstraint(LayoutConstraint.LEFT, LayoutConstraint.BOTTOM,
				LayoutConstraint.FLOW_X, 0, 0, 5, 5));
	}

	public LegendItemComponent(FeatureStyleInfo def, String label, LayerType layerType, Font font) {
		setConstraint(new LayoutConstraint(LayoutConstraint.LEFT, LayoutConstraint.BOTTOM,
				LayoutConstraint.FLOW_X, 0, 0, 5, 5));
		LegendIconComponent icon = new LegendIconComponent();
		icon.setLabel(label);
		icon.setStyleInfo(def);
		icon.setLayerType(layerType);
		this.addComponent(icon);
		this.addComponent(new LabelComponent(font, Color.black, label));
	}

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public void calculateSize(PdfContext context) {
		super.calculateSize(context);
		float height = context.getTextSize("A", getLegend().getFont()).getHeight();
		height *= 1.2;
		setBounds(new Rectangle(getBounds().getWidth(), height));
	}

	protected LegendComponent getLegend() {
		return (LegendComponent) getParent();
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
}
