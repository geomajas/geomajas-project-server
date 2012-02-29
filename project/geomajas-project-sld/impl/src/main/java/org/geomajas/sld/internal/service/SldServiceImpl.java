/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.internal.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.service.SldException;
import org.geomajas.sld.service.SldService;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
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
 * 
 */
public class SldServiceImpl implements SldService {

	private final Logger log = LoggerFactory.getLogger(SldServiceImpl.class);

	private Map<String, StyledLayerDescriptorInfo> allSlds = new ConcurrentHashMap<String, StyledLayerDescriptorInfo>();

	private Resource directory;

	public Resource getDirectory() {
		return directory;
	}

	public void setDirectory(Resource directory) {
		this.directory = directory;
	}
	
	public List<StyledLayerDescriptorInfo> findAll() throws SldException {
		return new ArrayList<StyledLayerDescriptorInfo>(allSlds.values());
	}

	public StyledLayerDescriptorInfo findByName(String name) throws SldException {
		return allSlds.get(name);
	}

	public StyledLayerDescriptorInfo saveOrUpdate(StyledLayerDescriptorInfo sld) throws SldException {
		validate(sld);
		allSlds.put(sld.getName(), sld);
		writeChanges(sld);
		StyledLayerDescriptorInfo newValue = allSlds.get(sld.getName());
		return newValue;
	}

	public StyledLayerDescriptorInfo create(StyledLayerDescriptorInfo sld) throws SldException {

		if (allSlds.containsKey(sld.getName())) {
			throw new SldException("SLD with name " + sld.getName() + " already exists.");
		}

		return saveOrUpdate(sld);

	}

	public boolean remove(String name) throws SldException {
		return allSlds.remove(name) != null;
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() throws SldException {
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
							IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
							IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
							Object object = uctx.unmarshalDocument(new FileReader(file));
							StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
							String fileName = StringUtils.stripFilenameExtension(file.getName());
							if (sld.getName() == null) {
								sld.setName(fileName);
							}
							log.info("added sld {} to service", fileName);
							allSlds.put(sld.getName(), sld);
						}
					}
				}
			} catch (Exception e) { // NOSONAR
				throw new SldException("Could not initialize SLD service", e);
			}
		}
	}

	public void validate(Object obj) throws SldException {
		IBindingFactory bfact;
		try {
			bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
			IMarshallingContext mctx = bfact.createMarshallingContext();
			StringWriter writer = new StringWriter();
			mctx.setOutput(writer);
			mctx.marshalDocument(obj);
			if (log.isDebugEnabled()) {
				log.debug(writer.toString());
			}
		} catch (JiBXException e) {
			throw new SldException("Validation error", e);
		}
	}

	private void writeChanges(StyledLayerDescriptorInfo sld) {
		IBindingFactory bfact;
		try {
			bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
			IMarshallingContext mctx = bfact.createMarshallingContext();
			mctx.setIndent(4);
			File outFile = new File(getDirectory().getFile(), sld.getName() + "-modified.sld");
			FileWriter writer = new FileWriter(outFile);
			mctx.setOutput(writer);
			mctx.marshalDocument(sld);
			log.info("SLD " + outFile.getAbsolutePath() + " written");
		} catch (JiBXException e) {
		} catch (IOException e) {
		}
	}


}
