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
package org.geomajas.plugin.printing.component.impl;

import org.geomajas.plugin.printing.component.ImageComponent;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.ImageComponentInfo;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

/**
 * Inclusion of image in printed document.
 * 
 * @author Jan De Moerloose
 */
@Component("ImageComponentPrototype")
@Scope(value = "prototype")
public class ImageComponentImpl extends PrintComponentImpl implements ImageComponent {

	/**
	 * The (relative) path of the image
	 */
	private String imagePath;

	/**
	 * The image
	 */
	private Image image;

	public ImageComponentImpl() {
		super("image");
	}

	public ImageComponentImpl(String id, LayoutConstraint constraint) {
		super(id, constraint);
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
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

	public void render(PdfContext context) {
		context.drawImage(image, getSize(), null);
	}

	@Override
	public void calculateSize(PdfContext context) {
		float width = getConstraint().getWidth();
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

	public void fromDto(PrintComponentInfo info, PrintDtoConverterService service) {
		super.fromDto(info, service);
		ImageComponentInfo imageInfo = (ImageComponentInfo) info;
		setImagePath(imageInfo.getImagePath());
	}

}
