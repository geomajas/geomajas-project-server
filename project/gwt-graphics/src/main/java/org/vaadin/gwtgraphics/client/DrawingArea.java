package org.vaadin.gwtgraphics.client;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.gwtgraphics.client.impl.SVGImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

/**
 * The following example shows how a DrawingArea instance is created and added
 * to a GWT application. A rectangle is added to this DrawingArea instance. When
 * the user clicks this rectangle its color changes.
 * 
 * <pre>
 * DrawingArea canvas = new DrawingArea(200, 200);
 * RootPanel.get().add(canvas);
 * 
 * Rectangle rect = new Rectangle(10, 10, 100, 50);
 * canvas.add(rect);
 * rect.setFillColor(&quot;blue&quot;);
 * rect.addClickHandler(new ClickHandler() {
 * 	public void onClick(ClickEvent event) {
 * 		Rectangle rect = (Rectangle) event.getSource();
 * 		if (rect.getFillColor().equals(&quot;blue&quot;)) {
 * 			rect.setFillColor(&quot;red&quot;);
 * 		} else {
 * 			rect.setFillColor(&quot;blue&quot;);
 * 		}
 * 	}
 * });
 * </pre>
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class DrawingArea extends Widget implements VectorObjectContainer,
		HasClickHandlers, HasAllMouseHandlers, HasDoubleClickHandlers {

	private static final SVGImpl impl = GWT.create(SVGImpl.class);

	private final Element root;

	private List<VectorObject> childrens = new ArrayList<VectorObject>();

	/**
	 * Creates a DrawingArea of given width and height.
	 * 
	 * @param width
	 *            the width of DrawingArea in pixels
	 * @param height
	 *            the height of DrawingArea in pixels
	 */
	public DrawingArea(int width, int height) {
		DivElement container = Document.get().createDivElement();
		setElement(container);

		root = getImpl().createDrawingArea(container, width, height);
	}

	protected SVGImpl getImpl() {
		return impl;
	}

	/**
	 * Returns a String that indicates what graphics renderer is used. This
	 * String is "VML" for Internet Explorer and "SVG" for other browsers.
	 * 
	 * @return the used graphics renderer
	 */
	public String getRendererString() {
		return getImpl().getRendererString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.VectorObjectContainer#add(org.vaadin.
	 * gwtgraphics.client.VectorObject)
	 */
	public VectorObject add(VectorObject vo) {
		getImpl().add(root, vo.getElement(), vo.isAttached());
		vo.setParent(this);
		childrens.add(vo);
		return vo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.VectorObjectContainer#insert(org.vaadin
	 * .gwtgraphics.client.VectorObject, int)
	 */
	public VectorObject insert(VectorObject vo, int beforeIndex) {
		if (beforeIndex < 0 || beforeIndex > getVectorObjectCount()) {
			throw new IndexOutOfBoundsException();
		}

		if (childrens.contains(vo)) {
			childrens.remove(vo);
		}

		childrens.add(beforeIndex, vo);
		vo.setParent(this);
		getImpl().insert(root, vo.getElement(), beforeIndex, vo.isAttached());

		return vo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.VectorObjectContainer#bringToFront(org.
	 * vaadin.gwtgraphics.client.VectorObject)
	 */
	public VectorObject bringToFront(VectorObject vo) {
		if (vo.getParent() != this) {
			return null;
		}
		getImpl().bringToFront(root, vo.getElement());
		return vo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.VectorObjectContainer#remove(org.vaadin
	 * .gwtgraphics.client.VectorObject)
	 */
	public VectorObject remove(VectorObject vo) {
		if (vo.getParent() != this) {
			return null;
		}
		vo.setParent(null);
		root.removeChild(vo.getElement());
		childrens.remove(vo);
		return vo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.VectorObjectContainer#clear()
	 */
	public void clear() {
		List<VectorObject> childrensCopy = new ArrayList<VectorObject>();
		childrensCopy.addAll(childrens);
		for (VectorObject vo : childrensCopy) {
			this.remove(vo);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.VectorObjectContainer#getVectorObject(int)
	 */
	public VectorObject getVectorObject(int index) {
		return childrens.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vaadin.gwtgraphics.client.VectorObjectContainer#getVectorObjectCount
	 * ()
	 */
	public int getVectorObjectCount() {
		return childrens.size();
	}

	/**
	 * Returns the width of the DrawingArea in pixels.
	 * 
	 * @return the width of the DrawingArea in pixels.
	 */
	public int getWidth() {
		return getImpl().getWidth(root);
	}

	/**
	 * Sets the width of the DrawingArea in pixels.
	 * 
	 * @param width
	 *            the new width in pixels
	 */
	public void setWidth(int width) {
		getImpl().setWidth(root, width);
	}

	/**
	 * Returns the height of the DrawingArea in pixels.
	 * 
	 * @return the height of the DrawingArea in pixels.
	 */
	public int getHeight() {
		return getImpl().getHeight(root);
	}

	/**
	 * Sets the height of the DrawingArea in pixels.
	 * 
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		getImpl().setHeight(root, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.UIObject#setHeight(java.lang.String)
	 */
	@Override
	public void setHeight(String height) {
		boolean successful = false;
		if (height != null && height.endsWith("px")) {
			try {
				setHeight(Integer.parseInt(height.substring(0,
						height.length() - 2)));
				successful = true;
			} catch (NumberFormatException e) {
			}
		}
		if (!successful) {
			throw new IllegalArgumentException(
					"Only pixel units (px) are supported");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.UIObject#setWidth(java.lang.String)
	 */
	@Override
	public void setWidth(String width) {
		boolean successful = false;
		if (width != null && width.endsWith("px")) {
			try {
				setWidth(Integer.parseInt(width
						.substring(0, width.length() - 2)));
				successful = true;
			} catch (NumberFormatException e) {
			}
		}
		if (!successful) {
			throw new IllegalArgumentException(
					"Only pixel units (px) are supported");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.UIObject#setStyleName(java.lang.String)
	 */
	@Override
	public void setStyleName(String style) {
		getElement().setClassName(
				style + " " + style + "-" + getImpl().getStyleSuffix());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.HasClickHandlers#addClickHandler(com.
	 * google.gwt.event.dom.client.ClickHandler)
	 */
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.HasDoubleClickHandlers#addDoubleClickHandler
	 * (com.google.gwt.event.dom.client.DoubleClickHandler)
	 */
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.HasMouseDownHandlers#addMouseDownHandler
	 * (com.google.gwt.event.dom.client.MouseDownHandler)
	 */
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.HasMouseUpHandlers#addMouseUpHandler(
	 * com.google.gwt.event.dom.client.MouseUpHandler)
	 */
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.HasMouseOutHandlers#addMouseOutHandler
	 * (com.google.gwt.event.dom.client.MouseOutHandler)
	 */
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.HasMouseOverHandlers#addMouseOverHandler
	 * (com.google.gwt.event.dom.client.MouseOverHandler)
	 */
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.HasMouseMoveHandlers#addMouseMoveHandler
	 * (com.google.gwt.event.dom.client.MouseMoveHandler)
	 */
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.HasMouseWheelHandlers#addMouseWheelHandler
	 * (com.google.gwt.event.dom.client.MouseWheelHandler)
	 */
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#doAttachChildren()
	 */
	@Override
	protected void doAttachChildren() {
		for (VectorObject vo : childrens) {
			vo.onAttach();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#doDetachChildren()
	 */
	@Override
	protected void doDetachChildren() {
		for (VectorObject vo : childrens) {
			vo.onDetach();
		}
	}
}
