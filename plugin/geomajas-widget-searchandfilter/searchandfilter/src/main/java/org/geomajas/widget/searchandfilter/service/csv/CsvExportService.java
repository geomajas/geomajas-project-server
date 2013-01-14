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
package org.geomajas.widget.searchandfilter.service.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.widget.searchandfilter.command.dto.CsvMetadata;
import org.geomajas.widget.searchandfilter.service.FileDownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service to convert features to CSV.
 *
 * TODO only Primitive Attributes are supported atm.
 *
 * @author Kristof Heirwegh
 */
@Component
public class CsvExportService {

	private final Logger log = LoggerFactory.getLogger(CsvExportService.class);

	private final Object lock = new Object();

	@Autowired
	private FileDownloadService downloadService;

	public String buildCsvDocument(Feature[] features, VectorLayer layer, CsvMetadata metadata) throws IOException {
		synchronized (lock) {
			if (features == null || layer == null || metadata == null) {
				throw new IllegalArgumentException("All parameters are required.");
			}

			CsvBuilder cb = new CsvBuilder(metadata);
			List<AttributeInfo> attrs = layer.getLayerInfo().getFeatureInfo().getAttributes();
			List<PrimitiveAttributeInfo> cols = new ArrayList<PrimitiveAttributeInfo>();
			for (AttributeInfo ai : attrs) { // clean up list
				if (!ai.isHidden() && ai instanceof PrimitiveAttributeInfo) {
					if (metadata.useIdentifyingAttributesOnly()) {
						if (ai.isIdentifying()) {
							cols.add((PrimitiveAttributeInfo) ai);
						}
					} else {
						cols.add((PrimitiveAttributeInfo) ai);
					}
				}
			}

			addHeaderRow(cb, cols);

			for (Feature f : features) {
				addFeature(cb, cols, f);
			}

			return save(cb, metadata.getFilename(), metadata.getEncoding());
		}
	}

	private String save(CsvBuilder cb, String description, String encoding) throws IOException {
		// save to file (might do this immediately, would be more efficient for
		// very large files)
		File f = File.createTempFile("geomajas-", ".csv");
		String documentId = UUID.randomUUID().toString();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			byte[] buffer = cb.getBuffer().toString().getBytes(encoding);
			fos.write(buffer);
			fos.flush();
			log.debug("Wrote a temporary downloadfile: " + f.getName() + " - size: " + buffer.length + " bytes.");
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

		// add to list
		downloadService.addFile(documentId, f, description);
		return documentId;
	}

	private void addHeaderRow(CsvBuilder cb, List<PrimitiveAttributeInfo> cols) {
		for (AttributeInfo ai : cols) {
			cb.addField(ai.getLabel());
		}
		cb.endRecord();
	}

	@SuppressWarnings("rawtypes")
	private void addFeature(CsvBuilder cb, List<PrimitiveAttributeInfo> cols, Feature feature) {
		Map<String, Attribute> attrs = feature.getAttributes();
		for (PrimitiveAttributeInfo ai : cols) {
			Object value = attrs.get(ai.getName()).getValue();
			if (value == null) {
				cb.addField("");
			} else {
				if (PrimitiveType.DATE.equals(ai.getType())) {
					cb.addField((Date) value);
				} else if (PrimitiveType.DOUBLE.equals(ai.getType())) {
					cb.addField((Double) value);
				} else if (PrimitiveType.FLOAT.equals(ai.getType())) {
					cb.addField((Float) value);
				} else if (PrimitiveType.INTEGER.equals(ai.getType())) {
					cb.addField((Integer) value);
				} else if (PrimitiveType.LONG.equals(ai.getType())) {
					cb.addField((Long) value);
				} else if (PrimitiveType.BOOLEAN.equals(ai.getType())) {
					cb.addField((Boolean) value);
				} else if (PrimitiveType.STRING.equals(ai.getType())) {
					cb.addField((String) value);
				} else {
					cb.addField(value.toString());
				}
			}
		}
		cb.endRecord();
	}
}
