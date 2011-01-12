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
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.ScaleBarComponent;
import org.geomajas.plugin.printing.component.dto.ScaleBarComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.parser.FontConverter;
import org.geomajas.service.GeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Scale bar component for inclusion in printed documents.
 * 
 * @author Jan De Moerloose
 */
@Component("ScaleBarComponentPrototype")
@Scope(value = "prototype")
public class ScaleBarComponentImpl extends PrintComponentImpl<ScaleBarComponentInfo> implements ScaleBarComponent {

	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(ScaleBarComponentImpl.class);

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
	@XStreamConverter(FontConverter.class)
	private Font font = new Font("Dialog", Font.PLAIN, 12);

	/**
	 * Width a tic (double checkerboard style).
	 */
	private float ticWidth;

	/**
	 * Height of a tic (double checkerboard style).
	 */
	private float ticHeight;

	@Autowired
	@XStreamOmitField
	private PrintConfigurationService configurationService;
	
	@Autowired
	@XStreamOmitField
	private GeoService geoService;

	@Autowired
	@XStreamOmitField
	private PrintDtoConverterService converterService;

	private static final float[] ALLOW_TICK_NUMBERS = new float[] { 1, 2, 5, 10, 25, 50, 100, 200, 250, 500, 750 };

	private static final String[] UNIT_PREFIXES = new String[] { "n", "m", "", "k", "M" };

	public ScaleBarComponentImpl() {
		getConstraint().setAlignmentX(LayoutConstraint.LEFT);
		getConstraint().setAlignmentY(LayoutConstraint.BOTTOM);
		getConstraint().setMarginX(20);
		getConstraint().setMarginY(20);
		getConstraint().setWidth(200);
	}	
	
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void calculateSize(PdfContext context) {
		// the width must be set !!
		float width = getConstraint().getWidth();

		// Calculate width in map units and round.
		float widthInUnits = width / getMap().getPpUnit();

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
		ClientMapInfo map = configurationService.getMapInfo(getMap().getMapId(), getMap().getApplicationId());
		if (map != null) {
			log.debug("calculateSize getMap.getId({}), res {}", getMap().getId(), map);
			Crs crs;
			try {
				crs = geoService.getCrs2(map.getCrs());
				unit = crs.getCoordinateSystem().getAxis(0).getUnit().toString();
			} catch (Exception e) {
				log.error("could not calculate map unit", e);
			}
		}
//		font = new Font("Dialog", Font.PLAIN, (int) (0.8 * ticHeight));
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
			context.drawRelativePath(new float[] { 0, 0 }, new float[] { 0, 1 }, new Rectangle(highX, highY, highX,
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

	MapComponentImpl getMap() {
		return (MapComponentImpl) getParent();
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.ScaleBarComponent#getTicNumber()
	 */
	public int getTicNumber() {
		return ticNumber;
	}

	public void setTicNumber(int ticNumber) {
		this.ticNumber = ticNumber;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.printing.component.impl.ScaleBarComponent#getUnit()
	 */
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public void fromDto(ScaleBarComponentInfo scaleBarInfo) {
		super.fromDto(scaleBarInfo);
		setTicNumber(scaleBarInfo.getTicNumber());
		setUnit(scaleBarInfo.getUnit());
		setFont(converterService.toInternal(scaleBarInfo.getFont()));
	}

	
}
