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
package org.geomajas.plugin.printing.component;

import java.util.List;

import org.geomajas.plugin.printing.PdfContext;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;

import com.lowagie.text.Rectangle;

/**
 * A print component is a UI component that can be rendered in a PDF context.
 * Examples are maps, images, labels, text boxes, etc.. A print component can be
 * composed of child components.
 *
 * @author Jan De Moerloose
 */
public interface PrintComponent {

	/**
	 * Return the id of this component. Id's should be hierarchically
	 * constructed with "." as delimiter.
	 *
	 * @return id
	 */
	String getId();

	/**
	 * Return the tag of this component.
	 *
	 * @return id
	 */
	String getTag();

	/**
	 * Render the component using the specified context. This operation assumes
	 * that layout() has been called.
	 *
	 * @param context
	 */
	void render(PdfContext context);

	/**
	 * Layout this component and its children. After this operation, the
	 * position and size of each component (both included in the bounds
	 * property) in the component hierarchy have been determined. This operation
	 * assumes that calculateSize() has been called.
	 *
	 * @param context
	 */
	void layout(PdfContext context);

	/**
	 * Calculate the expected width/height of this component for the specified
	 * context.
	 *
	 * @param context
	 */
	void calculateSize(PdfContext context);

	/**
	 * Return the rectangle in user space occupied by this component.
	 *
	 * @return rectangle
	 */
	Rectangle getBounds();

	void setBounds(Rectangle bounds);

	/**
	 * Return the layout constraint that should be applied when laying out this
	 * component.
	 *
	 * @return constraint
	 */
	LayoutConstraint getConstraint();

	void setParent(PrintComponent parent);

	/**
	 * Return the parent component of this component.
	 *
	 * @return
	 */
	PrintComponent getParent();

	List<PrintComponent> getChildren();

	PrintComponent getChild(String id);

	void addComponent(PrintComponent child);

	void addComponent(int index, PrintComponent child);
	
	void removeComponent(PrintComponent child);

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	void accept(PrintComponentVisitor visitor);
	
	void fromDto(PrintComponentInfo info, PrintDtoConverterService service);
}
