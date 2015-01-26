/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.service;

import java.awt.Font;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.media.jai.JAI;
import javax.media.jai.TileCache;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.printing.component.impl.ImageComponentImpl;
import org.geomajas.plugin.printing.component.impl.LabelComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendComponentImpl;
import org.geomajas.plugin.printing.component.impl.MapComponentImpl;
import org.geomajas.plugin.printing.component.impl.PageComponentImpl;
import org.geomajas.plugin.printing.component.impl.ScaleBarComponentImpl;
import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.geomajas.plugin.printing.configuration.PrintTemplateDao;
import org.geomajas.plugin.printing.document.Document;
import org.geomajas.plugin.printing.document.SinglePageDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Print service implementation based on iText. Persistence is configured through a DAO implementation of choice.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 * 
 */
@Component
@Transactional
@Api
public class PrintServiceImpl implements PrintService {

	private final Logger log = LoggerFactory.getLogger(PrintServiceImpl.class);

	private static final String ORG_GEOMAJAS = "org.geomajas.";

	private static final long MB = 1024L * 1024L;

	@Autowired
	private PrintTemplateDao printTemplateDao;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	@Qualifier("printing.printMarshaller")
	private Marshaller marshaller;

	@Autowired
	@Qualifier("printing.printMarshaller")
	private Unmarshaller unMarshaller;

	private int jaiTileCacheInMb = 64;

	private Map<String, Document> documentMap = Collections.synchronizedMap(new HashMap<String, Document>());

	public List<PrintTemplate> getAllTemplates() throws PrintingException {
		List<PrintTemplate> allTemplates = new ArrayList<PrintTemplate>();
		if (printTemplateDao != null) {
			try {
				allTemplates = printTemplateDao.findAll();
			} catch (IOException e) {
				log.warn("could not access templates in dao", e);
			}
		}
		allTemplates.addAll(getDefaults());
		List<PrintTemplate> badTemplates = new ArrayList<PrintTemplate>();
		for (PrintTemplate printTemplate : allTemplates) {
			if (printTemplate.getPage() == null) {
				try {
					Object o = unMarshaller.unmarshal(new StreamSource(new StringReader(printTemplate.getPageXml())));
					printTemplate.setPage((PageComponentImpl) o);
				} catch (Exception e) { // NOSONAR
					badTemplates.add(printTemplate);
				}
			}
		}
		allTemplates.removeAll(badTemplates);
		Collections.sort(allTemplates, new Comparator<PrintTemplate>() {

			public int compare(PrintTemplate p1, PrintTemplate p2) {
				return p1.getName().compareTo(p2.getName());
			}
		});
		return allTemplates;
	}

	public PrintTemplate createDefaultTemplate(String pageSize, boolean landscape) throws PrintingException {
		PrintTemplate template = createTemplate(pageSize, landscape);
		// calculate the sizes (if not already calculated !)
		SinglePageDocument document = new SinglePageDocument(template.getPage(), null);
		try {
			document.layout();
		} catch (PrintingException e) {
			// should not happen !
			log.warn("Unexpected problem while laying out default print template", e);
		}
		return template;
	}

	public void saveOrUpdateTemplate(PrintTemplate template) throws PrintingException {
		StringWriter sw = new StringWriter();
		try {
			marshaller.marshal(template.getPage(), new StreamResult(sw));
			template.setPageXml(sw.toString());
			printTemplateDao.merge(template);
		} catch (XmlMappingException e) {
			throw new PrintingException(e, PrintingException.PRINT_TEMPLATE_XML_PROBLEM);
		} catch (IOException e) {
			throw new PrintingException(e, PrintingException.PRINT_TEMPLATE_PERSIST_PROBLEM);
		}
	}

	/**
	 * Puts a new document in the service. The generate key is globally unique.
	 * 
	 * @param document document
	 * @return key unique key to reference the document
	 */
	public String putDocument(Document document) {
		String key = UUID.randomUUID().toString();
		documentMap.put(key, document);
		return key;
	}

	/**
	 * Gets a document from the service.
	 * 
	 * @param key
	 *            unique key to reference the document
	 * @return the document or null if no such document
	 */
	public Document removeDocument(String key) throws PrintingException {
		if (documentMap.containsKey(key)) {
			return documentMap.remove(key);
		} else {
			throw new PrintingException(PrintingException.DOCUMENT_NOT_FOUND, key);
		}
	}

	private List<PrintTemplate> getDefaults() throws PrintingException {
		List<PrintTemplate> allTemplates = new ArrayList<PrintTemplate>();
		allTemplates.add(createDefaultTemplate("A4", true));
		allTemplates.add(createDefaultTemplate("A3", true));
		allTemplates.add(createDefaultTemplate("A2", true));
		allTemplates.add(createDefaultTemplate("A0", true));
		allTemplates.add(createDefaultTemplate("A1", true));
		allTemplates.add(createDefaultTemplate("A4", false));
		allTemplates.add(createDefaultTemplate("A3", false));
		allTemplates.add(createDefaultTemplate("A2", false));
		allTemplates.add(createDefaultTemplate("A1", false));
		allTemplates.add(createDefaultTemplate("A0", false));
		return allTemplates;
	}

	public PrintTemplate createTemplate(String pageSize, boolean landscape) {
		PageComponentImpl page = createInstance(PageComponentImpl.class);
		page.setSize(pageSize, landscape);
		page.setTag(PrintTemplate.PAGE);
		MapComponentImpl map = createMap();
		ImageComponentImpl northarrow = createArrow();
		ScaleBarComponentImpl bar = createBar();
		LabelComponentImpl title = createTitle();

		LegendComponentImpl legend = new LegendComponentImpl();
		legend.setTag(PrintTemplate.LEGEND);
		map.addComponent(bar);
		map.addComponent(legend);
		map.addComponent(northarrow);
		page.addComponent(map);
		page.addComponent(title);
		PrintTemplate template = new PrintTemplate(true);
		template.setName("Default" + "-" + pageSize + "-" + (landscape ? "landscape" : "portrait"));
		template.setPage(page);
		return template;
	}

	/**
	 * returns the JAI (Java Advanced Imaging) tile cache size in MB.
	 * 
	 * @return size in MB of tile cache
	 */
	public int getJaiTileCacheInMb() {
		return jaiTileCacheInMb;
	}

	/**
	 * Sets the JAI (Java Advanced Imaging) tile cache size to the specified value.
	 * 
	 * @param jaiTileCacheInMb
	 *            size in MB of tile cache
	 * @deprecated use {@link #setJaiTileCacheInMb(int)}
	 */
	@Api
	@Deprecated
	public void setJaiTileCacheInMB(int jaiTileCacheInMb) {
		setJaiTileCacheInMb(jaiTileCacheInMb);
	}

	/**
	 * Sets the JAI (Java Advanced Imaging) tile cache size to the specified value.
	 *
	 * @param jaiTileCacheInMb
	 *            size in MB of tile cache
	 * @since 2.1.0
	 */
	@Api
	public void setJaiTileCacheInMb(int jaiTileCacheInMb) {
		this.jaiTileCacheInMb = jaiTileCacheInMb;
	}

	@PostConstruct
	protected void initJai() {
		TileCache cache = JAI.getDefaultInstance().getTileCache();
		cache.setMemoryCapacity(getJaiTileCacheInMb() * MB);
		log.info("JAI cache size set to " + cache.getMemoryCapacity() / MB + " MB");
	}

	private MapComponentImpl createMap() {
		MapComponentImpl map = createInstance(MapComponentImpl.class);
		map.getConstraint().setMarginX(20);
		map.getConstraint().setMarginY(20);
		map.setLocation(new Coordinate());
		map.setPpUnit(1.0f);
		map.setTag(PrintTemplate.MAP);
		return map;
	}

	private ImageComponentImpl createArrow() {
		ImageComponentImpl northarrow = createInstance(ImageComponentImpl.class);
		northarrow.setImagePath("/images/northarrow.gif");
		northarrow.getConstraint().setAlignmentX(LayoutConstraintInfo.RIGHT);
		northarrow.getConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		northarrow.getConstraint().setMarginX(20);
		northarrow.getConstraint().setMarginY(20);
		northarrow.getConstraint().setWidth(10);
		northarrow.setTag(PrintTemplate.ARROW);
		return northarrow;
	}

	private LabelComponentImpl createTitle() {
		LabelComponentImpl title = createInstance(LabelComponentImpl.class);
		title.setFont(new Font("Dialog", Font.ITALIC, 14));
		title.setBackgroundColor("white");
		title.setBorderColor("BLACK");
		title.setFontColor("BLACK");
		title.setText("<Title>");
		title.getConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		title.getConstraint().setAlignmentX(LayoutConstraintInfo.CENTER);
		title.getConstraint().setMarginY(40);
		title.setTag(PrintTemplate.TITLE);
		return title;
	}

	private ScaleBarComponentImpl createBar() {
		ScaleBarComponentImpl bar = createInstance(ScaleBarComponentImpl.class);
		bar.setTicNumber(3);
		bar.setUnit("m");
		bar.setTag(PrintTemplate.SCALEBAR);
		return bar;
	}

	@SuppressWarnings("unchecked")
	private <T> T createInstance(Class<T> clazz) {
		return (T) applicationContext.getBean(getPrototypeName(clazz));
	}

	private String getPrototypeName(Class serviceClass) {
		String name = serviceClass.getName();
		if (name.startsWith(ORG_GEOMAJAS)) {
			name = name.substring(ORG_GEOMAJAS.length());
		}
		return name;
	}

}
