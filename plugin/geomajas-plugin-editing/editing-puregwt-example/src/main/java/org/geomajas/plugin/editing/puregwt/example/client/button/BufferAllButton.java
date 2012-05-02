/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.puregwt.example.client.button;

import java.util.List;

import org.geomajas.command.dto.BufferInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryOperationServiceImpl;
import org.geomajas.plugin.editing.puregwt.example.client.GeometryToShapeConverter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Button that applies a buffer to all drawn geometries.
 * 
 * @author Emiel Ackermann
 */
public class BufferAllButton extends Button {

	public BufferAllButton(final GeometryToShapeConverter converter, final TextBox bufferDistance) {
		super("Buffer");
		this.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				BufferInfo bufferInfo = new BufferInfo();
				String value = bufferDistance.getValue();
				if (!"".equals(bufferDistance) && !"0".equals(bufferDistance)) {
					bufferInfo.setDistance(Double.valueOf(value));
					final List<Geometry> geometries = converter.getGeometries();
					final GeometryEditService editService = converter.getEditService();
					Geometry geometry = editService.stop();
					if (null != geometry) {
						geometries.add(geometry);
					}
					if (!geometries.isEmpty()) {
						new GeometryOperationServiceImpl().buffer(geometries, bufferInfo, 
								new Callback<List<Geometry>, Throwable>() {
							
							public void onSuccess(List<Geometry> result) {
								converter.clear();
								int size = result.size();
								for (int i = 0 ; i < size ; i++) {
									if (i == size - 1) {
										editService.start(result.get(i));
									} else {
										converter.processGeometry(result.get(i));
									}
								}
							}
							
							public void onFailure(Throwable reason) {
							}
						});
					}
				}
			}
		});
	}
}