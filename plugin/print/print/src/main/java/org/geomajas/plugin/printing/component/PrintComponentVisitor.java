/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component;


/**
 * Visitor pattern for print component tree.
 *
 * @author Jan De Moerloose
 */
public interface PrintComponentVisitor {

	void visit(LabelComponent label);

	void visit(LegendComponent legend);

	void visit(MapComponent<?> map);

	void visit(PageComponent page);

	void visit(ScaleBarComponent scaleBar);

	void visit(ViewPortComponent viewPort);

}
