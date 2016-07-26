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
package org.geomajas.plugin.printing.component;


/**
 * Visitor for rendering the printed documents.
 *
 * @author Jan De Moerloose
 */
public abstract class TopDownVisitor implements PrintComponentVisitor {

	public void visitTree(PrintComponent<?> component) {
		component.accept(this);
		for (PrintComponent<?> child : component.getChildren()) {
			visitTree(child);
		}
	}

	public void visit(LabelComponent label) {
	}

	public void visit(LegendComponent legend) {
	}

	public void visit(MapComponent map) {
	}

	public void visit(PageComponent page) {
	}

	public void visit(ScaleBarComponent scaleBar) {
	}

	public void visit(ViewPortComponent viewPort) {
	}

}
