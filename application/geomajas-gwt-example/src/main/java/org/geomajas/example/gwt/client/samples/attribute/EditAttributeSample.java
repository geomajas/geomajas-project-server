/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.example.gwt.client.samples.attribute;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureAttributeEditor;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.layer.feature.SearchCriterion;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows an attribute form.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class EditAttributeSample extends SamplePanel {

	public static final String TITLE = "EditAttribute";

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
		final MapWidget map = new MapWidget("mapBeansAssociation", "gwt-samples");
		map.setVisible(false);
		layout.addMember(map);
		map.init();

		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("beansAssociationLayer");
				FeatureAttributeEditor editor = new FeatureAttributeEditor(layer, false);
				editor.setWidth(400);
				layout.addMember(editor);

				
//				SearchFeatureRequest request = new SearchFeatureRequest();
//				request.setBooleanOperator("AND");
//				request.setCrs("EPSG:900913"); // Can normally be acquired from the MapModel.
//				request.setLayerId("beansAssociationLayer");
//				request.setMax(1);
//				request.setCriteria(new SearchCriterion[] { new SearchCriterion("id", "=", "1") });
//				final GwtCommand command = new GwtCommand("command.feature.Search");
//				command.setCommandRequest(request);
//				GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
//
//					public void execute(CommandResponse response) {
//						if (response instanceof SearchFeatureResponse) {
//							VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("beansAssociationLayer");
//							SearchFeatureResponse resp = (SearchFeatureResponse) response;
//							for (org.geomajas.layer.feature.Feature dtoFeature : resp.getFeatures()) {
//								Feature feature = new Feature(dtoFeature, layer);
//								FeatureAttributeEditor editor = new FeatureAttributeEditor(layer, false);
//								layout.addMember(editor);
//							}
//						}
//					}
//				});
			}
		});

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().editAttributeDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/attribute/EditAttributeSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerBeans.xml", "WEB-INF/mapBeans.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
