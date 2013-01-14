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

package org.geomajas.gwt.example.client.sample.attribute;

import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureAttributeEditor;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry;
import org.geomajas.gwt.client.widget.attribute.DataSourceFieldFactory;
import org.geomajas.gwt.client.widget.attribute.FormItemFactory;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows an attribute form that uses a custom type called 'myType'.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AttributeCustomTypeSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "AttributeCustomType";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new AttributeCustomTypeSample();
		}
	};

	public Canvas getViewPanel() {
		// @extract-start CustomAttributeFormFieldRegistry, CustomAttributeFormFieldRegistry
		// We define the custom type "myType" in the AttributeFormItemFactory:
		AttributeFormFieldRegistry.registerCustomFormItem("myType", new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceIntegerField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new SliderItem();
			}
		}, null);
		// @extract-end

		// Now we continue as usual:
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID beansMap is defined in the XML configuration. (contains any type of attribute)
		final MapWidget map = new MapWidget("mapBeansCustomType", "gwtExample");
		map.setVisible(false);
		layout.addMember(map);
		map.init();

		map.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("beansLayerCustomType");
				FeatureAttributeEditor editor = new FeatureAttributeEditor(layer, false);
				editor.setWidth(400);
				layout.addMember(editor);
			}
		});

		return layout;
	}

	public String getDescription() {
		return MESSAGES.attributeCustomTypeDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/layerBeansCustomType.xml",
				"classpath:org/geomajas/gwt/example/context/mapBeansCustomType.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
