package org.vaadin.gwtgraphics.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Group is a container, which can contain one or more VectorObjects.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public class Group extends VectorObject implements VectorObjectContainer {

	private List<VectorObject> childrens = new ArrayList<VectorObject>();

	/**
	 * Creates an empty Group.
	 */
	public Group() {
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Group.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vaadin.gwtgraphics.client.VectorObjectContainer#add(org.vaadin.
	 * gwtgraphics.client.VectorObject)
	 */
	public VectorObject add(VectorObject vo) {
		childrens.add(vo);
		getImpl().add(getElement(), vo.getElement(), vo.isAttached());
		vo.setParent(this);
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
		getImpl().insert(getElement(), vo.getElement(), beforeIndex,
				vo.isAttached());

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
		getElement().removeChild(vo.getElement());
		childrens.remove(vo);
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
		getImpl().bringToFront(getElement(), vo.getElement());
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
