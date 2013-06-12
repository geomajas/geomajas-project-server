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

import org.geomajas.plugin.printing.component.LabelComponent;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.LegendViaUrlComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendForLayerComponentInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Rectangle;

/**
 * Legend part for a layer whose legend image is specified via a URL.
 * 
 * @author An Buyle
 */
@Component()
@Scope(value = "prototype")
public class LegendForLayerComponentImpl extends AbstractPrintComponent<LegendForLayerComponentInfo> {

	private boolean visible = true;

	@SuppressWarnings("deprecation")
	public LegendForLayerComponentImpl() {
		setConstraint(new LayoutConstraint(LayoutConstraint.LEFT, LayoutConstraint.BOTTOM, LayoutConstraint.FLOW_Y, 0,
				0, 0, 5));
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void calculateSize(PdfContext context) {
		float marginX = -1.0f;
		LegendViaUrlComponent legendViaUrlComponent = null;

		for (PrintComponent<?> child : children) {
			if (marginX < 0f && child instanceof LabelComponent && null != child.getConstraint()) {
				marginX = child.getConstraint().getMarginX();
			} else if (child instanceof LegendViaUrlComponent) {
				legendViaUrlComponent = (LegendViaUrlComponent) child;
			}
		}
		if (marginX >= 0) {
			for (PrintComponent<?> child : children) {
				assert (child instanceof LabelComponent || null != child.getConstraint()) : 
						"For Non-label children of LegendForLayerComponent getConstraint() must be non-null";
				// This is required because it´s difficult to create a constraint instance here
				if (!(child instanceof LabelComponent) && (null != child.getConstraint())) {
					// Set the left margin (marginX) of the item in the legend (e.g an image
					// presenting the legend symbols of the concerning layer)

					if (child.getConstraint().getMarginX() <= 0) {
						child.getConstraint().setMarginX(marginX * 2.0f); // 2 times the margin
					} // of the title (usually the layer´s label
				}

			}
		}
		super.calculateSize(context);

		if (null != legendViaUrlComponent && !legendViaUrlComponent.isVisible()) {
			// Force zero-sized bounds for this component (layer title + legend graphic are not shown)
			setBounds(new Rectangle(0, 0));
			visible = false;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(PdfContext context) {
		if (null == getSize()) {
			calculateSize(context);
		}
		if (visible) {
			super.render(context);
		}
	}

	@SuppressWarnings("deprecation")
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