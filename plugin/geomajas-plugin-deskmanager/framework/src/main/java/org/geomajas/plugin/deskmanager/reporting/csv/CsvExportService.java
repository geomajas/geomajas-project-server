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
package org.geomajas.plugin.deskmanager.reporting.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.layer.VectorLayer;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class CsvExportService {

	private final Logger log = LoggerFactory.getLogger(CsvExportService.class);

	private final Map<String, Item> map = new HashMap<String, Item>();

	private final Object lock = new Object();

	/**
	 * for geotools features.
	 * 
	 * @param it
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation") // See GBE-321
	public String buildCsvDocument(Iterator<SimpleFeatureImpl> it, VectorLayer layer) throws IOException {
		synchronized (lock) {
			if (it == null) {
				throw new IllegalArgumentException("Please provide a list of features");
			}

			CsvBuilder cb = new CsvBuilder();
			Collection<AbstractAttributeInfo> atti = layer.getLayerInfo().getFeatureInfo().getAttributesMap().values();
			List<PrimitiveAttributeInfo> cols = new ArrayList<PrimitiveAttributeInfo>();
			for (AbstractAttributeInfo ai : atti) { // clean up list
				if (ai instanceof PrimitiveAttributeInfo && !((PrimitiveAttributeInfo) ai).isHidden()
						&& ((PrimitiveAttributeInfo) ai).isIdentifying()) {
					cols.add((PrimitiveAttributeInfo) ai);
				}
			}

			addHeaderRow(cb, cols);

			while (it.hasNext()) {
				addFeature(cb, cols, it.next());
			}

			return save(cb, layer.getId());
		}
	}

	/**
	 * FIXME do in separate thread.
	 * 
	 * @return documentId -- use with CsvServlet to return the csv file
	 */
	public String buildCsvDocument(Iterator<CsvExport> it, String layerId) throws IOException {
		synchronized (lock) {
			if (it == null) {
				throw new IllegalArgumentException("Please provide a list of features");
			}

			CsvBuilder cb = new CsvBuilder();
			if (it.hasNext()) {
				CsvExport ce = it.next();
				ce.addHeaderRow(cb);
				ce.toCsv(cb);
			}

			while (it.hasNext()) {
				it.next().toCsv(cb);
			}

			return save(cb, layerId);
		}
	}

	public Item getFile(String documentId) {
		synchronized (lock) {
			return (map.get(documentId));
		}
	}

	public void removeFile(String documentId) {
		synchronized (lock) {
			File f = map.get(documentId).file;
			if (f != null) {
				try {
					f.delete();
					log.info("Deleted a temporary csv-file: " + f.getName());
				} catch (Exception e) {
					log.warn("Failed to delete temporary csv export file: " + e.getMessage());
				}
				map.remove(documentId);
			}
		}
	}

	private String save(CsvBuilder cb, String layerId) throws IOException {
		// save to file (might do this immediately, would be more efficient for very large files)
		File f = File.createTempFile("geogui-", ".csv");
		String documentId = UUID.randomUUID().toString();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			byte[] buffer = cb.getBuffer().toString().getBytes("UTF-8");
			fos.write(buffer);
			fos.flush();
			log.info("Wrote a temporary csv-file: " + f.getName() + " - size: " + buffer.length + " bytes.");
		} catch (IOException e) {
			throw e;
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

		// add to list
		map.put(documentId, new Item(f));

		return documentId;
	}

	// ------------------------------------------------------------------
	// -- generic feature export --

	@SuppressWarnings("deprecation") // See GBE-321
	private void addHeaderRow(CsvBuilder cb, List<PrimitiveAttributeInfo> cols) {
		for (PrimitiveAttributeInfo ai : cols) {
			cb.addField(ai.getLabel());
		}
		cb.endRecord();
	}

	@SuppressWarnings("deprecation") // See GBE-321
	private void addFeature(CsvBuilder cb, List<PrimitiveAttributeInfo> cols, SimpleFeatureImpl feature) {
		for (PrimitiveAttributeInfo ai : cols) {
			if (PrimitiveType.DATE.equals(ai.getType())) {
				cb.addField((Date) feature.getAttribute(ai.getName()));
			} else if (PrimitiveType.DOUBLE.equals(ai.getType())) {
				cb.addField((Double) feature.getAttribute(ai.getName()));
			} else if (PrimitiveType.FLOAT.equals(ai.getType())) {
				cb.addField((Float) feature.getAttribute(ai.getName()));
			} else if (PrimitiveType.INTEGER.equals(ai.getType())) {
				cb.addField((Integer) feature.getAttribute(ai.getName()));
			} else if (PrimitiveType.LONG.equals(ai.getType())) {
				cb.addField((Long) feature.getAttribute(ai.getName()));
			} else if (PrimitiveType.BOOLEAN.equals(ai.getType())) {
				cb.addField((Boolean) feature.getAttribute(ai.getName()));
			} else {
				cb.addField(feature.getAttribute(ai.getName()).toString());
			}
		}
		cb.endRecord();
	}

	// ------------------------------------------------------------------

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private static final class Item {

		private File file;

		private Item(File file) {
			this.file = file;
		}
	}
}
