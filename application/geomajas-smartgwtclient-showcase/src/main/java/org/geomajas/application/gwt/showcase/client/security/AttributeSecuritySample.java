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

package org.geomajas.application.gwt.showcase.client.security;

import org.geomajas.application.gwt.showcase.client.i18n.ShowcaseMessages;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.widget.FeatureAttributeWindow;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.layer.feature.SearchCriterion;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that tests security on attribute level.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AttributeSecuritySample extends SamplePanel {

	public static final String TITLE = "AttributeSecurity";

	private static final ShowcaseMessages MESSAGES = GWT.create(ShowcaseMessages.class);

	private MapModel mapModel;

	private VectorLayer layer;

	private FeatureAttributeWindow featureAttributeWindow;

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new AttributeSecuritySample();
		}
	};

	@Override
	public Canvas getViewPanel() {
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Create horizontal layout for login buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);
		buttonLayout.setHeight(20);

		// Map with ID duisburgMap is defined in the XML configuration. (mapDuisburg.xml)
		mapModel = new MapModel("mapBeans", "gwtExample");

		Runnable removeAttributeWindow = new Runnable() {
			public void run() {
				if (null != featureAttributeWindow) {
					featureAttributeWindow.destroy();
					featureAttributeWindow = null;
				}
			}
		};
		buttonLayout.addMember(new UserLoginButton("elvis", removeAttributeWindow));
		buttonLayout.addMember(new UserLoginButton("luc", removeAttributeWindow));

		// Set up the search command, that will fetch a feature:
		// Searches for ID=1, but we might as well have created a filter on one of the attributes...
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setBooleanOperator("AND");
		request.setCrs("EPSG:900913"); // Can normally be acquired from the MapModel.
		request.setLayerId("layerBeans");
		request.setMax(1);
		request.setCriteria(new SearchCriterion[] { new SearchCriterion("id", "=", "1") });
		final GwtCommand command = new GwtCommand(SearchFeatureRequest.COMMAND);
		command.setCommandRequest(request);

		// Create a button that executes the search command:
		IButton editFeatureButton = new IButton(MESSAGES.attributeSecurityButtonTitle());
		editFeatureButton.setWidth(200);
		editFeatureButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				layer = (VectorLayer) mapModel.getLayer("clientLayerBeans");
				GwtCommandDispatcher.getInstance().execute(command,
						new AbstractCommandCallback<SearchFeatureResponse>() {

					public void execute(SearchFeatureResponse response) {
						for (org.geomajas.layer.feature.Feature dtoFeature : response.getFeatures()) {
							Feature feature = new Feature(dtoFeature, layer);
							if (null != featureAttributeWindow) {
								featureAttributeWindow.destroy();
								featureAttributeWindow = null;
							}
							featureAttributeWindow = new FeatureAttributeWindow(feature, true);
							featureAttributeWindow.setWidth(400);
							layout.addMember(featureAttributeWindow);
						}
					}
				});
			}
		});

		layout.addMember(buttonLayout);
		layout.addMember(editFeatureButton);
		return layout;
	}

	@Override
	public void onDraw() {
		mapModel.init();
	}

	@Override
	public String getDescription() {
		return MESSAGES.attributeSecurityDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/security.xml",
				"classpath:org/geomajas/gwt/example/layerBeans.xml",
				"classpath:org/geomajas/gwt/example/mapBeans.xml" };
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}
}
