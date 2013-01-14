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

package org.geomajas.plugin.editing.gwt.example.client.widget;

import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.plugin.editing.client.snap.SnapAlgorithm;
import org.geomajas.plugin.editing.client.snap.SnapSourceProvider;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestEdgeOfIntersection;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestEdgeSnapAlgorithm;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestVertexOfIntersection;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestVertexSnapAlgorithm;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.snap.VectorLayerSourceProvider;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Window for snapping options.
 * 
 * @author Pieter De Graef
 */
public class SnappingOptionWindow extends Window {

	private final GeometryEditor editor;

	private final SnapSourceProvider sourceProvider;

	private final DynamicForm enableForm;

	private final DynamicForm options;

	public SnappingOptionWindow(final GeometryEditor editor) {
		super();
		this.editor = editor;
		sourceProvider = new VectorLayerSourceProvider(editor.getMapWidget().getMapModel()
				.getVectorLayer("clientLayerGepCountries"));

		setHeaderControls(HeaderControls.HEADER_ICON, HeaderControls.HEADER_LABEL, HeaderControls.CLOSE_BUTTON);
		setAutoSize(true);
		setTitle("Snapping options");
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		enableForm = new DynamicForm();
		options = new DynamicForm();
		options.setIsGroup(true);
		options.setGroupTitle("Options");
		options.setTitleWidth(100);

		final IButton submit = new IButton();
		submit.setTitle("Ok");
		submit.setLayoutAlign(Alignment.RIGHT);
		submit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onSave();
			}
		});

		final CheckboxItem enableItem = new CheckboxItem("enable", "Enable snapping to the Countries layer");
		enableItem.setValue(false);
		enableItem.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				options.setDisabled(enableItem.getValueAsBoolean());
				submit.setDisabled(enableItem.getValueAsBoolean());
				editor.setSnapOnDrag(!enableItem.getValueAsBoolean());
				editor.setSnapOnInsert(!enableItem.getValueAsBoolean());
			}
		});

		final SliderItem distanceItem = new SliderItem("distance", "Distance (km)");
		distanceItem.setWidth(500);
		distanceItem.setMinValue(0);
		distanceItem.setMaxValue(1000);
		distanceItem.setValue(500);

		RadioGroupItem typeItem = new RadioGroupItem("type");
		typeItem.setValueMap("Snap to end-points only", "Snap to end-points and edges");
		typeItem.setWidth("400");
		typeItem.setValue("Snap to end-points only");

		CheckboxItem intersectItem = new CheckboxItem("intersect", "Snap only to intersecting geometries");
		intersectItem.setValue(false);

		enableForm.setFields(enableItem);
		options.setFields(distanceItem, typeItem, intersectItem);

		VLayout layout = new VLayout(10);
		layout.setPadding(10);
		layout.addMember(enableForm);
		layout.addMember(options);
		layout.addMember(submit);
		addItem(layout);
	}

	@Override
	protected void onDraw() {
		super.onDraw();
		onShow();
		centerInPage();
	}

	private void onShow() {
		enableForm.getItem("enable").setValue(editor.isSnapOnDrag());
		options.setDisabled(!editor.isSnapOnDrag());
	}

	private void onSave() {
		float distance = (Integer) options.getItem("distance").getValue();
		String type = (String) options.getItem("type").getValue();
		boolean intersect = (Boolean) options.getItem("intersect").getValue();

		SnapAlgorithm algorithm;
		if (intersect) {
			if (type.indexOf("edge") > 0) {
				algorithm = new NearestEdgeOfIntersection();
			} else {
				algorithm = new NearestVertexOfIntersection();
			}
		} else {
			if (type.indexOf("edge") > 0) {
				algorithm = new NearestEdgeSnapAlgorithm();
			} else {
				algorithm = new NearestVertexSnapAlgorithm();
			}
		}

		editor.getSnappingService().clearSnappingRules();
		editor.getSnappingService().addSnappingRule(algorithm, sourceProvider, distance * 1000, true);
		if (editor.isBusyEditing()) {
			Bbox bounds = editor.getMapWidget().getMapModel().getMapView().getBounds();
			editor.getSnappingService()
					.update(new org.geomajas.geometry.Bbox(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds
							.getHeight()));
		}
		hide();
	}
}