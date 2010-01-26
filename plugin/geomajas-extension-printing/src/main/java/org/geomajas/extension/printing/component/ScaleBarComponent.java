/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.extension.printing.component;

import com.lowagie.text.Rectangle;
import org.geomajas.configuration.MapInfo;
import org.geomajas.extension.printing.PdfContext;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * ???
 *
 * @author check subversion
 */
@XmlRootElement
public class ScaleBarComponent extends BaseComponent {

	private final Logger log = LoggerFactory.getLogger(ScaleBarComponent.class);

	/**
	 * The unit (meter, mile, degree)
	 */
	private String unit = "units";

	/**
	 * The number of tics for the scalebar
	 */
	private int ticNumber;

	/**
	 * The labels on top of the ticks
	 */
	private List<String> tickLabels = new ArrayList<String>();

	/**
	 * The calculated sizes of the ticks
	 */
	private List<Rectangle> tickSizes = new ArrayList<Rectangle>();

	/**
	 * The label font
	 */
	private Font font = new Font("Dialog", Font.PLAIN, 12);

	/**
	 * Width a tic (double checkerboard style).
	 */
	private float ticWidth;

	/**
	 * Height of a tic (double checkerboard style).
	 */
	private float ticHeight;

	private static final float[] ALLOW_TICK_NUMBERS = new float[] {1, 2, 5, 10, 25, 50, 100, 200, 250, 500, 750};

	private static final String[] UNIT_PREFIXES = new String[] {"n", "m", "", "k", "M"};

	public ScaleBarComponent() {
		getConstraint().setAlignmentX(LayoutConstraint.LEFT);
		getConstraint().setAlignmentY(LayoutConstraint.BOTTOM);
		getConstraint().setMarginX(20);
		getConstraint().setMarginY(20);
		getConstraint().setWidth(200);
	}

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void calculateSize(PdfContext context) {
		// the width must be set !!
		float width = getConstraint().getWidth();

		// Calculate width in map units and round.
		float widthInUnits = width / (float) getMap().getPpUnit();

		// Calculate ideal tic width in units
		float ticWidthInUnits = widthInUnits / getTicNumber();

		// Calculate minimal tic width in units
		int ticLog = (int) Math.floor(Math.log10(ticWidthInUnits) / 3);
		float minTicWidthInUnits = (float) Math.pow(10, ticLog * 3);

		// Find the highest nice number
		float nicestNumber = 0;
		for (float allowTickNumber : ALLOW_TICK_NUMBERS) {
			if (minTicWidthInUnits * allowTickNumber > ticWidthInUnits) {
				break;
			} else {
				nicestNumber = allowTickNumber;
			}
		}
		ticWidthInUnits = minTicWidthInUnits * nicestNumber;
		ticWidth = ticWidthInUnits * getMap().getPpUnit();
		ticHeight = ticWidth * 0.3f;
		int ticCount = getTicNumber();

		// Calculate the labels
		MapInfo map = context.getMap(getMap().getId());
		if (map != null) {
			log.debug("calculateSize getMap.getId({}), res {}", getMap().getId(), map);
			CoordinateReferenceSystem crs;
			try {
				crs = CRS.decode(map.getCrs());
				unit = crs.getCoordinateSystem().getAxis(0).getUnit().toString();
			} catch (Exception e) {
				log.error("could not calculate map unit", e);
			}
		}
		font = new Font("Dialog", Font.PLAIN, (int) (0.8 * ticHeight));
		for (int i = 0; i <= ticCount; i++) {
			String label = "" + (int) (i * ticWidthInUnits / Math.pow(10, 3 * ticLog));
			String units = ((ticLog >= -2 && ticLog <= 2) ? UNIT_PREFIXES[ticLog + 2] : "*10^" + (ticLog * 3)) + unit;
			if (i == ticCount) {
				label += " " + units;
			}
			tickLabels.add(label);
			tickSizes.add(context.getTextSize(label, font));
		}

		// Calculate size
		width = ticWidth * ticCount;
		Rectangle first = context.getTextSize(tickLabels.get(0), font);
		Rectangle last = context.getTextSize(tickLabels.get(ticCount), font);
		width += 0.5 * first.getWidth();
		width += 0.5 * last.getWidth();
		float height = ticHeight;
		height += first.getHeight();
		setBounds(new Rectangle(0, 0, width, height));
	}

	@Override
	public void render(PdfContext context) {
		// draw the tics
		float lowX = 0.5f * tickSizes.get(0).getWidth();
		float lowY = 0;
		float highX = lowX;
		float highY = 0.333f * ticHeight;
		Rectangle baseRect = new Rectangle(0, 0, ticWidth, 0.333f * ticHeight);

		// fills
		for (int i = 0; i < ticNumber; i++) {
			if (i % 2 == 0) {
				context.moveRectangleTo(baseRect, lowX, lowY);
				context.fillRectangle(baseRect, Color.white);
				context.strokeRectangle(baseRect, Color.black, 0.5f);
				context.moveRectangleTo(baseRect, highX, highY);
				context.fillRectangle(baseRect, Color.black);
				context.strokeRectangle(baseRect, Color.black, 0.5f);
			} else {
				context.moveRectangleTo(baseRect, highX, highY);
				context.fillRectangle(baseRect, Color.white);
				context.strokeRectangle(baseRect, Color.black, 0.5f);
				context.moveRectangleTo(baseRect, lowX, lowY);
				context.fillRectangle(baseRect, Color.black);
				context.strokeRectangle(baseRect, Color.black, 0.5f);
			}
			lowX += ticWidth;
			highX += ticWidth;
		}

		// tick extensions
		highX = 0.5f * tickSizes.get(0).getWidth();
		highY = 0.6666f * ticHeight;
		for (int i = 0; i <= ticNumber; i++) {
			context.drawRelativePath(new float[] {0, 0}, new float[] {0, 1}, new Rectangle(highX, highY, highX,
					0.75f * ticHeight), Color.black, 0.5f, null);
			highX += ticWidth;
		}

		// position and print the labels
		float labelX = 0.5f * tickSizes.get(0).getWidth();
		float labelY = ticHeight;
		for (int i = 0; i < tickLabels.size(); i++) {
			Rectangle box = tickSizes.get(i);
			// center the label
			context.moveRectangleTo(box, labelX - 0.5f * box.getWidth(), labelY);
			context.drawText(tickLabels.get(i), font, box, Color.black);
			labelX += ticWidth;
		}
	}

	MapComponent getMap() {
		return (MapComponent) getParent();
	}

	public int getTicNumber() {
		return ticNumber;
	}

	public void setTicNumber(int ticNumber) {
		this.ticNumber = ticNumber;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Resets cyclic references like child -> parent relationship.
	 *
	 * @param u
	 * @param parent
	 */
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		setParent((PrintComponent) parent);
	}

}
