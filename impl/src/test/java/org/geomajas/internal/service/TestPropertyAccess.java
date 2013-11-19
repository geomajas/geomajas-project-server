package org.geomajas.internal.service;

/**
 * To be implemented by test features for testing filters.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface TestPropertyAccess {

	Object get(String xpath, Class target);
}
