package org.vaadin.gwtgraphics.client;

/**
 * Classes implementing this class are able to contain VectorObjects.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public interface VectorObjectContainer {

	/**
	 * 
	 * Add the given VectorObject to this VectorObjectContainer.
	 * 
	 * @param vo
	 *            VectorObject to be added
	 * @return added VectorObject
	 */
	public abstract VectorObject add(VectorObject vo);

	/**
	 * Insert the given VectorObject before the specified index.
	 * 
	 * If the VectorObjectContainer contains already the VectorObject, it will
	 * be removed from the VectorObjectContainer before insertion.
	 * 
	 * @param vo
	 *            VectorObject to be inserted
	 * @param beforeIndex
	 *            the index before which the VectorObject will be inserted.
	 * @return inserted VectorObject
	 * @throws IndexOutOfBoundsException
	 *             if <code>beforeIndex</code> is out of range
	 */
	public abstract VectorObject insert(VectorObject vo, int beforeIndex);

	/**
	 * Remove the given VectorObject from this VectorObjectContainer.
	 * 
	 * @param vo
	 *            VectorObject to be removed
	 * 
	 * @return removed VectorObject or null if the container doesn't contained
	 *         the given VectorObject
	 */
	public abstract VectorObject remove(VectorObject vo);

	/**
	 * 
	 * Brings the given VectorObject to front in this VectorObjectContainer.
	 * 
	 * @param vo
	 *            VectorObject to be brought to front
	 * @return the popped VectorObject
	 */
	public abstract VectorObject bringToFront(VectorObject vo);

	/**
	 * Removes all contained VectorObjects from this VectorObjectContainer.
	 */
	public abstract void clear();

	/**
	 * Returns the number of VectorObjects in this VectorObjectContainer.
	 * 
	 * @return the number of VectorObjects in this VectorObjectContainer.
	 */
	public abstract int getVectorObjectCount();

	/**
	 * Returns the VectorObject element at the specified position.
	 * 
	 * @param index
	 *            index of element to return.
	 * @return the VectorObject element at the specified position.
	 */
	public abstract VectorObject getVectorObject(int index);

}