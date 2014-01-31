/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.service.TestRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Real {@link TestRecorder} implementation for use in tests.
 *
 * @author Joachim Van der Auwera
 */
public class RealTestRecorder implements TestRecorder {

	private final Logger log = LoggerFactory.getLogger(RealTestRecorder.class);

	private Map<String, List<String>> messages = new HashMap<String, List<String>>();

	@Override
	public void record(Object groupObj, String message) {
		String group = "" + groupObj; // NOSONAR
		List<String> list = messages.get(group);
		if (null == list) {
			list = new ArrayList<String>();
			messages.put(group, list);
		}
		if (null != message) {
			list.add(message);
			log.debug("[{}] {}", group, message);
		}
	}

	@Override
	public void clear() {
		messages.clear();
	}

	@Override
	public String matches(Object groupObj, String... compare) {
		String group = "" + groupObj; // NOSONAR
		List<String> list = messages.get(group);
		if (null == list) {
			if (compare.length > 0) {
				return "no messages for group, expected " + Arrays.asList(compare);
			} else {
				return "";
			}
		}
		for (int i = 0; i < compare.length; i++) {
			if (i >= list.size()) {
				return "too little recorded messages, only " + i + " available, " + list;
			}
			String str1 = compare[i];
			String str2 = list.get(i);
			if (!str2.equals(str1)) {
				return "match failed at position " + i + ", requested [" + str1 + "] got [" + str2 + "], " + list;
			}
		}
		if (list.size() > compare.length) {
			return "more recorded messages then tested, first " + compare.length + " ok, " + list;
		}
		return "";
	}
}
