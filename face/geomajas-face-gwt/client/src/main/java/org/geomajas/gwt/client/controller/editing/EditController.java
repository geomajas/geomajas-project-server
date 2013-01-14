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

package org.geomajas.gwt.client.controller.editing;

import org.geomajas.gwt.client.controller.AbstractSnappingController;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.DistanceFormat;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.menu.Menu;

/**
 * Basic template for a controller that handles the editing of certain types of geometries.
 * 
 * @author Pieter De Graef
 */
public abstract class EditController extends AbstractSnappingController {

	/**
	 * When editing geometries, 2 editing modes exist: one in which you constantly add new points to the geometry
	 * (INSERT_MODE), and one where you are free to move points, add or remove points etc (DRAG_MODE). This is the
	 * definition of both modes.
	 */
	public static enum EditMode {
		DRAG_MODE, INSERT_MODE
	}

	/**
	 * Reference to a parent editing controller. Editing controllers support a hierarchical structure where a parent
	 * delegates to the correct child EditController, depending on the circumstances.
	 */
	protected EditController parent;

	/** The currently active editing modus. */
	private EditMode editMode;

	/** The current active context menu on the map. */
	protected Menu menu;

	/** Definition of the label that displays geometric information of the geometry that's currently being edited. */
	protected GeometricInfoLabel infoLabel;

	/** Option to show the maximum bounds wherein editing is allowed. */
	private boolean maxBoundsDisplayed;

	/** Paintable for the maximum bounds wherein editing is allowed. */
	private GfxGeometry maxExtent;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	/**
	 * Protected and only constructor. Extending classes must use this constructor.
	 * 
	 * @param mapWidget
	 *            Reference to the map widget on which editing is in progress.
	 * @param parent
	 *            The parent editing controller, or null if there is none.
	 */
	protected EditController(MapWidget mapWidget, EditController parent) {
		super(mapWidget);
		this.parent = parent;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Return a context menu specific for this editing controller instance.
	 */
	public abstract Menu getContextMenu();

	/**
	 * When creating a new feature, the controller must specify through a <code>TransactionGeomIndex</code> object,
	 * where to begin adding coordinates in the geometry. The contents of this index will depend on the type of
	 * geometric object the edit controller supports.
	 * 
	 * @return
	 */
	public abstract TransactionGeomIndex getGeometryIndex();

	/**
	 * Set a new index at which inserting of points should happen.
	 * 
	 * @param geometryIndex
	 */
	public abstract void setGeometryIndex(TransactionGeomIndex geometryIndex);

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
	
	/**
	 * Show an overview of geometric attributes of the geometry that's being edited.
	 */
	public void showGeometricInfo() {
		FeatureTransaction ft = getFeatureTransaction();
		if (infoLabel == null && ft != null && ft.getNewFeatures() != null && ft.getNewFeatures().length > 0) {
			infoLabel = new GeometricInfoLabel();
			infoLabel.addClickHandler(new DestroyLabelInfoOnClick());
			infoLabel.setGeometry(ft.getNewFeatures()[0].getGeometry());
			infoLabel.animateMove(mapWidget.getWidth() - 155, 10);
		}
	}

	/**
	 * Update the overview of geometric attributes of the geometry that's being edited.
	 */
	public void updateGeometricInfo() {
		FeatureTransaction ft = getFeatureTransaction();
		if (infoLabel != null && ft != null && ft.getNewFeatures() != null && ft.getNewFeatures().length > 0) {
			infoLabel.setGeometry(ft.getNewFeatures()[0].getGeometry());
		}
	}

	/**
	 * Hide the overview of geometric attributes.
	 */
	public void hideGeometricInfo() {
		if (infoLabel != null) {
			infoLabel.destroy();
			infoLabel = null;
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Reference to a parent editing controller. Editing controllers support a hierarchical structure where a parent
	 * delegates to the correct child EditController, depending on the circumstances.
	 */
	public EditController getParent() {
		return parent;
	}

	/** The currently active editing modus. */
	public EditMode getEditMode() {
		return editMode;
	}

	public void setEditMode(EditMode editMode) {
		this.editMode = editMode;
	}

	public GeometricInfoLabel getInfoLabel() {
		return infoLabel;
	}

	/** Should the maximum bounds wherein editing is allowed be rendered on the map or not? */
	public boolean isMaxBoundsDisplayed() {
		return maxBoundsDisplayed;
	}

	/**
	 * Determine whether or not the maximum bounds wherein editing is allowed be rendered on the map.
	 * 
	 * @param maxBoundsDisplayed
	 *            The new value.
	 */
	public void setMaxBoundsDisplayed(boolean maxBoundsDisplayed) {
		this.maxBoundsDisplayed = maxBoundsDisplayed;
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	public void onActivate() {
		menu = getContextMenu();
		mapWidget.setContextMenu(menu);

		if (maxBoundsDisplayed) {
			VectorLayer layer = getFeatureTransaction().getLayer();
			GeometryFactory factory = mapWidget.getMapModel().getGeometryFactory();
			LinearRing hole = factory.createLinearRing(new Bbox(layer.getLayerInfo().getMaxExtent()));
			LinearRing shell = factory.createLinearRing(mapWidget.getMapModel().getMapView().getMaxBounds());
			Polygon polygon = factory.createPolygon(shell, new LinearRing[] { hole });

			maxExtent = new GfxGeometry("maxExtent");
			maxExtent.setGeometry(polygon);
			maxExtent.setStyle(new ShapeStyle("#000000", .6f, "#990000", 1, 2));
			mapWidget.registerWorldPaintable(maxExtent);
		}
	}

	public void onDeactivate() {
		if (maxExtent != null) {
			mapWidget.unregisterWorldPaintable(maxExtent);
		}
		if (getFeatureTransaction() != null) {
			mapWidget.render(getFeatureTransaction(), RenderGroup.VECTOR, RenderStatus.DELETE);
			mapWidget.getMapModel().getFeatureEditor().stopEditing();
		}
		if (menu != null) {
			menu.destroy();
			menu = null;
			mapWidget.setContextMenu(null);
		}
		hideGeometricInfo();
		cleanup();
	}

	// -------------------------------------------------------------------------
	// Protected helper methods:
	// -------------------------------------------------------------------------

	/**
	 * Helper method to the FeatureTransaction.
	 */
	protected FeatureTransaction getFeatureTransaction() {
		return mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
	}

	// -------------------------------------------------------------------------
	// Private inner classes:
	// -------------------------------------------------------------------------

	/**
	 * Label that displays some basic geometric information, such as length and area.
	 * 
	 * @author Pieter De Graef
	 */
	private class GeometricInfoLabel extends Label {

		private Geometry geometry;

		// Constructors:

		protected GeometricInfoLabel() {
			setParentElement(mapWidget);
			setValign(VerticalAlignment.TOP);
			setShowEdges(true);
			setWidth(145);
			setPadding(3);
			setLeft(mapWidget.getWidth() - 155);
			setTop(-80);
			setBackgroundColor("#FFFFFF");
			setAnimateTime(500);
		}

		// Getters and setters:

		public void setGeometry(Geometry geometry) {
			this.geometry = geometry;
			updateContents();
		}

		// Private methods:

		private void updateContents() {
			String contents = I18nProvider.getMenu().editGeometricInfoLabelTitle();
			if (geometry == null) {
				setContents(contents + I18nProvider.getMenu().editGeometricInfoLabelNoGeometry());
			} else {
				String area = DistanceFormat.asMapArea(mapWidget, geometry.getArea());
				String length = DistanceFormat.asMapLength(mapWidget, geometry.getLength());
				setContents(contents
						+ I18nProvider.getMenu().editGeometricInfoLabelInfo(area, length, geometry.getNumPoints()));
			}
		}
	}

	/**
	 * @author Pieter De Graef
	 */
	private class DestroyLabelInfoOnClick implements ClickHandler {

		public void onClick(ClickEvent event) {
			infoLabel.destroy();
			infoLabel = null;
		}
	}
}
