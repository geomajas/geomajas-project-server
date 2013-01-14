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
package org.geomajas.plugin.printing.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.geomajas.configuration.SymbolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Utilities;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Context for PDF printing. This is a wrapper on a PdfWriter on which components of a print template can render
 * themselves. Components do not need to know about the template's layout and can draw in a relative coordinate system
 * in which their bottom-left origin corresponds to (0,0).
 *
 * @author Jan De Moerloose
 */
public class PdfContext {

	private PdfWriter writer;

	private PdfTemplate template;

	// current origin
	private float origX;

	private float origY;

	// stack of previous origins
	private Stack<Float> prevOrigX = new Stack<Float>();

	private Stack<Float> prevOrigY = new Stack<Float>();

	private final Logger log = LoggerFactory.getLogger(PdfContext.class);
	
	private Map<String, Color> predefinedColors = new HashMap<String, Color>();

	/**
	 * Constructs a context for the specified writer and application.
	 *
	 * @param writer writer
	 */
	public PdfContext(PdfWriter writer) {
		this.writer = writer;
		predefinedColors.put("black", Color.BLACK);
		predefinedColors.put("blue", Color.BLUE);
		predefinedColors.put("cyan", Color.CYAN);
		predefinedColors.put("darkgray", Color.DARK_GRAY);
		predefinedColors.put("gray", Color.GRAY);
		predefinedColors.put("green", Color.GREEN);
		predefinedColors.put("lightgray", Color.LIGHT_GRAY);
		predefinedColors.put("magenta", Color.MAGENTA);
		predefinedColors.put("orange", Color.ORANGE);
		predefinedColors.put("pink", Color.PINK);
		predefinedColors.put("red", Color.RED);
		predefinedColors.put("white", Color.WHITE);
		predefinedColors.put("yellow", Color.YELLOW);
	}

	/**
	 * Initializes context size.
	 *
	 * @param rectangle rectangle
	 */
	public void initSize(Rectangle rectangle) {
		template = writer.getDirectContent().createTemplate(rectangle.getWidth(), rectangle.getHeight());
	}

	public void setOrigin(float x, float y) {
		this.origX = x;
		this.origY = y;
	}

	/**
	 * Return the text box for the specified text and font.
	 *
	 * @param text text
	 * @param font font
	 * @return text box
	 */
	public Rectangle getTextSize(String text, Font font) {
		template.saveState();
		// get the font
		DefaultFontMapper mapper = new DefaultFontMapper();
		BaseFont bf = mapper.awtToPdf(font);
		template.setFontAndSize(bf, font.getSize());
		// calculate text width and height
		float textWidth = template.getEffectiveStringWidth(text, false);
		float ascent = bf.getAscentPoint(text, font.getSize());
		float descent = bf.getDescentPoint(text, font.getSize());
		float textHeight = ascent - descent;
		template.restoreState();
		return new Rectangle(0, 0, textWidth, textHeight);
	}

	/**
	 * Draw text in the center of the specified box.
	 *
	 * @param text text
	 * @param font font
	 * @param box box to put text int
	 * @param fontColor colour
	 */
	public void drawText(String text, Font font, Rectangle box, Color fontColor) {
		template.saveState();
		// get the font
		DefaultFontMapper mapper = new DefaultFontMapper();
		BaseFont bf = mapper.awtToPdf(font);
		template.setFontAndSize(bf, font.getSize());

		// calculate descent
		float descent = 0;
		if (text != null) {
			descent = bf.getDescentPoint(text, font.getSize());
		}

		// calculate the fitting size
		Rectangle fit = getTextSize(text, font);

		// draw text if necessary
		template.setColorFill(fontColor);
		template.beginText();
		template.showTextAligned(PdfContentByte.ALIGN_LEFT, text, origX + box.getLeft() + 0.5f
				* (box.getWidth() - fit.getWidth()), origY + box.getBottom() + 0.5f
				* (box.getHeight() - fit.getHeight()) - descent, 0);
		template.endText();
		template.restoreState();
	}

	/**
	 * Draw a rectangular boundary.
	 *
	 * @param rect rectangle
	 */
	public void strokeRectangle(Rectangle rect) {
		strokeRectangle(rect, Color.black, 1, null);
	}

	/**
	 * Draw a rectangular boundary with this color and linewidth.
	 *
	 * @param rect
	 *            rectangle
	 * @param color
	 *            color
	 * @param linewidth
	 *            line width
	 */
	public void strokeRectangle(Rectangle rect, Color color, float linewidth) {
		strokeRectangle(rect, color, linewidth, null);
	}

	public void strokeRectangle(Rectangle rect, Color color, float linewidth, float[] dashArray) {
		template.saveState();
		setStroke(color, linewidth, dashArray);
		template.rectangle(origX + rect.getLeft(), origY + rect.getBottom(), rect.getWidth(), rect.getHeight());
		template.stroke();
		template.restoreState();
	}

	/**
	 * Draw a rounded rectangular boundary.
	 *
	 * @param rect rectangle
	 * @param color colour
	 * @param linewidth line width
	 * @param r radius for rounded corners
	 */
	public void strokeRoundRectangle(Rectangle rect, Color color, float linewidth, float r) {
		template.saveState();
		setStroke(color, linewidth, null);
		template.roundRectangle(origX + rect.getLeft(), origY + rect.getBottom(), rect.getWidth(), rect.getHeight(), r);
		template.stroke();
		template.restoreState();
	}

	/**
	 * Draw a rectangle's interior.
	 *
	 * @param rect rectangle
	 */
	public void fillRectangle(Rectangle rect) {
		fillRectangle(rect, Color.white);
	}

	/**
	 * Draw a rectangle's interior with this color.
	 *
	 * @param rect rectangle
	 * @param color colour
	 */
	public void fillRectangle(Rectangle rect, Color color) {
		template.saveState();
		setFill(color);
		template.rectangle(origX + rect.getLeft(), origY + rect.getBottom(), rect.getWidth(), rect.getHeight());
		template.fill();
		template.restoreState();
	}

	public void fillRoundRectangle(Rectangle rect, Color color, float r) {
		template.saveState();
		setFill(color);
		template.roundRectangle(origX + rect.getLeft(), origY + rect.getBottom(), rect.getWidth(), rect.getHeight(), r);
		template.fill();
		template.restoreState();
	}

	/**
	 * Draw an elliptical exterior with this color.
	 *
	 * @param rect rectangle in which ellipse should fit
	 * @param color colour to use for stroking
	 * @param linewidth line width
	 */
	public void strokeEllipse(Rectangle rect, Color color, float linewidth) {
		template.saveState();
		setStroke(color, linewidth, null);
		template.ellipse(origX + rect.getLeft(), origY + rect.getBottom(), origX + rect.getRight(),
				origY + rect.getTop());
		template.stroke();
		template.restoreState();
	}

	/**
	 * Draw an elliptical interior with this color.
	 *
	 * @param rect rectangle in which ellipse should fit
	 * @param color colour to use for filling
	 */
	public void fillEllipse(Rectangle rect, Color color) {
		template.saveState();
		setFill(color);
		template.ellipse(origX + rect.getLeft(), origY + rect.getBottom(), origX + rect.getRight(),
				origY + rect.getTop());
		template.fill();
		template.restoreState();
	}

	/**
	 * Move this rectangle to the specified bottom-left point.
	 *
	 * @param rect rectangle to move
	 * @param x new x origin
	 * @param y new y origin
	 */
	public void moveRectangleTo(Rectangle rect, float x, float y) {
		float width = rect.getWidth();
		float height = rect.getHeight();
		rect.setLeft(x);
		rect.setBottom(y);
		rect.setRight(rect.getLeft() + width);
		rect.setTop(rect.getBottom() + height);
	}

	/**
	 * Translate this rectangle over the specified following distances.
	 *
	 * @param rect rectangle to move
	 * @param dx delta x
	 * @param dy delta y
	 */
	public void translateRectangle(Rectangle rect, float dx, float dy) {
		float width = rect.getWidth();
		float height = rect.getHeight();
		rect.setLeft(rect.getLeft() + dx);
		rect.setBottom(rect.getBottom() + dy);
		rect.setRight(rect.getLeft() + dx + width);
		rect.setTop(rect.getBottom() + dy + height);
	}

	public Color getColor(String css, float opacity) {
		return getColor(css, opacity, Color.white);
	}

	public Color getColor(String css, float opacity, Color defaultColor) {
		Color color;
		if (null == css) {
			color = defaultColor;
		} else {
			String lc = css.toLowerCase();
			if (predefinedColors.containsKey(lc)) {
				return predefinedColors.get(lc);
			} else {
				try {
					color = Color.decode(css);
				} catch (NumberFormatException e) {
					color = defaultColor;
				}
			}
		}
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (opacity * 255));
	}

	public float[] getDashArray(String dashArrayString) {
		if (dashArrayString == null || "".equals(dashArrayString.trim()) || "none".equals(dashArrayString)) {
			return null;
		}

		try {
			String[] res = dashArrayString.split(",");
			float[] dasharr = new float[res.length];
			for (int i = 0; i < res.length; i++) {
				dasharr[i] = Float.parseFloat(res[i]);
			}
			return dasharr;
		} catch (Exception e) { // NOSONAR
			log.warn("Error in dashArrayString: " + dashArrayString);
			return null;
		}
	}

	public Image getImage(String path) {
		try {
			URL url = getClass().getResource(path);
			if (url == null) {
				url = Utilities.toURL(path);
			}
			return Image.getInstance(url);
		} catch (Exception e) { // NOSONAR
			log.warn("could not fetch image", e);
			return null;
		}
	}

	/**
	 * Draws the specified image with the first rectangle's bounds, clipping with the second one and adding
	 * transparency.
	 *
	 * @param img image
	 * @param rect rectangle
	 * @param clipRect clipping bounds
	 */
	public void drawImage(Image img, Rectangle rect, Rectangle clipRect) {
		drawImage(img, rect, clipRect, 1);
	}

	/**
	 * Draws the specified image with the first rectangle's bounds, clipping with the second one.
	 *
	 * @param img image
	 * @param rect rectangle
	 * @param clipRect clipping bounds
	 * @param opacity opacity of the image (1 = opaque, 0= transparent)
	 */
	public void drawImage(Image img, Rectangle rect, Rectangle clipRect, float opacity) {
		try {
			template.saveState();
			// opacity
			PdfGState state = new PdfGState();
			state.setFillOpacity(opacity);
			state.setBlendMode(PdfGState.BM_NORMAL);
			template.setGState(state);
			// clipping code
			if (clipRect != null) {
				template.rectangle(clipRect.getLeft() + origX, clipRect.getBottom() + origY, clipRect.getWidth(),
						clipRect.getHeight());
				template.clip();
				template.newPath();
			}
			template.addImage(img, rect.getWidth(), 0, 0, rect.getHeight(), origX + rect.getLeft(), origY
					+ rect.getBottom());
		} catch (DocumentException e) {
			log.warn("could not draw image", e);
		} finally {
			template.restoreState();
		}
	}

	/**
	 * Draw a path specified by relative coordinates in [0,1] range wrt the specified rectangle.
	 *
	 * @param x x-ordinate
	 * @param y y-ordinate
	 * @param rect rectangle
	 * @param color color to use
	 * @param lineWidth line width
	 * @param dashArray lengths for dashed and white area
	 */
	public void drawRelativePath(float[] x, float[] y, Rectangle rect, Color color, float lineWidth,
			float[] dashArray) {
		template.saveState();
		setStroke(color, lineWidth, dashArray);
		template.moveTo(origX + getAbsoluteX(x[0], rect), origY + getAbsoluteY(y[0], rect));
		for (int i = 1; i < x.length; i++) {
			template.lineTo(origX + getAbsoluteX(x[i], rect), origY + getAbsoluteY(y[i], rect));
		}
		template.stroke();
		template.restoreState();
	}

	private float getAbsoluteX(float f, Rectangle rect) {
		return rect.getLeft() + f * rect.getWidth();
	}

	private float getAbsoluteY(float f, Rectangle rect) {
		return rect.getBottom() + f * rect.getHeight();
	}

	private void setStroke(Color color, float linewidth, float[] dashArray) {
		// Color and transparency
		PdfGState state = new PdfGState();
		state.setStrokeOpacity(color.getAlpha());
		state.setBlendMode(PdfGState.BM_NORMAL);
		template.setGState(state);
		template.setColorStroke(color);
		// linewidth
		template.setLineWidth(linewidth);
		if (dashArray != null) {
			template.setLineDash(dashArray, 0f);
		}
	}

	private void setFill(Color color) {
		// Color and transparency
		PdfGState state = new PdfGState();
		state.setFillOpacity(color.getAlpha() / 255f);
		state.setBlendMode(PdfGState.BM_NORMAL);
		template.setGState(state);
		template.setColorFill(color);
	}

	/**
	 * Draw the specified geometry.
	 *
	 * @param geometry geometry to draw
	 * @param symbol symbol for geometry
	 * @param fillColor fill colour
	 * @param strokeColor stroke colour
	 * @param lineWidth line width
	 * @param clipRect clipping rectangle
	 */
	public void drawGeometry(Geometry geometry, SymbolInfo symbol, Color fillColor, Color strokeColor, float lineWidth,
			float[] dashArray, Rectangle clipRect) {
		template.saveState();
		// clipping code
		if (clipRect != null) {
			template.rectangle(clipRect.getLeft() + origX, clipRect.getBottom() + origY, clipRect.getWidth(), clipRect
					.getHeight());
			template.clip();
			template.newPath();
		}
		setStroke(strokeColor, lineWidth, dashArray);
		setFill(fillColor);
		drawGeometry(geometry, symbol);
		template.restoreState();
	}

	private void drawGeometry(Geometry g, SymbolInfo symbol) {
		if (g instanceof MultiPolygon) {
			MultiPolygon mpoly = (MultiPolygon) g;
			for (int i = 0; i < mpoly.getNumGeometries(); i++) {
				drawGeometry(mpoly.getGeometryN(i), symbol);
			}
		} else if (g instanceof MultiLineString) {
			MultiLineString mline = (MultiLineString) g;
			for (int i = 0; i < mline.getNumGeometries(); i++) {
				drawGeometry(mline.getGeometryN(i), symbol);
			}
		} else if (g instanceof MultiPoint) {
			MultiPoint mpoint = (MultiPoint) g;
			for (int i = 0; i < mpoint.getNumGeometries(); i++) {
				drawGeometry(mpoint.getGeometryN(i), symbol);
			}
		} else if (g instanceof Polygon) {
			Polygon poly = (Polygon) g;
			LineString shell = poly.getExteriorRing();
			int nHoles = poly.getNumInteriorRing();
			drawPathContent(shell.getCoordinates());
			for (int j = 0; j < nHoles; j++) {
				drawPathContent(poly.getInteriorRingN(j).getCoordinates());
			}
			template.closePathEoFillStroke();
		} else if (g instanceof LineString) {
			LineString line = (LineString) g;
			drawPathContent(line.getCoordinates());
			template.stroke();
		} else if (g instanceof Point) {
			Point point = (Point) g;
			drawPoint(point.getCoordinate(), symbol);
			template.fillStroke();
		}
	}

	private void drawPathContent(Coordinate[] coord) {
		template.moveTo(origX + (float) coord[0].x, origY + (float) coord[0].y);
		for (int i = 1; i < coord.length; i++) {
			template.lineTo(origX + (float) coord[i].x, origY + (float) coord[i].y);
		}
	}

	private void drawPoint(Coordinate coord, SymbolInfo symbol) {
		if (symbol.getImage() != null) {
			try {
				Image pointImage = Image.getInstance(symbol.getImage().getHref());
				//template.addImage(image, width, 0, 0, height, x, y)
				template.addImage(pointImage, symbol.getImage().getWidth(), 0, 0, symbol.getImage().getHeight()
						, origX + (float) coord.x, origY + (float) coord.y);
			} catch (Exception ex) { // NOSONAR
				log.error("Not able to create POINT image for rendering", ex);
			}
		} else if (symbol.getCircle() != null) {
			float radius = symbol.getCircle().getR();

			template.circle(origX + (float) coord.x, origY + (float) coord.y, radius);

		} else if (symbol.getRect() != null) {
			float width = symbol.getRect().getW();
			float height = symbol.getRect().getW();

			template.rectangle(origX + (float) coord.x - (width / 2), origY + (float) coord.y - (height / 2), width,
					height);

		}

	}

	/**
	 * Return this context as an image.
	 *
	 * @return this context as image
	 * @throws BadElementException oops
	 */
	public Image getImage() throws BadElementException {
		return Image.getInstance(template);
	}

	/**
	 * Push the current origin on the stack.
	 */
	public void saveOrigin() {
		prevOrigX.push(origX);
		prevOrigY.push(origY);
	}

	/**
	 * Pop the previous origin from the stack.
	 */
	public void restoreOrigin() {
		origX = prevOrigX.pop();
		origY = prevOrigY.pop();
	}

	public Color makeTransparent(Color color, float alpha) {
		return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);
	}

	public void drawLine(float x0, float y0, float x1, float y1, Color color, float lineWidth) {
		template.saveState();
		setStroke(color, lineWidth, null);
		template.moveTo(origX + x0, origY + y0);
		template.lineTo(origX + x1, origY + y1);
		template.stroke();
		template.restoreState();

	}

	/**
	 * Converts an absolute rectangle to a relative one wrt to the current coordinate system.
	 *
	 * @param rect absolute rectangle
	 * @return relative rectangle
	 */
	public Rectangle toRelative(Rectangle rect) {
		return new Rectangle(rect.getLeft() - origX, rect.getBottom() - origY, rect.getRight() - origX, rect.getTop()
				- origY);
	}

	public Graphics2D getGraphics2D(Rectangle clipRect) {
		Graphics2D graphics = template.createGraphics(template.getWidth(), template.getHeight());
		graphics.translate(origX, template.getHeight() - origY - clipRect.getHeight());
		graphics.clipRect(0, 0, (int) clipRect.getWidth(), (int) clipRect.getHeight());
		return graphics;
	}

}
