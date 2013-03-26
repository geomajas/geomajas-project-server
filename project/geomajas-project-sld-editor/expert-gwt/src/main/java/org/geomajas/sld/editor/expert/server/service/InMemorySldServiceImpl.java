/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.expert.server.service;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;
import org.geomajas.sld.editor.expert.client.domain.SldInfoImpl;
import org.geomajas.sld.service.SldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * Default implementation of the SLD service using an in-memory map. This service loads a configurable directory of SLD
 * files at startup.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 */
public class InMemorySldServiceImpl implements org.geomajas.sld.editor.expert.server.service.SldService {

	private final Logger log = LoggerFactory.getLogger(InMemorySldServiceImpl.class);

	private static final String FILE_ENCODING = "UTF-8";
	
	private Map<String, RawSld> allSlds = new ConcurrentHashMap<String, RawSld>();

	private Resource directory;

	public Resource getDirectory() {
		return directory;
	}

	public void setDirectory(Resource directory) {
		this.directory = directory;
	}
	
	@PostConstruct
	void init() throws SldException {
		if (getDirectory() != null) {
			try {
				if (getDirectory().getFile().exists()) {
					if (getDirectory().getFile().isDirectory()) {
						File[] sldFiles = getDirectory().getFile().listFiles(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.endsWith(".sld") || name.endsWith(".xml");
							}
						});
						for (File file : sldFiles) {
							RawSld raw = new RawSld();
							raw.setXml(FileUtils.readFileToString(file, FILE_ENCODING));
							String fileName = StringUtils.stripFilenameExtension(file.getName());
							raw.setName(fileName);
							raw.setTitle(fileName);
							log.info("added sld '{}' to service", fileName);
							allSlds.put(raw.getName(), raw);
						}
					}
				}
			} catch (Exception e) { // NOSONAR
				throw new SldException("Could not initialize SLD service", e);
			}
		}
	}

	// ---------------------------------------------------------------
	
	public List<SldInfo> findTemplates() throws SldException {
		List<SldInfo> res = new ArrayList<SldInfo>();
		for (RawSld raw : allSlds.values()) {
			res.add(new SldInfoImpl(raw.getName(), raw.getTitle()));
		}
		return res;
	}

	public RawSld findTemplateByName(String name) throws SldException {
		return allSlds.get(name);
	}

//	public void validate(Object obj) throws SldException {
//		IBindingFactory bfact;
//		try {
//			bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
//			IMarshallingContext mctx = bfact.createMarshallingContext();
//			StringWriter writer = new StringWriter();
//			mctx.setOutput(writer);
//			mctx.marshalDocument(obj);
//			if (log.isDebugEnabled()) {
//				log.debug(writer.toString());
//			}
//		} catch (JiBXException e) {
//			throw new SldException("Validation error", e);
//		}
//	}


	public RawSld saveOrUpdate(RawSld sld) throws SldException {
		return null;
//		validate(sld);
//		allSlds.put(sld.getName(), sld);
//		writeChanges(sld);
//		StyledLayerDescriptorInfo newValue = allSlds.get(sld.getName());
//		return newValue;
	}

	
	// ---------------------------------------------------------------
	
//	private void writeChanges(StyledLayerDescriptorInfo sld) {
//	IBindingFactory bfact;
//	try {
//		bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
//		IMarshallingContext mctx = bfact.createMarshallingContext();
//		mctx.setIndent(4);
//		File outFile = new File(getDirectory().getFile(), sld.getName() + "-modified.sld");
//		FileWriter writer = new FileWriter(outFile);
//		mctx.setOutput(writer);
//		mctx.marshalDocument(sld);
//		log.info("SLD " + outFile.getAbsolutePath() + " written");
//	} catch (JiBXException e) {
//	} catch (IOException e) {
//	}
//}


}
