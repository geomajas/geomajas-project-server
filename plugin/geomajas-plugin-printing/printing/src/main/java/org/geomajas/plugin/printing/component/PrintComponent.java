/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component;

import java.util.Collection;
import java.util.List;

import org.geomajas.global.Api;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;

import com.lowagie.text.Rectangle;

/**
 * A print component is a UI component that can be rendered in a PDF context. Examples are maps, images, labels, text
 * boxes, etc.. A print component can be composed of child components.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 * 
 * @param <T> DTO object class
 */
@Api(allMethods = true)
public interface PrintComponent<T extends PrintComponentInfo> {

	/**
	 * Return the id of this component. Id's should be hierarchically constructed with "." as delimiter.
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
	 * Render the component using the specified context. This operation assumes that layout() has been called.
	 * 
	 * @param context
	 */
	void render(PdfContext context);

	/**
	 * Layout this component and its children. After this operation, the position and size of each component (both
	 * included in the bounds property) in the component hierarchy have been determined. This operation assumes that
	 * calculateSize() has been called.
	 * 
	 * @param context
	 */
	void layout(PdfContext context);

	/**
	 * Calculate the expected width/height of this component for the specified context.
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
	 * Return the layout constraint that should be applied when laying out this component.
	 * 
	 * @return constraint
	 */
	LayoutConstraint getConstraint();

	void setParent(PrintComponent<?> parent);

	/**
	 * Return the parent component of this component.
	 * 
	 * @return
	 */
	PrintComponent<?> getParent();

	List<PrintComponent<?>> getChildren();

	PrintComponent<?> getChild(String id);

	void addComponent(PrintComponent<?> child);

	void addComponent(int index, PrintComponent<?> child);

	void addComponents(Collection<? extends PrintComponent<?>> children);
	
	void addComponents(int index, Collection<? extends PrintComponent<?>> children);

	void removeComponent(PrintComponent<?> child);

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 */
	void accept(PrintComponentVisitor visitor);

	void fromDto(T info);
}
