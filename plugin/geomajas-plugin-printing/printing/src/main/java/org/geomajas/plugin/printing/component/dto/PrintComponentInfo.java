/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * DTO object for PrintComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.PrintComponent
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public abstract class PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	private static final String ORG_GEOMAJAS = "org.geomajas.";

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
		if (child != null) {
			children.add(child);
		}
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
	
	public String getPrototypeName() {
		String name = getClass().getName();
		if (name.startsWith(ORG_GEOMAJAS)) {
			name = name.substring(ORG_GEOMAJAS.length());
		}
		name = name.replace(".dto.", ".impl.");
		return name.substring(0, name.length() - 4) + "Impl";
	}

}
