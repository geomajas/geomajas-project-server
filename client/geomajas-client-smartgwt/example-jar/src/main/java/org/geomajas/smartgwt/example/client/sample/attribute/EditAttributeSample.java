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

package org.geomajas.smartgwt.example.client.sample.attribute;

import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.widget.FeatureAttributeEditor;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows an attribute form.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class EditAttributeSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "EditAttribute";

	private FeatureAttributeEditor editor;

	private MapModel mapModel;

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new EditAttributeSample();
		}
	};

	public Canvas getViewPanel() {
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID beansMap is defined in the XML configuration. (contains any type of attribute)
		mapModel = new MapModel("mapBeansAssociation", "gwtExample");

		HLayout hLayout = new HLayout(10);
		hLayout.setHeight(40);
		IButton disabledBtn = new IButton("Disable form");
		disabledBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				editor.setDisabled(true);
			}
		});
		hLayout.addMember(disabledBtn);
		IButton enabledBtn = new IButton("Enable form");
		enabledBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				editor.setDisabled(false);
			}
		});
		hLayout.addMember(enabledBtn);
		layout.addMember(hLayout);

		mapModel.runWhenInitialized(new Runnable() {

			public void run() {
				VectorLayer layer = (VectorLayer) mapModel.getLayer("beansAssociationLayer");
				editor = new FeatureAttributeEditor(layer, false);
				editor.setFeature(layer.getFeatureStore().newFeature());
				editor.setWidth(400);
				layout.addMember(editor);
			}
		});

		return layout;
	}
	
	public void onDraw() {
		mapModel.init();
	}


	public String getDescription() {
		return MESSAGES.editAttributeDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/context/layerBeans.xml",
				"classpath:org/geomajas/smartgwt/example/context/mapBeans.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
