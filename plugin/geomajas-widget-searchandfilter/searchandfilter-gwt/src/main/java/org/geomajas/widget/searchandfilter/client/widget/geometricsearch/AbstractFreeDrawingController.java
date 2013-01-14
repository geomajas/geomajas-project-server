/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import org.geomajas.gwt.client.controller.AbstractSnappingController;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Basic template for a controller that handles the editing of certain types of geometries.
 * 
 * @author Bruce Palmkoeck
 */
public abstract class AbstractFreeDrawingController extends AbstractSnappingController {

	/**
	 * Reference to a parent drawing controller. Drawing controllers support a hierarchical structure where a parent
	 * delegates to the correct child DrawController, depending on the circumstances.
	 */
	protected AbstractFreeDrawingController parent;

	protected Geometry geometry;

	protected GeometryFactory factory;
	
	protected GeometryDrawHandler handler;

	/** The currently active editing modus. */
	private EditMode editMode;

	/** Option to show the maximum bounds wherein editing is allowed. */
	private boolean maxBoundsDisplayed;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	/**
	 * Protected and only constructor. Extending classes must use this constructor.
	 * 
	 * @param mapWidget Reference to the map widget on which drawing is in progress.
	 * @param parent The parent drawing controller, or null if there is none.
	 * @param handler called when a geometry is drawn.
	 */
	protected AbstractFreeDrawingController(MapWidget mapWidget, AbstractFreeDrawingController parent,
			GeometryDrawHandler handler) {
		super(mapWidget);
		this.parent = parent;
		this.handler = handler;
		factory = new GeometryFactory(mapWidget.getMapModel().getSrid(), mapWidget.getMapModel().getPrecision());
		geometry = null;
		// TODO Auto-generated constructor stub
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Clean up all graphical stuff.
	 */
	public abstract void cleanup();

	/**
	 * True if this controller is busy. A busy controller should receive all subsequent mouse events until this method
	 * returns false.
	 * 
	 * @return true if busy
	 */
	public abstract boolean isBusy();

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Reference to a parent drawing controller. Drawing controllers support a hierarchical structure where a parent
	 * delegates to the correct child DrawController, depending on the circumstances.
	 */
	public AbstractFreeDrawingController getParent() {
		return parent;
	}

	/** The currently active editing modus. */
	public EditMode getEditMode() {
		return editMode;
	}

	public void setEditMode(EditMode editMode) {
		this.editMode = editMode;
	}

	/** Should the maximum bounds wherein drawing is allowed be rendered on the map or not? */
	public boolean isMaxBoundsDisplayed() {
		return maxBoundsDisplayed;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Determine whether or not the maximum bounds wherein drawing is allowed be rendered on the map.
	 * 
	 * @param maxBoundsDisplayed
	 *            The new value.
	 */
	public void setMaxBoundsDisplayed(boolean maxBoundsDisplayed) {
		this.maxBoundsDisplayed = maxBoundsDisplayed;
	}
}
