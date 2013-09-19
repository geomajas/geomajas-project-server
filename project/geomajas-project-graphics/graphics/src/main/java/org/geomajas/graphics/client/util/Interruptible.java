/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.util;


/**
 * Interface for {@link Action} or Controller classes that have an interruptible workflow.
 * 
 * @author Jan Venstermans
 * 
 */
public interface Interruptible {
	
	/**
	 * Stops the workflow and does not save new sitation.
	 */
	void cancel();
	
	/**
	 * Indicates a start of the workflow.
	 */
	void start();
	
	/**
	 * Stops the workflow.
	 */
	void stop();
	
	/**
	 * Saves the current situation.
	 */
	void save();
	
	/**
	 * Stops the workflow, saves the current situation and flags that workflow is interrupted.
	 */
	void pause();
	
	/**
	 * Starts new workflow, based on paused situation.
	 */
	void resume();
	
	/**
	 * Whether in pause state.
	 */
	boolean isInterrupted();
	
	/**
	 * Whether workflow is in progress.
	 */
	boolean isInProgress();
}