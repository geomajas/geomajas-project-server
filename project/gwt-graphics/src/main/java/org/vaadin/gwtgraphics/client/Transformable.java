package org.vaadin.gwtgraphics.client;

/**
 * Vector objects implementing this interface should subject all their internal coordinate values to the specified
 * transformation operations before drawing themselves. Such vector objects typically provide a way to define their
 * coordinates in user space (using double precision) by appropriately named methods, e.g. setUserX(). After
 * transformation, normal screen space coordinates can be retrieved in the usual manner, e.g. getX(). Although the
 * transformation parameters are determined by the application, implementations may impose limits for some vector
 * objects, e.g. circle may not turn into eclipse if scaleX != scaleY.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface Transformable {

	/**
	 * Translate this vector object over the specified distance in the x and y direction (negative axis). This
	 * translation will stay into effect until this method gets called again.
	 * 
	 * @param deltaX distance in x direction
	 * @param deltaY distance in y direction
	 */
	void setTranslation(double deltaX, double deltaY);

	/**
	 * Scale this vector object with the specified factors in the x and y direction.
	 * 
	 * @param scaleX scale factor in the x-direction
	 * @param scaleY scale factor in the y-direction
	 */
	void setScale(double scaleX, double scaleY);
}
