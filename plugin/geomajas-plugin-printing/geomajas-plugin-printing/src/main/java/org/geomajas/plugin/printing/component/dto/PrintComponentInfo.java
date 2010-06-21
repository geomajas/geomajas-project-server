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
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;

/**
 * DTO object for PrintComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.PrintComponent
 *
 */
public abstract class PrintComponentInfo implements Serializable {

	private String id;

	private String tag;

	private List<PrintComponentInfo> children = new ArrayList<PrintComponentInfo>();

	private Bbox bounds;

	private LayoutConstraintInfo layoutConstraint = new LayoutConstraintInfo();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<PrintComponentInfo> getChildren() {
		return children;
	}

	public void setChildren(List<PrintComponentInfo> children) {
		this.children = children;
	}

	public void addChild(PrintComponentInfo child) {
		children.add(child);
	}

	public Bbox getBounds() {
		return bounds;
	}

	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	public LayoutConstraintInfo getLayoutConstraint() {
		return layoutConstraint;
	}

	public void setLayoutConstraint(LayoutConstraintInfo layoutConstraint) {
		this.layoutConstraint = layoutConstraint;
	}

}
