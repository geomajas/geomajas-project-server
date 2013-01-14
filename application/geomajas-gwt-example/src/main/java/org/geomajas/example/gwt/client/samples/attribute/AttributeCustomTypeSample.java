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
import org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry;
import org.geomajas.gwt.client.widget.attribute.DataSourceFieldFactory;
import org.geomajas.gwt.client.widget.attribute.FormItemFactory;

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
		final MapWidget map = new MapWidget("mapBeansCustomType", "gwt-samples");
		map.setVisible(false);
		layout.addMember(map);
		map.init();

		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("beansLayerCustomType");
				FeatureAttributeEditor editor = new FeatureAttributeEditor(layer, false);
				editor.setWidth(400);
				layout.addMember(editor);
			}
		});

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().attributeCustomTypeDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/attribute/AttributeCustomTypeSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerBeansCustomType.xml", "WEB-INF/mapBeansCustomType.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
