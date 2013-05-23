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
package org.geomajas.gwt.server.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.geomajas.annotation.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

/**
 * Implementation of {@link SerializationPolicyLocator} that searches for the policy file in a list of root resources.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
public class ResourceSerializationPolicyLocator implements SerializationPolicyLocator {

	private final Logger log = LoggerFactory.getLogger(ResourceSerializationPolicyLocator.class);

	private List<Resource> policyRoots = new ArrayList<Resource>();

	@Override
	public SerializationPolicy loadPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {

		SerializationPolicy serializationPolicy = null;
		String serializationPolicyFilePath = SerializationPolicyLoader.getSerializationPolicyFileName(strongName);

		for (Resource directory : policyRoots) {
			Resource policy;
			try {
				policy = directory.createRelative(serializationPolicyFilePath);
				if (policy.exists()) {
					// Open the RPC resource file and read its contents.
					InputStream is = policy.getInputStream();
					try {
						if (is != null) {
							try {
								serializationPolicy = SerializationPolicyLoader.loadFromStream(is, null);
								break;
							} catch (ParseException e) {
								log.error("Failed to parse the policy file '" + policy + "'", e);
							} catch (IOException e) {
								log.error("Could not read the policy file '" + policy + "'", e);
							}
						} else {
							// existing spring resources should not return null, log anyways
							log.error("Unexpected null stream from the policy file resource '" + policy + "'");
						}
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (IOException e) {
								// Ignore this error
							}
						}
					}
				}
			} catch (IOException e1) {
				log.debug("Could not create the policy resource '" + serializationPolicyFilePath
						+ "' from parent resource " + directory, e1);
			}
		}
		if (serializationPolicy == null) {
			String message = "The serialization policy file '" + serializationPolicyFilePath
					+ "' was not found; did you forget to include it in this deployment?";
			log.error(message);
		}
		return serializationPolicy;
	}

	/**
	 * Returns the list of policy root resources (e.g. file system folder or classpath location) that contain policy
	 * files.
	 * 
	 * @return the list of policy resources
	 */
	public List<Resource> getPolicyRoots() {
		return policyRoots;
	}

	/**
	 * Sets the list of policy root resources (e.g. file system folder or classpath location) that contain policy files.
	 * 
	 * @param policyRoots list of policy root resources.
	 */
	public void setPolicyRoots(List<Resource> policyRoots) {
		this.policyRoots = policyRoots;
	}

}
