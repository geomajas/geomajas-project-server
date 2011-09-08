package org.vaadin.gwtgraphics.client;

/**
 * This interface must be implemented by VectorObjects that can be stroked.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public interface Strokeable {

	/**
	 * Returns the stroke color.
	 */
	public abstract String getStrokeColor();

	/**
	 * Sets stroke color. The color value is specified using one of the CSS2
	 * color notations. For example, the following values are legal:
	 * <ul>
	 * <li>red
	 * <li>#ff0000
	 * <li>#f00
	 * <li>rgb(255, 0, 0)
	 * <li>rgb(100%, 0%, 0%)
	 * </ul>
	 * 
	 * @see http://www.w3.org/TR/CSS2/syndata.html#value-def-color
	 * @param color
	 *            the new stroke color
	 */
	public abstract void setStrokeColor(String color);

	/**
	 * Returns the stroke width in pixels.
	 * 
	 * @return the stroke width in pixels
	 */
	public abstract int getStrokeWidth();

	/**
	 * Sets the stroke width in pixels.
	 * 
	 * @param width
	 *            the stroke width in pixels
	 */
	public abstract void setStrokeWidth(int width);

	/**
	 * Returns the stroke opacity of the element.
	 * 
	 * @return the current stroke opacity
	 */
	public abstract double getStrokeOpacity();

	/**
	 * Sets the stroke opacity of the element. The initial value 1.0 means fully
	 * opaque stroke. On the other hand, value 0.0 means fully transparent
	 * paint.
	 * 
	 * @param opacity
	 *            the new stroke opacity
	 */
	public abstract void setStrokeOpacity(double opacity);

}