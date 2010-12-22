package org.geomajas.puregwt.client.map;

/** 
 * Zoom options. 
 *
 * @author Jan De Moerloose
 */
public enum ZoomOption {

	/** Zoom exactly to the new scale. */
	EXACT,

	/**
	 * Zoom to a scale level that is different from the current (lower or higher according to the new scale, only if
	 * allowed of course).
	 */
	LEVEL_CHANGE,

	/** Zoom to a scale level that is as close as possible to the new scale. */
	LEVEL_CLOSEST,

	/** Zoom to a scale level that makes the bounds fit inside our view. */
	LEVEL_FIT
}