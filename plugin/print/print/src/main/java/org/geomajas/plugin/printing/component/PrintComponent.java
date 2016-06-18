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

import java.util.Collection;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.ExpectAlternatives;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;

import com.lowagie.text.Rectangle;

/**
 * A print component is a UI component that can be rendered in a PDF context. Examples are maps, images, labels, text
 * boxes, etc.. A print component can be composed of child components.
 * <p />
 * Use the abstract {@link org.geomajas.plugin.printing.component.impl.AbstractPrintComponent} class as base class.
 *
 * @author Jan De Moerloose
 * @since 2.0.0
 *
 * @param <T> DTO object class
 */
@Api(allMethods = true)
@ExpectAlternatives
public interface PrintComponent<T extends PrintComponentInfo> {

	/**
	 * Return the id of this component. Id's should be hierarchically constructed with "." as delimiter.
	 * 
	 * @return component id
	 */
	String getId();

	/**
	 * Return the tag of this component.
	 * 
	 * @return id
	 */
	String getTag();

	/**
	 * Render the component using the specified context. This operation assumes that layout() has been called.
	 * 
	 * @param context pdf context
	 */
	void render(PdfContext context);

	/**
	 * Layout this component and its children. After this operation, the position and size of each component (both
	 * included in the bounds property) in the component hierarchy have been determined. This operation assumes that
	 * calculateSize() has been called.
	 * 
	 * @param context pdf context
	 */
	void layout(PdfContext context);

	/**
	 * Calculate the expected width/height of this component for the specified context.
	 * 
	 * @param context pdf context
	 */
	void calculateSize(PdfContext context);

	/**
	 * Return the rectangle in user space occupied by this component.
	 * 
	 * @return component bounds
	 */
	Rectangle getBounds();

	/**
	 * Set the component bounds in user space for this component.
	 *
	 * @param bounds component bounds
	 */
	void setBounds(Rectangle bounds);

	/**
	 * Return the layout constraint that should be applied when laying out this component.
	 * 
	 * @return constraint
	 */
	LayoutConstraint getConstraint();

	/**
	 * Set the this component's parent.
	 *
	 * @param parent parent component
	 */
	void setParent(PrintComponent<?> parent);

	/**
	 * Return the parent component of this component.
	 * 
	 * @return parent component
	 */
	PrintComponent<?> getParent();

	/**
	 * Get list with child components.
	 *
	 * @return list of children
	 */
	List<PrintComponent<?>> getChildren();

	/**
	 * Get child component with given id.
	 *
	 * @param id component id
	 * @return requested child
	 */
	PrintComponent<?> getChild(String id);

	/**
	 * Add component as last child.
	 *
	 * @param child child
	 */
	void addComponent(PrintComponent<?> child);

	/**
	 * Add child component at given index.
	 *
	 * @param index index
	 * @param child component
	 */
	void addComponent(int index, PrintComponent<?> child);

	/**
	 * Add collection of child components (at end of list).
	 *
	 * @param children child components
	 */
	void addComponents(Collection<? extends PrintComponent<?>> children);

	/**
	 * Add collection of child components at specific index.
	 *
	 * @param index index
	 * @param children child components
	 */
	void addComponents(int index, Collection<? extends PrintComponent<?>> children);

	/**
	 * Remove child component by id.
	 *
	 * @param child child to remove
	 */
	void removeComponent(PrintComponent<?> child);

	/**
	 * Call back visitor.
	 * 
	 * @param visitor visitor
	 */
	void accept(PrintComponentVisitor visitor);

	/**
	 * Get data from the dto.
	 *
	 * @param info dto to convert
	 */
	void fromDto(T info);
}
