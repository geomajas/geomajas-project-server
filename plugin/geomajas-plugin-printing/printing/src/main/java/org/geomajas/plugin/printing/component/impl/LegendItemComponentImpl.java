/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.printing.component.impl;

import java.awt.Font;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendItemComponentInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Legend item component for printed document.
 * 
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class LegendItemComponentImpl extends AbstractPrintComponent<LegendItemComponentInfo> {

	public LegendItemComponentImpl() {
		setConstraint(new LayoutConstraint(LayoutConstraint.LEFT, LayoutConstraint.BOTTOM, LayoutConstraint.FLOW_X, 0,
				0, 5, 5));
	}

	public LegendItemComponentImpl(FeatureStyleInfo def, String label, LayerType layerType, Font font) {
		setConstraint(new LayoutConstraint(LayoutConstraint.LEFT, LayoutConstraint.BOTTOM, LayoutConstraint.FLOW_X, 0,
				0, 5, 5));
		LegendIconComponentImpl icon = new LegendIconComponentImpl();
		icon.setLabel(label);
		icon.setStyleInfo(def);
		icon.setLayerType(layerType);
		this.addComponent(icon);
		this.addComponent(new LabelComponentImpl(font, "black", label));
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
		@SuppressWarnings("deprecation")
		PrintComponent<?> ancestor = getParent();

		while (null != ancestor && !(ancestor instanceof LegendComponent)) {
			ancestor = ancestor.getParent();
		}
		if (null != ancestor && ancestor instanceof LegendComponent) {
			return (LegendComponent) ancestor;
		} else {
			return null;
		}
	}
}