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

	/** Get component id. */
	public String getId() {
		return id;
	}

	/**
	 * Set component id.
	 *
	 * @param id component id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get tag.
	 *
	 * @return tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Set tag.
	 *
	 * @param tag tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Get child components.
	 *
	 * @return children
	 */
	public List<PrintComponentInfo> getChildren() {
		return children;
	}

	/**
	 * Set child components.
	 *
	 * @param children child components
	 */
	public void setChildren(List<PrintComponentInfo> children) {
		this.children = children;
	}

	/**
	 * Add a child component.
	 *
	 * @param child component to add
	 */
	public void addChild(PrintComponentInfo child) {
		if (child != null) {
			children.add(child);
		}
	}

	/**
	 * Get page bounds for component.
	 *
	 * @return page bounds
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Set page bounds for component.
	 *
	 * @param bounds page bounds
	 */
	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	/**
	 * Get layout constraints.
	 *
	 * @return layout constraints
	 */
	public LayoutConstraintInfo getLayoutConstraint() {
		return layoutConstraint;
	}

	/**
	 * Set layout constraints.
	 *
	 * @param layoutConstraint layout constraints
	 */
	public void setLayoutConstraint(LayoutConstraintInfo layoutConstraint) {
		this.layoutConstraint = layoutConstraint;
	}

	/**
	 * Get prototype name.
	 *
	 * @return prototype name
	 */
	public String getPrototypeName() {
		String name = getClass().getName();
		if (name.startsWith(ORG_GEOMAJAS)) {
			name = name.substring(ORG_GEOMAJAS.length());
		}
		name = name.replace(".dto.", ".impl.");
		return name.substring(0, name.length() - 4) + "Impl";
	}

}
