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

import com.lowagie.text.Rectangle;
import org.geomajas.global.Json;
import org.geomajas.plugin.printing.PdfContext;
import org.geomajas.plugin.printing.parser.RectangleAdapter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic container component for printing. Handles the size calculation, layout and rendering of its children.
 * 
 * @author Jan De Moerloose
 */
public abstract class BaseComponent implements PrintComponent {

	private Rectangle bounds = new Rectangle(0, 0);

	@XmlTransient
	private PrintComponent parent;

	private LayoutConstraint constraint;

	protected String id;

	protected List<PrintComponent> children = new ArrayList<PrintComponent>();

	protected String tag;

	public BaseComponent() {
		this("");
	}

	public BaseComponent(String id) {
		this(id, new LayoutConstraint());
	}

	public BaseComponent(String id, LayoutConstraint constraint) {
		this.id = id;
		this.constraint = constraint;
	}

	public void layout(PdfContext context) {
		// top down layout
		float x = getBounds().getLeft();
		float y = getBounds().getBottom();
		float w = getBounds().getWidth();
		float h = getBounds().getHeight();
		if (getConstraint().getFlowDirection() == LayoutConstraint.FLOW_Y) {
			y = getBounds().getTop();
		}
		for (PrintComponent child : children) {
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
			}
		}
		for (PrintComponent child : children) {
			child.layout(context);
		}
	}

	/**
	 * Calculates the size based constraint width and height if present, otherwise from children sizes.
	 */
	public void calculateSize(PdfContext context) {
		float width = 0;
		float height = 0;
		for (PrintComponent child : children) {
			child.calculateSize(context);
			float cw = child.getBounds().getWidth() + 2 * child.getConstraint().getMarginX();
			float ch = child.getBounds().getHeight() + 2 * child.getConstraint().getMarginY();
			switch (getConstraint().getFlowDirection()) {
				case LayoutConstraint.FLOW_NONE:
					width = Math.max(width, cw);
					height = Math.max(width, ch);
					break;
				case LayoutConstraint.FLOW_X:
					width += cw;
					height = Math.max(height, ch);
					break;
				case LayoutConstraint.FLOW_Y:
					width = Math.max(width, cw);
					height += ch;
					break;
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

	public void render(PdfContext context) {
		for (PrintComponent child : children) {
			context.saveOrigin();
			context.setOrigin(child.getBounds().getLeft(), child.getBounds().getBottom());
			child.render(context);
			context.restoreOrigin();
		}
	}

	public void addComponent(PrintComponent child) {
		if (!children.contains(child)) {
			child.setParent(this);
			children.add(child);
		}
	}

	public void removeComponent(PrintComponent child) {
		if (children.contains(child)) {
			child.setParent(null);
			children.remove(child);
		}
	}

	public void addComponent(int index, PrintComponent child) {
		child.setParent(this);
		children.add(index, child);
	}

	@XmlTransient
	@Json(serialize = false)
	public PrintComponent getParent() {
		return parent;
	}

	public void setParent(PrintComponent parent) {
		this.parent = parent;
	}

	@XmlJavaTypeAdapter(RectangleAdapter.class)
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * Return a rectangle with the size of this component and origin (0,0)
	 * 
	 * @return
	 */
	protected Rectangle getSize() {
		return new Rectangle(getBounds().getWidth(), getBounds().getHeight());
	}

	public LayoutConstraint getConstraint() {
		return constraint;
	}

	public void setConstraint(LayoutConstraint constraint) {
		this.constraint = constraint;
	}

	private void layoutChild(PrintComponent child, Rectangle box) {
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
		}
		child.setBounds(new Rectangle(x, y, x + w, y + h));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + (int) getBounds().getLeft() + " : " + (int) getBounds().getRight()
				+ "] x [" + (int) getBounds().getBottom() + " : " + (int) getBounds().getTop() + "]";
	}

	@XmlAnyElement(lax = true)
	public List<PrintComponent> getChildren() {
		return children;
	}

	public PrintComponent getChild(String childTag) {
		for (PrintComponent child : children) {
			if (child.getTag() != null && child.getTag().equals(childTag)) {
				return child;
			}
		}
		return null;
	}

	public void setChildren(List<PrintComponent> children) {
		this.children = children;
		// needed for Json unmarshall !!!!
		for (PrintComponent child : children) {
			child.setParent(this);
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	/**
	 * Resets cyclic references like child -> parent relationship Unfortunately, this has to be implemented in the
	 * concrete class !
	 * 
	 * @param u
	 * @param parentComponent
	 */
	public abstract void afterUnmarshal(Unmarshaller u, Object parentComponent);

}
