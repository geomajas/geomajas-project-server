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

package org.geomajas.plugin.printing.component.impl;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.LegendViaUrlComponent;
import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;
import org.geomajas.plugin.printing.component.dto.LegendViaUrlComponentInfo;
import org.geomajas.plugin.printing.parser.FontConverter;
import org.geomajas.service.DispatcherUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Utilities;
import com.lowagie.text.pdf.codec.PngImage;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Rendering of the legend for a certain layer in a printed document via an image specified via a URL.
 * 
 * @author An Buyle
 */
@Component()
@Scope(value = "prototype")
public class LegendViaUrlComponentImpl extends AbstractPrintComponent<LegendViaUrlComponentInfo> implements
LegendViaUrlComponent {

	private static final String BUNDLE_NAME = "org/geomajas/plugin/printing/PrintingMessages"; //$NON-NLS-1$
	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(LegendViaUrlComponentImpl.class);
	
	// do not make this static, different requests might need different bundles
	@XStreamOmitField
	private ResourceBundle resourceBundle;


	private static final int DPI_FOR_IMAGE = 288;

	private static final String NOT_VISIBLE_MSG = "INVISIBLE_FOR_SCALE";

	private static final float MARGIN_LEFT_IMAGE_RELATIVE_TO_FONTSIZE = 0.25f;

	private static final float MARGIN_TOP_IMAGE_RELATIVE_TO_FONTSIZE = 0.25f;
	
	/**
	 * The font for error text
	 */
	@XStreamConverter(FontConverter.class)
	private Font font = new Font(LegendComponentInfo.DEFAULT_LEGEND_FONT_FAMILY, Font.PLAIN, 
			9); // Default font

	/**
	 * Color of the text.
	 */
	private String fontColor = "0x000000";


	@Autowired
	private DispatcherUrlService dispatcherUrlService;

	private String layerId;

	private URL legendImageServiceUrl;

	private boolean visible = true;

	private Image image;

	private String legendImageServiceUrlAsString;

	private String warning;

	/** Constructor. */

	public LegendViaUrlComponentImpl() {
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 *			visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
	}

	@Override
	public void calculateSize(PdfContext context) {
		
		if (null == getLegendImageServiceUrl()) {
			log.error("getLegendImageServiceUrl() returns unexpectedly with NULL");
			setBounds(new Rectangle(0, 0));
			return; // Abort
		}
		
		String locale = getLocale();
		try {
			if (null != locale && !locale.isEmpty()) {
				resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(locale));
			} else {
				resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
			}
		} catch (MissingResourceException e) {
			resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("en'"));
		}
		
		if (getConstraint().getMarginX() <= 0.0f) {
			getConstraint().setMarginX(MARGIN_LEFT_IMAGE_RELATIVE_TO_FONTSIZE * getLegend().getFont().getSize());
		}
		if (getConstraint().getMarginY() <= 0.0f) {
			getConstraint().setMarginY(MARGIN_TOP_IMAGE_RELATIVE_TO_FONTSIZE * getLegend().getFont().getSize());
		}

		@SuppressWarnings("deprecation")
		float width = getConstraint().getWidth();
		@SuppressWarnings("deprecation")
		float height = getConstraint().getHeight();

		// Retrieve legend image from URL if not yet retrieved
		if (null == image && visible && null == warning) {
			if (getLegendImageServiceUrl().contains("=image/png")) {
				// It´s approx. 2 times faster to use PngImage.getImage() instead of Image.getInstance()
				// since the latter will retrieve the URL twice!
				try {
					image = PngImage.getImage(new URL(getLegendImageServiceUrl()));
									// Image.getInstance(new URL(getLegendImageServiceUrl()));
					image.setDpi(DPI_FOR_IMAGE, DPI_FOR_IMAGE); // Increase the precision
				} catch (MalformedURLException e) {
					log.error("Error in Image.getInstance() for URL " + getLegendImageServiceUrl(), e);
					e.printStackTrace();
				} catch (IOException e) {
					// This exception is OK if no legend image is generated because out of scale range
					// for a dynamic layer, then a text message which indicates an invisible legend is referred
					// to by the URL 
					visible = !hasInVisibleResponse();
					if (visible) {
						log.warn("Unexpected IOException for Image.getInstance() for URL "
									+ getLegendImageServiceUrl(), e);
					}
				}
			} else {
				try {
					image = Image.getInstance(new URL(getLegendImageServiceUrl()));
					image.setDpi(DPI_FOR_IMAGE, DPI_FOR_IMAGE); // Increase the precision
				} catch (BadElementException e) {
					log.error("Error in Image.getInstance() for URL " + getLegendImageServiceUrl(), e);
					e.printStackTrace();
				} catch (MalformedURLException e) {
					log.error("Error in Image.getInstance() for URL " + getLegendImageServiceUrl(), e);
					e.printStackTrace();
				} catch (IOException e) {
					// This exception is OK if no legend image is generated because out of scale range
					// for a dynamic layer, then a text message which indicates an invisible legend is referred
					// to by the URL 
					visible = !hasInVisibleResponse();
					if (visible) {
						log.warn("Unexpected IOException for Image.getInstance() for URL "
									+ getLegendImageServiceUrl(), e);
					}
				}
				
			}
		}
		if (!visible) {
			setBounds(new Rectangle(0, 0));
		} else if (null == image) {
			generateWarningMessage(context);
		} else {

			if (width <= 0 && height <= 0) {
				// when / 2.0f: The image is generated with a scale of 1:0.5 (but looks awful!)
				width = image.getWidth(); // 2.0f;
				height = image.getHeight(); // 2.0f;
			} else if (width <= 0) {
				width = image.getWidth() / image.getHeight() * height;
			} else if (height <= 0) {
				height = image.getHeight() / image.getWidth() * width;
			}

			setBounds(new Rectangle(width, height)); // excluding the marginX
		}
	}


	protected LegendComponent getLegend() {
		@SuppressWarnings("deprecation")
		PrintComponent<?> ancestor = getParent();
		
		while (null != ancestor && !(ancestor instanceof LegendComponent)) {
			ancestor = ancestor.getParent();
		} 
		if (null != ancestor && ancestor instanceof LegendComponent) {
			return (LegendComponent) ancestor;
		} else {
			return null;
		}
		
	}

	protected String getLocale() {
		PrintComponent<?> ancestor = getParent();
		
		while (null != ancestor && !(ancestor instanceof PageComponent)) {
			ancestor = ancestor.getParent();
		} 
		if (null != ancestor && ancestor instanceof PageComponent) {
			return ((PageComponent) ancestor).getLocale();
		} else {
			return null;
		}
		
	}

	
	@Override
	public boolean isVisible() {
		return this.visible;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(PdfContext context) {
		if (null == image && visible && null == warning) {
			calculateSize(context);
		}
		if (visible) {
			if (null != warning) {
				context.drawText(warning, getFont(), getSize(), context.getColor(getFontColor(), 1f));	
			} else {
				context.drawImage(image, getSize(), null);
			}
		}
	}

	public String getClientLayerId() {
		return layerId;  // purely informative
	}

	public void setClientLayerId(String layerId) {
		this.layerId = layerId; // purely informative
	}

	@Override
	public String getLegendImageServiceUrl() {
		calculateLegendImageServiceUrl();
		return this.legendImageServiceUrl.toExternalForm();
	}

	@Override
	public void setLegendImageServiceUrl(String legendImageServiceUrlAsString) {
		this.image = null; // remove the cached image
		this.legendImageServiceUrl = null; // Clear the cached value

		URL absoluteUrl = null;

		if (!legendImageServiceUrlAsString.startsWith("http:") && !legendImageServiceUrlAsString.startsWith("https:")) {

			try {
				String baseUrlAsString = dispatcherUrlService.getLocalDispatcherUrl();
				log.debug("BaseURL: {}", baseUrlAsString);
				URL baseUrl = new URL(baseUrlAsString);
				absoluteUrl = new URL(baseUrl, "../" + legendImageServiceUrlAsString);
				log.debug("AbsoluteUrl: {}", absoluteUrl);
			} catch (MalformedURLException e) {
				// Should never happen...
				log.error("Error converting URL " + legendImageServiceUrlAsString + " to absolute URL", e);
				e.printStackTrace();
			}
		} else {
			try {
				absoluteUrl = new URL(legendImageServiceUrlAsString);
			} catch (MalformedURLException e) {
				// Should never happen...
				log.error("Error converting URL " + legendImageServiceUrlAsString + " to absolute URL", e);
			}
		}
		this.legendImageServiceUrlAsString = absoluteUrl.toExternalForm();
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
	
	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void fromDto(LegendViaUrlComponentInfo info) {
		super.fromDto(info);
		visible = true;
		warning = null;
		
		setLegendImageServiceUrl(info.getLegendImageServiceUrl());
		setClientLayerId(info.getClientLayerId());
	}

	private boolean hasInVisibleResponse() {
		boolean inVisibleResponse = false;

		URL url = null;
		try {
			url = Utilities.toURL(getLegendImageServiceUrl());
		} catch (MalformedURLException e1) {
			// Should never happen!
			log.error("Exception in  Utilities.toURL() for URL " + getLegendImageServiceUrl(), e1);
			e1.printStackTrace();
		}
		if (null == url) {
			log.error("Error in  Utilities.toURL() for URL " + getLegendImageServiceUrl());
			return false; // Unexpected error, ABORT
		}

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e2) {
			log.error("Exception in url.openConnection() for URL " + getLegendImageServiceUrl(), e2);
			e2.printStackTrace();
		}
		if (null == connection) {
			log.error("Error in  url.openConnection() for URL " + getLegendImageServiceUrl());
			return false;  // Unexpected error, ABORT
		}

		try {
			connection.connect();
		} catch (IOException e1) {
			log.error("Exception in connection.connect for URL " + getLegendImageServiceUrl(), e1);
			e1.printStackTrace();
			return false; // ABORT
		}

		String contentType = connection.getContentType();
		if (contentType.startsWith("text/")) {
			// Something went wrong (normally the URL returns an image
			// Read the response text
			InputStream is = null;
			try {
				is = url.openStream();
				byte[] messageByteArray = new byte[19];
				is.read(messageByteArray, 0, 19);

				is.close();
				is = null;

				String message = new String(messageByteArray);
				if (NOT_VISIBLE_MSG.equals(message)) {
					inVisibleResponse = true;
				}

			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return inVisibleResponse;
	}

	private void calculateLegendImageServiceUrl() {
		if (null == this.legendImageServiceUrl) {
			try {

				this.legendImageServiceUrl = new URL(legendImageServiceUrlAsString);
			} catch (MalformedURLException e) {
				// Should never happen
				e.printStackTrace();
				this.legendImageServiceUrl = null;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void generateWarningMessage(PdfContext context) {
		warning = resourceBundle.getString("ErrorRetrievingLegend");
		
		Rectangle textSize = context.getTextSize(warning, getFont());
		float margin = 0.5f * getFont().getSize();
		
		setBounds(new Rectangle(textSize.getWidth() + 2.0f * margin, textSize.getHeight() + 2 * margin));
	}


}