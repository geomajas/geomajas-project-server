/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.printing.component.impl;

import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.ImageComponentInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

/**
 * Inclusion of image in printed document.
 * 
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class ImageComponentImpl extends AbstractPrintComponent<ImageComponentInfo> {

	/** The (relative) path of the image. */
	private String imagePath;

	/** The image. */
	private Image image;

	public ImageComponentImpl() {
		super("image");
	}

	public ImageComponentImpl(String id, LayoutConstraint constraint) {
		super(id, constraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.ImageComponent#getImagePath()
	 */
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@SuppressWarnings("deprecation")
	public void render(PdfContext context) {
		context.drawImage(image, getSize(), null);
	}

	@Override
	public void calculateSize(PdfContext context) {
		@SuppressWarnings("deprecation")
		float width = getConstraint().getWidth();
		@SuppressWarnings("deprecation")
		float height = getConstraint().getHeight();
		image = context.getImage(getImagePath());
		if (width == 0 && height == 0) {
			width = image.getWidth();
			height = image.getHeight();
		} else if (width == 0) {
			width = image.getWidth() / image.getHeight() * height;
		} else if (height == 0) {
			height = image.getHeight() / image.getWidth() * width;
		}
		setBounds(new Rectangle(width, height));
	}

	public void fromDto(ImageComponentInfo imageInfo) {
		super.fromDto(imageInfo);
		setImagePath(imageInfo.getImagePath());
	}

	public void accept(PrintComponentVisitor visitor) {
	}
}