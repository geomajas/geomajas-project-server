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
package org.geomajas.plugin.printing.component.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.Json;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.parser.RectangleConverter;

import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Basic container component for printing. Handles the size calculation, layout and rendering of its children.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 * 
 * @param <T> DTO object class
 * @deprecated use AbstractPrintComponent instead
 */
@Deprecated
@Api(allMethods = true)
public abstract class PrintComponentImpl<T extends PrintComponentInfo> implements PrintComponent<T> {

	@XStreamConverter(RectangleConverter.class)
	private Rectangle bounds = new Rectangle(0, 0);

	@XStreamOmitField
	private PrintComponent<?> parent;

	private LayoutConstraint constraint;

	protected String id;

	@XStreamImplicit(itemFieldName = "component")
	protected List<PrintComponent<?>> children = new ArrayList<PrintComponent<?>>();

	protected String tag;

	/**
	 * Create a print component with empty id and no constraints.
	 */
	public PrintComponentImpl() {
		this("");
	}

	/**
	 * Create a print component with given id and no constraints.
	 *
	 * @param id component id
	 */
	public PrintComponentImpl(String id) {
		this(id, new LayoutConstraint());
	}

	/**
	 * Create a print component with given id and constraints.
	 *
	 * @param id id
	 * @param constraint constraints
	 */
	public PrintComponentImpl(String id, LayoutConstraint constraint) {
		this.id = id;
		this.constraint = constraint;
	}

	/**
	 * Default implementation.
	 */
	public void accept(PrintComponentVisitor visitor) {
	}

	@Override
	public void layout(PdfContext context) {
		// top down layout
		float x = getBounds().getLeft();
		float y = getBounds().getBottom();
		float w = getBounds().getWidth();
		float h = getBounds().getHeight();
		if (getConstraint().getFlowDirection() == LayoutConstraint.FLOW_Y) {
			y = getBounds().getTop();
		}
		for (PrintComponent<?> child : children) {
			float cw = child.getBounds().getWidth();
			float ch = child.getBounds().getHeight();
			float marginX = child.getConstraint().getMarginX();
			float marginY = child.getConstraint().getMarginY();

			switch (getConstraint().getFlowDirection()) {
				case LayoutConstraint.FLOW_NONE:
					layoutChild(child, getBounds());
					break;
				case LayoutConstraint.FLOW_X:
					layoutChild(child, new Rectangle(x, y, x + cw + 2 * marginX, y + h));
					x += cw + 2 * marginX;
					break;
				case LayoutConstraint.FLOW_Y:
					layoutChild(child, new Rectangle(x, y - ch - 2 * marginY, x + w, y));
					y -= ch + 2 * marginY;
					break;
				default:
					throw new IllegalStateException("Unknown flow direction " + getConstraint().getFlowDirection());
			}
		}
		for (PrintComponent<?> child : children) {
			child.layout(context);
		}
	}

	/**
	 * Calculates the size based constraint width and height if present, otherwise from children sizes.
	 */
	public void calculateSize(PdfContext context) {
		float width = 0;
		float height = 0;
		for (PrintComponent<?> child : children) {
			child.calculateSize(context);
			float cw = child.getBounds().getWidth() + 2 * child.getConstraint().getMarginX();
			float ch = child.getBounds().getHeight() + 2 * child.getConstraint().getMarginY();
			switch (getConstraint().getFlowDirection()) {
				case LayoutConstraint.FLOW_NONE:
					width = Math.max(width, cw);
					height = Math.max(height, ch);
					break;
				case LayoutConstraint.FLOW_X:
					width += cw;
					height = Math.max(height, ch);
					break;
				case LayoutConstraint.FLOW_Y:
					width = Math.max(width, cw);
					height += ch;
					break;
				default:
					throw new IllegalStateException("Unknown flow direction " + getConstraint().getFlowDirection());
			}
		}
		if (getConstraint().getWidth() != 0) {
			width = getConstraint().getWidth();
		}
		if (getConstraint().getHeight() != 0) {
			height = getConstraint().getHeight();
		}
		setBounds(new Rectangle(0, 0, width, height));
	}

	@Override
	public void render(PdfContext context) {
		for (PrintComponent<?> child : children) {
			context.saveOrigin();
			context.setOrigin(child.getBounds().getLeft(), child.getBounds().getBottom());
			child.render(context);
			context.restoreOrigin();
		}
	}

	@Override
	public void addComponent(PrintComponent<?> child) {
		if (!children.contains(child)) {
			child.setParent(this);
			children.add(child);
		}
	}

	@Override
	public void removeComponent(PrintComponent<?> child) {
		if (children.contains(child)) {
			child.setParent(null);
			children.remove(child);
		}
	}

	@Override
	public void addComponent(int index, PrintComponent<?> child) {
		child.setParent(this);
		children.add(index, child);
	}

	@Override
	public void addComponents(Collection<? extends PrintComponent<?>> children) {
		for (PrintComponent<?> child : children) {
			addComponent(child);
		}
	}

	@Override
	public void addComponents(int index, Collection<? extends PrintComponent<?>> children) {
		// reverse add to index to keep the order
		List<PrintComponent<?>> reverse = new ArrayList<PrintComponent<?>>(children);
		Collections.reverse(reverse);
		for (PrintComponent<?> child : reverse) {
			addComponent(index, child);
		}
	}

	@Override
	@Json(serialize = false)
	public PrintComponent<?> getParent() {
		return parent;
	}

	@Override
	public void setParent(PrintComponent<?> parent) {
		this.parent = parent;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * Return a rectangle with the size of this component and origin (0,0).
	 * 
	 * @return rectangle with the size of this component
	 */
	protected Rectangle getSize() {
		return new Rectangle(getBounds().getWidth(), getBounds().getHeight());
	}

	@Override
	public LayoutConstraint getConstraint() {
		return constraint;
	}

	/**
	 * Set constraint on this component.
	 *
	 * @param constraint constraint
	 */
	public void setConstraint(LayoutConstraint constraint) {
		this.constraint = constraint;
	}

	private void layoutChild(PrintComponent<?> child, Rectangle box) {
		LayoutConstraint layoutConstraint = child.getConstraint();
		float bx = box.getLeft();
		float by = box.getBottom();
		float bw = box.getWidth();
		float bh = box.getHeight();
		float cw = child.getBounds().getWidth();
		float ch = child.getBounds().getHeight();
		float marginX = layoutConstraint.getMarginX();
		float marginY = layoutConstraint.getMarginY();
		float absw = layoutConstraint.getWidth();
		float absh = layoutConstraint.getHeight();
		float x = 0;
		float y = 0;
		float w = cw;
		float h = ch;
		switch (layoutConstraint.getAlignmentX()) {
			case LayoutConstraint.LEFT:
				x = bx + marginX;
				break;
			case LayoutConstraint.CENTER:
				x = bx + (bw - cw) / 2;
				break;
			case LayoutConstraint.RIGHT:
				x = bx + bw - marginX - cw;
				break;
			case LayoutConstraint.JUSTIFIED:
				x = bx + marginX;
				w = bw - 2 * marginX;
				break;
			case LayoutConstraint.ABSOLUTE:
				x = marginX;
				w = absw;
				break;
			default:
				throw new IllegalStateException("Unknown X alignment " + layoutConstraint.getAlignmentX());
		}
		switch (layoutConstraint.getAlignmentY()) {
			case LayoutConstraint.BOTTOM:
				y = by + marginY;
				break;
			case LayoutConstraint.CENTER:
				y = by + (bh - ch) / 2;
				break;
			case LayoutConstraint.TOP:
				y = by + bh - marginY - ch;
				break;
			case LayoutConstraint.JUSTIFIED:
				y = by + marginY;
				h = bh - 2 * marginY;
				break;
			case LayoutConstraint.ABSOLUTE:
				y = marginY;
				h = absh;
				break;
			default:
				throw new IllegalStateException("Unknown Y alignment " + layoutConstraint.getAlignmentY());
		}
		child.setBounds(new Rectangle(x, y, x + w, y + h));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + (int) getBounds().getLeft() + " : " + (int) getBounds().getRight()
				+ "] x [" + (int) getBounds().getBottom() + " : " + (int) getBounds().getTop() + "]";
	}

	@Override
	public List<PrintComponent<?>> getChildren() {
		return children;
	}

	@Override
	public PrintComponent<?> getChild(String childTag) {
		for (PrintComponent<?> child : children) {
			if (child.getTag() != null && child.getTag().equals(childTag)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Set child components.
	 *
	 * @param children children
	 */
	public void setChildren(List<PrintComponent<?>> children) {
		this.children = children;
		// needed for Json unmarshall !!!!
		for (PrintComponent<?> child : children) {
			child.setParent(this);
		}
	}

	/**
	 * Set component id.
	 *
	 * @param id id
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Set tag.
	 *
	 * @param tag tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public void fromDto(T info) {
		if (info.getBounds() != null) {
			setBounds(createRectangle(info.getBounds()));
		}
		if (info.getLayoutConstraint() != null) {
			getConstraint().fromDto(info.getLayoutConstraint());
		}
		setId(info.getId());
		setTag(info.getTag());
	}

	private Rectangle createRectangle(Bbox bounds) {
		return new Rectangle((float) bounds.getX(), (float) bounds.getY(), (float) bounds.getX()
				+ (float) bounds.getWidth(), (float) bounds.getY() + (float) bounds.getHeight());
	}

}
