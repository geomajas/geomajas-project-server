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
package org.geomajas.graphics.client.object.labeler;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableAwareRole;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.shape.AnchoredText;
import org.geomajas.graphics.client.util.BboxPosition;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Text;

/**
 * Implementation of {@link Labeler} role for multiline labels, for {@link Resizable} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizableMultiLineLabeler extends ResizableLabeler {
	
	private String textString;

	private String[] textSplit;

	private AnchoredText[] textObjects;

	private Group textGroup;
	
	private static final int INTERLINE = 20;
	
	private static final int MAX_LINES = 10;
	
	private int totalWidth;
	
	private BboxPosition userUlPosition;

	public ResizableMultiLineLabeler() {
		this("", BboxPosition.CORNER_UL);
	}

	public ResizableMultiLineLabeler(String label,  BboxPosition screenUpperLeftPositionInUserSpace) {
		super(label);
		this.userUlPosition = screenUpperLeftPositionInUserSpace;
		this.textString = label;
		textObjects = new AnchoredText[MAX_LINES];
		textGroup = new Group();
		for (int i = 0; i < MAX_LINES; i++) {
			textObjects[i] = new AnchoredText(0, 0, "", 0, 0);
			textObjects[i].setFillColor("black");
			textObjects[i].setStrokeWidth(0);
			textGroup.add(textObjects[i]);
		}
		getInternalLabel().setVisible(false);
	}
	
	@Override
	public void setResizable(Resizable resizable) {
		super.setResizable(resizable);
		splitTextAccordingToWidth();
	}

	@Override
	public VectorObject asObject() {
		return textGroup;
	}
	
	@Override
	public void onUpdate() {
		splitTextAccordingToWidth();
	}

	@Override
	public void setLabel(String label) {
		this.textString = label;
		splitTextAccordingToWidth();
	}

	@Override
	public String getLabel() {
		return textString;
	}

	@Override
	public Labeled asRole() {
		return this;
	}

	@Override
	public RoleType<Labeled> getType() {
		return Labeled.TYPE;
	}

	@Override
	public ResizableAwareRole<Labeled> cloneRole(Resizable resizable) {
		ResizableMultiLineLabeler clone = new ResizableMultiLineLabeler(textString, userUlPosition);
		clone.setResizable(resizable);
		return clone;
	}
	
	private void splitTextAccordingToWidth() {
		textSplit = textString.split(" ");
		Coordinate resStartPosition = getResizabel().getPosition();
		
		double resUserWidth = getResizabel().getUserBounds().getWidth();
		// possibly correct for width sign
		if (userUlPosition.equals(BboxPosition.CORNER_LR) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
			resUserWidth = -resUserWidth;
		}

		double resUserHeigth = getResizabel().getUserBounds().getHeight();
		if (userUlPosition.equals(BboxPosition.CORNER_UL) || userUlPosition.equals(BboxPosition.CORNER_UR)) {
			resUserHeigth = -resUserHeigth;
		}

		double resWidth = getResizabel().getBounds().getWidth();

		double userHeight = textObjects[0].getUserHeight();
		double userInterline = INTERLINE * userHeight / textObjects[0].getTextHeight();
		
		int splitCount = 0;
		totalWidth = 0;
		for (int i = 0; i < MAX_LINES; i++) {
			textObjects[i].setUserX(resStartPosition.getX());
			textObjects[i].setUserY(resStartPosition.getY() + i * userInterline + userHeight);
			int restOfWords = textSplit.length - splitCount;
			if (restOfWords <= 0) {
				textObjects[i].setText("");
				continue;
			}
			textObjects[i].setText(textSplit[splitCount]);
			if (restOfWords > 1) {
				for (int j = 1; j < restOfWords; j++) {
					String before = textObjects[i].getText();
					textObjects[i].setText(before + " " + textSplit[splitCount + 1]);
					if (textObjects[i].getTextWidth() > resWidth) {
						textObjects[i].setText(before);
						break;
					} else {
						splitCount++;
					}
				}
			}
			if (textObjects[i].getTextWidth() > totalWidth) {
				totalWidth = textObjects[i].getTextWidth();
			}
			splitCount++;
		}
		giveBoundariesToResizabel();
	}

	private void giveBoundariesToResizabel() {
		// determine total height
		int height = textObjects[0].getTextHeight();
		int numberOfLines = 0;
		for (Text text : textObjects) {
			if (("").equals(text.getText())) {
				break;
			}
			numberOfLines++;
		}
		int totalHeight = numberOfLines * (height + INTERLINE);
		
		// transform height and width to userSpace
		Bbox resizableScreenBounds = getResizabel().getBounds();
		Bbox resizableUserBounds = getResizabel().getUserBounds();
		boolean resizableToLow = false;
//		boolean resizableToLow = totalHeight > Math.abs(resizableScreenBounds.getHeight());
//		boolean resizableToSmall = totalWidth > Math.abs(resizableScreenBounds.getWidth());
//		if (resizableToLow || resizableToSmall) {
//			if (resizableToLow) {
//				resizableScreenBounds.setHeight(totalHeight);
//			}
//			if (resizableToSmall) {
//				resizableUserBounds.setWidth(resizableUserBounds.getWidth() * totalWidth
//						/ resizableScreenBounds.getWidth());
//			}
//			getResizabel().setUserBounds(resizableUserBounds);
//		}
	}
	
	// font properties
	@Override
	public void setFontSize(int size) {
		for (Text line : textObjects) {
			line.setFontSize(size);
		}
		splitTextAccordingToWidth();
	}

	@Override
	public int getFontSize() {
		return textObjects[0].getFontSize();
	}

	@Override
	public void setFontFamily(String font) {
		for (Text line : textObjects) {
			line.setFontFamily(font);
		}
		splitTextAccordingToWidth();
	}

	@Override
	public String getFontFamily() {
		return textObjects[0].getFontFamily();
	}

	@Override
	public void setFontColor(String color) {
		for (Text line : textObjects) {
			line.setFillColor(color);
		}
		splitTextAccordingToWidth();
	}

	@Override
	public String getFontColor() {
		return textObjects[0].getFillColor();
	}
}
