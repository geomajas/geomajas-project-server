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
package org.geomajas.sld.editor.client;

import java.util.List;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.client.SldGwtService;
import org.geomajas.sld.client.SldGwtServiceAsync;
import org.geomajas.sld.editor.client.widget.RefreshSldHandler;
import org.geomajas.sld.editor.client.widget.RefuseSldLoadingHandler;
import org.geomajas.sld.editor.client.widget.SldManager;
import org.geomajas.sld.editor.client.widget.SldWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point of SmartGWT SLD editor.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 * 
 */
public class SldEditorEntryPoint implements EntryPoint {

	private HLayout mainLayout;

	private VLayout vLayoutLeft;

	private SldWidget sldWidget;

	private Canvas canvasForSLD;

	private SldGwtServiceAsync service;

	private SldManager sldSelector;

	private VLayout vLayoutRight;

	private String selectedSLDName;

	public void onModuleLoad() {
		// Add it to the root panel.
		RootPanel.get().add(getViewPanel());
	}

	public Canvas getViewPanel() {

		Cookies.setCookie("skin_name", "Enterprise");

		mainLayout = new HLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// mainLayout.setMinWidth(300);
		// mainLayout.setMinHeight(400);

		vLayoutLeft = new VLayout();
		vLayoutLeft.setWidth("30%");
		vLayoutLeft.setHeight100();
		vLayoutLeft.setMembersMargin(5);
		vLayoutLeft.setMargin(5);
		vLayoutLeft.setShowResizeBar(true);
		vLayoutLeft.setMinWidth(150);

		mainLayout.addMember(vLayoutLeft);

		service = GWT.create(SldGwtService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + "d/sld");

		// Select Control to select an SLD from the list of sld's made available
		// by the server
		sldSelector = new SldManager(service);
		vLayoutLeft.addMember(sldSelector.getCanvas());

		sldWidget = new SldWidget(service);

		sldWidget.addRefuseSldLoadingHandler(new RefuseSldLoadingHandler() {

			public void execute(String refusedSldName, String currentSldName) {

				sldSelector.selectSld(currentSldName, true/* do not reload current SLD */);

			}

		});

		// formSelectLayer.setItems(selectSLD);

		vLayoutRight = new VLayout();
		vLayoutRight.setWidth("70%");
		vLayoutRight.setHeight100();
		vLayoutRight.setMembersMargin(5);
		vLayoutRight.setMargin(5);
		vLayoutRight.setShowResizeBar(true);
		vLayoutRight.setMinWidth(150);

		mainLayout.addMember(vLayoutRight);

		/** Populate select list of Select SLD control **/
		service.findAll(new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> result) {

				GWT.log("got " + result.size() + " SLDs");

				sldSelector.setData(result);
			}

			public void onFailure(Throwable caught) {
				GWT.log("could not access SLDs", caught);
			}
		});

		/** Add ChangedHandler for Select SLD control **/
		sldSelector.addSelectionChangedHandler(new SelectionChangedHandler() {

			public void onSelectionChanged(SelectionEvent event) {
				if (sldSelector.isSelectAfterLoaded()) {
					return; /* ABORT */
				}
				ListGridRecord record = event.getSelectedRecord();

				if (record == null) {
					if (null != canvasForSLD) {
						sldWidget.clear(true);
						//canvasForSLD.disable(); // TODO: needed?
					}
					vLayoutRight.markForRedraw();
				} else {

					selectedSLDName = (String) record.getAttribute(SldManager.SLD_NAME_ATTRIBUTE_NAME);

					service.findByName(selectedSLDName, new AsyncCallback<StyledLayerDescriptorInfo>() {

						/** call-back for handling service.findByName() success return **/
						public void onSuccess(StyledLayerDescriptorInfo sld) {
							if (!sld.getChoiceList().isEmpty()) {

								if (null == canvasForSLD) {
									canvasForSLD = sldWidget.getCanvasForSLD(sld);

									sldWidget.addRefreshHandler(new RefreshSldHandler() {

										public void execute(String sldName) {
											service.findByName(sldName,
													new AsyncCallback<StyledLayerDescriptorInfo>() {

														public void onSuccess(StyledLayerDescriptorInfo sld) {
															sldWidget.getCanvasForSLD(sld);
														}

														public void onFailure(Throwable caught) {
															SC.warn("De SLD " + selectedSLDName
																	+ " kan niet gevonden worden. Interne fout: "
																	+ caught.getMessage());
															GWT.log("could not access SLD " + selectedSLDName, caught);
														}
													});
										}
									});
									vLayoutRight.addMember(canvasForSLD);
								} else {
									sldWidget.getCanvasForSLD(sld);
									vLayoutRight.markForRedraw();
								}
								if (null != canvasForSLD) {
									canvasForSLD.enable(); // TODO: needed?
								}
							}
						}

						public void onFailure(Throwable caught) {
							GWT.log("could not access SLDs", caught);
						}
					});
				}
			}

		});

		return mainLayout;
	}

}
