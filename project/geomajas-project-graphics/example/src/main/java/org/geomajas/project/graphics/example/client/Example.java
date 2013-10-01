/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.project.graphics.example.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geomajas.graphics.client.action.AddTextAsAnchorAction;
import org.geomajas.graphics.client.action.BringToFrontAction;
import org.geomajas.graphics.client.action.DeleteAction;
import org.geomajas.graphics.client.action.DuplicateAction;
import org.geomajas.graphics.client.action.ToggleLabelAction;
import org.geomajas.graphics.client.controller.AnchorControllerFactory;
import org.geomajas.graphics.client.controller.CreateAnchoredIconController;
import org.geomajas.graphics.client.controller.CreateAnchoredTextAreaController;
import org.geomajas.graphics.client.controller.CreateAnchoredTextController;
import org.geomajas.graphics.client.controller.CreateEllipseController;
import org.geomajas.graphics.client.controller.CreateIconController;
import org.geomajas.graphics.client.controller.CreateImageController;
import org.geomajas.graphics.client.controller.CreateLineWithTemplateLabeledController;
import org.geomajas.graphics.client.controller.CreatePathController;
import org.geomajas.graphics.client.controller.CreateRectangleController;
import org.geomajas.graphics.client.controller.CreateTextAreaHtmlController;
import org.geomajas.graphics.client.controller.CreateTextController;
import org.geomajas.graphics.client.controller.DeleteControllerFactory;
import org.geomajas.graphics.client.controller.DragControllerFactory;
import org.geomajas.graphics.client.controller.ExternalizableLabeledControllerFactory;
import org.geomajas.graphics.client.controller.LabelControllerFactory;
import org.geomajas.graphics.client.controller.PopupMenuControllerFactory;
import org.geomajas.graphics.client.controller.PopupMenuFactory;
import org.geomajas.graphics.client.controller.ResizeControllerFactory;
import org.geomajas.graphics.client.editor.AnchorStyleEditor;
import org.geomajas.graphics.client.editor.LabelEditor;
import org.geomajas.graphics.client.editor.StrokeFillEditor;
import org.geomajas.graphics.client.editor.TemplateLabelEditor;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.ActionType;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.Handler;
import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.service.GraphicsServiceImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * Example application.
 * 
 * @author Jan De Moerloose
 * 
 */
public class Example implements EntryPoint, Handler {

	private FlowPanel buttonPanel;
	
	private GraphicsService service;
	
	private VerticalPanel iconChoicePanel;
	
	private CheckBox checkShowDrag;
	
	private CheckBox checkExternalLabel;
	
	private List<String> urls = new ArrayList<String>(Arrays.asList(GWT.getModuleBaseURL() + "image/slider.gif",
			GWT.getModuleBaseURL() + "image/cloud.png",
			GWT.getModuleBaseURL() + "image/sun.jpg"));
	
	private List<String> url = new ArrayList<String>(Arrays.asList(urls.get(0)));
	
	private PopupMenuControllerFactory popupFactory;

	@Override
	public void onModuleLoad() {
		SimpleEventBus bus = new SimpleEventBus();
		final TestContainer rc = new TestContainer(bus);
		service = new GraphicsServiceImpl(bus, true, true);
		service.setObjectContainer(rc);
		service.getObjectContainer().addGraphicsObjectContainerHandler(this);
		
		//functionalities
		popupFactory = new PopupMenuControllerFactory(new PopupMenuFactory());
		registerControllerFactories();
		registerPopupFactoryActionsAndEditiors();		
		
		//layout
		DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		buttonPanel = new FlowPanel();
		createButtonPanel(rc);
		buttonPanel.getElement().getStyle().setBackgroundColor("grey");
		dock.addWest(buttonPanel, 100);
		dock.add(rc);
		RootLayoutPanel.get().add(dock);
		
		service.start();
	}
	
	private void registerControllerFactories() {
		service.registerControllerFactory(new ResizeControllerFactory());
		service.registerControllerFactory(new DragControllerFactory());
		service.registerControllerFactory(new DeleteControllerFactory());
		service.registerControllerFactory(new LabelControllerFactory());
		service.registerControllerFactory(new ExternalizableLabeledControllerFactory());
		service.registerControllerFactory(new AnchorControllerFactory());
		service.registerControllerFactory(popupFactory);
	}
	
	private void registerPopupFactoryActionsAndEditiors() {
		popupFactory.registerAction(new DeleteAction());
		popupFactory.registerEditor(new LabelEditor());
		popupFactory.registerEditor(new StrokeFillEditor());
		popupFactory.registerAction(new DuplicateAction());
		popupFactory.registerAction(new BringToFrontAction());
		popupFactory.registerEditor(new AnchorStyleEditor());
		popupFactory.registerAction(new AddTextAsAnchorAction());
		popupFactory.registerAction(new ToggleLabelAction());
		popupFactory.registerEditor(new TemplateLabelEditor());
	}
	
	private void createButtonPanel(final TestContainer rc) {
		// create extra elements
		createCheckShowDrag();
		createCheckExternalLabel();
		CreateIconController createIconController = new CreateIconController(service, 16, 16, url);
		CreateAnchoredIconController createAnchoredIconController 
			= new CreateAnchoredIconController(service, 16,	16, url);
		createIconChoicePanel(createIconController, createAnchoredIconController);
		
		//buttons for creation of objects
		addCheckbox(checkShowDrag);
		addCheckbox(checkExternalLabel);
		addControllerButton(new CreateTextController(service), "text");
		addControllerButton(new CreateTextAreaHtmlController(service, 100, 70), "textarea");
		addControllerButton(new CreateAnchoredTextController(service), "anchored text");
		addControllerButton(new CreateAnchoredTextAreaController(service, 100, 70), "anchored textarea");
		addControllerButton(new CreateRectangleController(service), "rectangle");
		addControllerButton(new CreateEllipseController(service), "ellipse");
		addControllerButton(new CreateImageController(service, 200, 235,
				"http://tuxpaint.org/stamps/stamps/animals/birds/cartoon/tux.png"), "image");
		addControllerButton(new CreatePathController(service, false), "line");
		addControllerButton(new CreatePathController(service, true), "polygon");
		addControllerButton(new NavigationController(service, rc.getRootContainer()), "navigation");
		addControllerButton(new CreateLineWithTemplateLabeledController(service), "line with template label");
		addControllerButton(createIconController, "icon");
		addControllerButton(createAnchoredIconController, "anchored icon");
		buttonPanel.add(iconChoicePanel);
	}

	private void addControllerButton(final GraphicsController controller, String name) {
		buttonPanel.add(new ControllerButton(controller, name));
	}
	
	private void addCheckbox(final CheckBox checkbox) {
		buttonPanel.add(new SimplePanel(checkbox));
	}
	
	//checkbox for showing original object when dragging
	private void createCheckShowDrag() {
		checkShowDrag = new CheckBox();
		checkShowDrag.setValue(service.isShowOriginalObjectWhileDragging());
		checkShowDrag.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.setShowOriginalObjectWhileDragging(checkShowDrag.getValue());
			}
			
		});
		checkShowDrag.setText("duplicate on drag");
	}
	
	//checkbox for showing original object when dragging
	private void createCheckExternalLabel() {
		checkExternalLabel = new CheckBox();
		checkExternalLabel.setValue(service.isExternalizableLabeledOriginallyExternal());
		checkExternalLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.setExternalizableLabeledOriginallyExternal(checkExternalLabel.getValue());
			}
			
		});
		checkExternalLabel.setText("label external on creation");
	}
	
	private void createIconChoicePanel(final CreateIconController createIconController, 
			final CreateAnchoredIconController createAnchoredIconController) {
		iconChoicePanel = new VerticalPanel();
		RadioButton rb0 = new RadioButton("myRadioGroup", "No icon: sets default");
		rb0.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createIconController.setHrefs(null);
				createAnchoredIconController.setHrefs(null);
			}
		});
		RadioButton rb1 = new RadioButton("myRadioGroup", "1 icon");
		rb1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createIconController.setHrefs(url);
				createAnchoredIconController.setHrefs(url);
			}
		});
		RadioButton rb2 = new RadioButton("myRadioGroup", "multiple icons");
		rb2.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createIconController.setHrefs(urls);
				createAnchoredIconController.setHrefs(urls);
			}
		});
		iconChoicePanel.add(new Label("Change nr of icons in icon choice list:"));
		iconChoicePanel.add(rb0);
		iconChoicePanel.add(rb1);
		iconChoicePanel.add(rb2);
		iconChoicePanel.setVisible(false);
		rb1.setValue(true);
	}
	
	

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (event.getActionType() != ActionType.UPDATE) {
			for (int i = 0; i < buttonPanel.getWidgetCount(); i++) {
				if (buttonPanel.getWidget(i) instanceof ControllerButton) {
					((ControllerButton) buttonPanel.getWidget(i)).setControllerActive(false);
					service.getMetaController().setActive(true);
				}
			}
		}

	}

	/**
	 * 
	 */
	public class ControllerButton extends ToggleButton {

		private GraphicsController controller;

		public ControllerButton(final GraphicsController controller, String upText) {
			super(upText);
			this.controller = controller;
			addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					iconChoicePanel.setVisible(false);
					setControllerActive(!controller.isActive());
				}
			});
		}

		public void setControllerActive(boolean active) {
			setDown(active);
			controller.setActive(active);
			if (active) {
				service.getMetaController().setActive(false);
				if (controller instanceof CreateAnchoredIconController || controller instanceof CreateIconController) {
					iconChoicePanel.setVisible(true);
				}
			}
		}

	}
}
