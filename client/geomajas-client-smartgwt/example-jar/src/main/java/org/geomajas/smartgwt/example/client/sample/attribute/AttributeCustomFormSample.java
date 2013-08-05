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

import com.google.gwt.core.client.GWT;
import org.geomajas.smartgwt.client.widget.attribute.DataSourceFieldFactory;
import org.geomajas.smartgwt.client.widget.attribute.FormItemFactory;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.widget.FeatureAttributeEditor;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.attribute.AttributeFormFieldRegistry;
import org.geomajas.smartgwt.client.widget.attribute.FeatureForm;
import org.geomajas.smartgwt.client.widget.attribute.FeatureFormFactory;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows how a custom attribute form can be used for editing features.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AttributeCustomFormSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "AttributeCustomForm";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new AttributeCustomFormSample();
		}
	};

	public Canvas getViewPanel() {
		// Start out by registering a new type in the AttributeFormFieldRegistry. When an attribute is configured with
		// the 'formInputType' equal to "myTextAre", then this type will be used (a text area):
		AttributeFormFieldRegistry.registerCustomFormItem("myTextArea", new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceTextField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				TextAreaItem textAreaItem = new TextAreaItem();
				textAreaItem.setColSpan(4);
				textAreaItem.setHeight(150);
				return textAreaItem;
			}
		}, null);

		final FeatureFormFactory<DynamicForm> myFactory = new FeatureFormFactory<DynamicForm>() {

			public FeatureForm<DynamicForm> createFeatureForm(VectorLayer layer) {
				return new AttributeCustomForm(layer);
			}

		};

		// Continue creating the layout:
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID beansMap is defined in the XML configuration. (contains any type of attribute)
		final MapWidget map = new MapWidget("mapBeansCustomForm", "gwtExample");
		map.setVisible(false);
		layout.addMember(map);
		map.init();

		map.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("beansLayerCustomForm");
				FeatureAttributeEditor editor = new FeatureAttributeEditor(layer, false, myFactory);
				layout.addMember(editor);
			}
		});

		return layout;
	}

	public String getDescription() {
		return MESSAGES.attributeCustomFormDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/context/layerBeansCustomForm.xml",
				"classpath:org/geomajas/smartgwt/example/context/mapBeansCustomForm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
