/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
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

	private String locale = "en";
	
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
	 * Get the locale.
	 * 
	 * @param locale
	 *            which should be used for error messages in the print
	 */
	@Override
	public String getLocale() {
		return locale;
	}

	/**
	 * Set the locale.
	 * 
	 * @return locale
	 *            which should be used for error messages in the print
	 */
	@Override
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	@Override
	public void fromDto(PageComponentInfo pageInfo) {
		super.fromDto(pageInfo);
		setLocale(pageInfo.getLocale());
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
