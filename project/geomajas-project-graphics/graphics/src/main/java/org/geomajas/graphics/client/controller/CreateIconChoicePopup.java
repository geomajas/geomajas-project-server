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
package org.geomajas.graphics.client.controller;

import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.object.GIcon;
import org.geomajas.graphics.client.object.anchor.Anchored;
import org.geomajas.graphics.client.object.anchor.ResizableAnchorer;
import org.geomajas.graphics.client.resource.GraphicsResource;
import org.geomajas.graphics.client.shape.AnchoredImage;
import org.geomajas.graphics.client.shape.MarkerShape;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Popup window with icon and marker choice, used by {@link CreateIconController} and
 * {@link CreateAnchoredIconController}.
 * 
 * @author Jan Venstermans
 * 
 */
public class CreateIconChoicePopup {

	private static final Binder UIBINDER = GWT.create(Binder.class);

	/**
	 * UI binder.
	 * 
	 */
	interface Binder extends UiBinder<PopupPanel, CreateIconChoicePopup> {

	}

	@UiField
	protected PopupPanel popupPanel;

	@UiField
	protected FlowPanel iconsPanel;

	@UiField
	protected SimplePanel markersPanel;

	@UiField
	protected SimplePanel previewPanel;

	@UiField
	protected Button okButton;

	@UiField
	protected HTMLPanel iconChoiceTablePanel;

	private final CreateIconController controller;

	private int imagePerRow = 5;

	private int amountOfMarkers = -1;

	private String iconUrl;

	private MarkerShape markerShape;

	private GIcon previewIcon;

	private DrawingArea previewArea;

	private String noMarkerStyle = "noMarker";

	protected void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
		updatePreview();
	}

	protected void setMarkerShape(MarkerShape markerShape) {
		this.markerShape = markerShape;
		updatePreview();
	}

	public CreateIconChoicePopup(CreateIconController controllerInput, List<String> hrefs) {
		this.controller = controllerInput;
		popupPanel = UIBINDER.createAndBindUi(this);
		iconChoiceTablePanel.setStyleName("iconCreationChoiceTable");
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				controller.createIconInContainer(iconUrl, markerShape);
				hide();
			}

		});

		// set icons choice
		setIconsToChooseFrom(hrefs);

		// markers choice
		DrawingArea drawingArea = new DrawingArea(12 * imagePerRow, 12);
		ClickableMarkerShape marker1 = new ClickableMarkerShape(MarkerShape.SQUARE);
		drawingArea.add(translateMarker(marker1.asVectorObject()));
		ClickableMarkerShape marker2 = new ClickableMarkerShape(MarkerShape.CIRCLE);
		drawingArea.add(translateMarker(marker2.asVectorObject()));
		ClickableMarkerShape marker3 = new ClickableMarkerShape(MarkerShape.CROSS);
		drawingArea.add(translateMarker(marker3.asVectorObject()));
		markersPanel.setWidget(drawingArea);
		drawingArea.getElement().getStyle().setMargin(5, Unit.PX);

		// preview panel
		previewArea = new DrawingArea(30, 100);
		previewPanel.setWidget(previewArea);
	}

	protected void updatePreview() {
		previewArea.clear();
		if (iconUrl != null) {
			previewIcon = new GIcon(new AnchoredImage(0, 0, 16, 16, iconUrl, 0.5, 0.5));
			previewIcon.setPosition(new Coordinate(15, 20));
			if (markerShape != null) {
				previewIcon.addRole(new ResizableAnchorer(new Coordinate(0, 0), markerShape));
				previewIcon.getRole(Anchored.TYPE).setAnchorPosition(new Coordinate(15, 60));
				// previewIcon.getRole(Anchored.TYPE);
			}
			previewArea.add(previewIcon.asObject());
		}
	}

	public void show(int clientX, int clientY) {
		popupPanel.setPopupPosition(clientX, clientY);
		popupPanel.show();
	}

	public void hide() {
		clear();
		popupPanel.hide();
	}

	public void clear() {
		iconUrl = null;
		markerShape = null;
		if (previewArea != null) {
			previewArea.clear();
		}
	}

	// translate so it will be
	private VectorObject translateMarker(VectorObject shape) {
		if (amountOfMarkers % imagePerRow != 0) {
			shape.setTranslation(12 * (amountOfMarkers % imagePerRow), 12 * (amountOfMarkers / imagePerRow));
		}
		return shape;
	}

	protected void raiseMarkerCount() {
		amountOfMarkers++;
	}

	public void setMarkerSectionVisible(boolean visible) {
		if (visible) {
			iconChoiceTablePanel.removeStyleDependentName(noMarkerStyle);
		} else {
			iconChoiceTablePanel.addStyleDependentName(noMarkerStyle);
		}

	}

	public void setIconsToChooseFrom(List<String> urls) {
		clear();
		iconsPanel.clear();
		if (urls == null || urls.isEmpty()) {
			// standard icons choice
			iconsPanel.add(new ClickableIconImage(GWT.getModuleBaseURL() + "image/cloud.png"));
			iconsPanel.add(new ClickableIconImage(GWT.getModuleBaseURL() + "image/sun.jpg"));
		} else {
			for (String url : urls) {
				iconsPanel.add(new ClickableIconImage(url));
			}
		}
	}

	/**
	 * Shows an icon in the popup window.
	 * 
	 * @author Jan Venstermans
	 * 
	 */
	private class ClickableIconImage implements IsWidget, MouseOverHandler, MouseOutHandler {

		private Image iconImage;

		public ClickableIconImage(String iconUrl) {
			iconImage = new Image(iconUrl);
			iconImage.setHeight("12px");
			iconImage.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					setIconUrl(iconImage.getUrl());
				}

			});
			iconImage.addMouseOverHandler(this);
			iconImage.addMouseOutHandler(this);
		}

		public Widget asWidget() {
			return iconImage;
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			iconImage.removeStyleName(GraphicsResource.INSTANCE.css().iconsPanelSelectImage());
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			iconImage.addStyleName(GraphicsResource.INSTANCE.css().iconsPanelSelectImage());
		}
	}

	/**
	 * Shows a marker in the popup window. Contains 2 shape files: simpleShape is the shape that will be drawn in the
	 * popup window; markerShape is a cloneable Shape that will be passed to the Anchor constructor.
	 * 
	 * @author Jan Venstermans
	 * 
	 */
	private class ClickableMarkerShape implements MouseOverHandler, MouseOutHandler, MouseDownHandler {

		private MarkerShape shape;
		
		private Shape simpleShape;
		
		private Rectangle rectangle;
		
		private Group group;

		public ClickableMarkerShape(MarkerShape markerEnum) {
			shape = markerEnum;
			simpleShape = shape.getSimpleShape();
			simpleShape.setFixedSize(true);
			simpleShape.setFillColor("#FF6600");
			simpleShape.setStrokeColor("#FF6600");
			simpleShape.setFillOpacity(0.7);
			
			rectangle = new Rectangle(2, 2, 8, 8);
			rectangle.setStrokeOpacity(0);
			rectangle.setFillOpacity(0);
			simpleShape.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// Bbox bounds = getBoxOfSimpleShape();
					// Coordinate coord = new Coordinate(event.getClientX(),event.getClientY());
					// System.out.println(bounds.toString() + " " +coord.toString());
					setMarkerShape(shape);
				}

			});
			simpleShape.addMouseOverHandler(this);
			simpleShape.addMouseOutHandler(this);
			simpleShape.getElement().getStyle().setCursor(Cursor.POINTER);
			
			group = new Group();
			group.add(rectangle);
//			group.add(simpleShape);
			raiseMarkerCount();
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			simpleShape.setStrokeColor("#FF6600");
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			simpleShape.setStrokeColor("black");
		}

		public VectorObject asVectorObject() {
			return simpleShape;
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			// TODO: make it work: clicking bounding box of simple marker selects this marker
			// this should be alternative for Click event
			Coordinate coord = new Coordinate(event.getClientX(), event.getClientY());
			if (BboxService.contains(getBoxOfSimpleShape(), coord)) {
				setMarkerShape(markerShape);
			}

		}

		private Bbox getBoxOfSimpleShape() {
			return new Bbox(simpleShape.getAbsoluteLeft(), simpleShape.getAbsoluteTop(),
					simpleShape.getParent().getOffsetWidth(), simpleShape.getParent().getOffsetHeight());
		}
	}
}
