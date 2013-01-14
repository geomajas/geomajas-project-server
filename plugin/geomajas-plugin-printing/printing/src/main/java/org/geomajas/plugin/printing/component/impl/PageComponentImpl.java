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

import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.PageComponentInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


/**
 * Page component for inclusion in printed document.
 *
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class PageComponentImpl extends AbstractPrintComponent<PageComponentInfo> implements PageComponent {

	public PageComponentImpl() {
		this("A4", true);
	}

	public PageComponentImpl(String size, boolean landscape) {
		setSize(size, landscape);
	}

	public void setSize(String size, boolean landscape) {
		Rectangle rect = null;
		if (landscape) {
			rect = PageSize.getRectangle(size).rotate();
		} else {
			rect = PageSize.getRectangle(size);
		}
		setBounds(rect);
		getConstraint().setWidth(rect.getWidth());
		getConstraint().setHeight(rect.getHeight());
	}

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

}
