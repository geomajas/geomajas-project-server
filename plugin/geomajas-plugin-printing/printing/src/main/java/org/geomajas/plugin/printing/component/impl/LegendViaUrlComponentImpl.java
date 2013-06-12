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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.LegendViaUrlComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendViaUrlComponentInfo;
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

	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(LegendViaUrlComponentImpl.class);

	private static final int DPI_FOR_IMAGE = 288;

	private static final String NOT_VISIBLE_MSG = "INVISIBLE_FOR_SCALE";

	private static final float MARGIN_LEFT_IMAGE_RELATIVE_TO_FONTSIZE = 0.25f;

	private static final float MARGIN_TOP_IMAGE_RELATIVE_TO_FONTSIZE = 0.25f;

	@Autowired
	private DispatcherUrlService dispatcherUrlService;

	private String layerId;

	private URL legendImageServiceUrl;

	private boolean visible = true;

	private Image image;

	private String legendImageServiceUrlAsString;

	/** Constructor. */
	public LegendViaUrlComponentImpl() {
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 *            visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
	}

	@Override
	public void calculateSize(PdfContext context) {
		if (getConstraint().getMarginX() <= 0.0f) {
			getConstraint().setMarginX(MARGIN_LEFT_IMAGE_RELATIVE_TO_FONTSIZE * getLegend().getFont().getSize());
		}
		getConstraint().setMarginY(MARGIN_TOP_IMAGE_RELATIVE_TO_FONTSIZE * getLegend().getFont().getSize());

		@SuppressWarnings("deprecation")
		float width = getConstraint().getWidth();
		@SuppressWarnings("deprecation")
		float height = getConstraint().getHeight();

		// Retrieve legend image from URL if not yet retrieved
		if (null == image && visible) {
			try {
				image = Image.getInstance(new URL(getLegendImageServiceUrl()));
				image.setDpi(DPI_FOR_IMAGE, DPI_FOR_IMAGE); // Increase the precision
			} catch (BadElementException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				visible = !hasInVisibleResponse();
			}
		}
		if (!visible) {
			setBounds(new Rectangle(0, 0));
		} else {
			if (width <= 0 && height <= 0) {
				// when / 2.0f: The image is generated with a scale of 1:0.5
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

	@SuppressWarnings("deprecation")
	protected LegendComponent getLegend() {
		return (LegendComponent) (getParent().getParent());
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(PdfContext context) {
		if (null == image && visible) {
			calculateSize(context);
		}
		if (visible) {
			context.drawImage(image, getSize(), null);
		}
	}

	public String getClientLayerId() {
		return layerId; // purely informative
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
				log.info("BaseURL: {}", baseUrlAsString);
				URL baseUrl = new URL(baseUrlAsString);
				absoluteUrl = new URL(baseUrl, "../" + legendImageServiceUrlAsString);
				log.info("AbsoluteUrl: {}", absoluteUrl);
			} catch (MalformedURLException e) {
				// Should never happen...
				e.printStackTrace();
			}
		} else {
			try {
				absoluteUrl = new URL(legendImageServiceUrlAsString);
			} catch (MalformedURLException e) {
				// Should never happen...
				e.printStackTrace();
			}
		}
		this.legendImageServiceUrlAsString = absoluteUrl.toExternalForm();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void fromDto(LegendViaUrlComponentInfo info) {
		super.fromDto(info);
		visible = true;
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
			e1.printStackTrace();
		}
		if (null == url) {
			return false; // ABORT
		}

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		if (null == connection) {
			return false; // ABORT
		}

		try {
			connection.connect();
		} catch (IOException e1) {
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
}