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

package org.geomajas.example.gwt.client.samples.attribute;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureAttributeEditor;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows an attribute form, thereby testing the "includeInForm=false" for the integer attribute.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AttributeIncludeInFormSample extends SamplePanel {

	public static final String TITLE = "AttributeIncludeInForm";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new AttributeIncludeInFormSample();
		}
	};

	public Canvas getViewPanel() {
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID beansMap is defined in the XML configuration. (contains any type of attribute)
		final MapWidget map = new MapWidget("mapBeansIncludeInForm", "gwt-samples");
		map.setVisible(false);
		layout.addMember(map);
		map.init();

		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("beansLayerIncludeInForm");
				FeatureAttributeEditor editor = new FeatureAttributeEditor(layer, false);
				editor.setWidth(400);
				layout.addMember(editor);
			}
		});

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().attributeIncludeInFormDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/attribute/AttributeIncludeInFormSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerBeansIncludeInForm.xml", "WEB-INF/mapBeansIncludeInForm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
