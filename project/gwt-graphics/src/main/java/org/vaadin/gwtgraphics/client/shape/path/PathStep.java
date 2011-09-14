package org.vaadin.gwtgraphics.client.shape.path;

/**
 * This class is an abstract class for Path steps.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public abstract class PathStep {

	public abstract String getSVGString();
	
	public abstract void scale(ScaleHelper scaleHelper);

	@Override
	public String toString() {
		return getSVGString();
	}
}
