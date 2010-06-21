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

import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


/**
 * Page component for inclusion in printed document.
 *
 * @author Jan De Moerloose
 */
public class PageComponentImpl extends PrintComponentImpl implements PageComponent {

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
