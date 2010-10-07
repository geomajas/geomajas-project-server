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

package org.geomajas.internal.service;

import org.geomajas.service.TestRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Real {@link TestRecorder} implementation for use in tests.
 *
 * @author Joachim Van der Auwera
 */
public class RealTestRecorder implements TestRecorder {

	private Logger log = LoggerFactory.getLogger(RealTestRecorder.class);

	private Map<String, List<String>> messages = new HashMap<String, List<String>>();

	public void record(Object groupObj, String message) {
		String group = "" + groupObj;
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

	public void clear() {
		messages.clear();
	}

	public String matches(Object groupObj, String... compare) {
		String group = "" + groupObj;
		List<String> list = messages.get(group);
		if (null == list) {
			if (compare.length > 0) {
				return "no messages for group";
			} else {
				return "";
			}
		}
		for (int i = 0; i < compare.length; i++) {
			if (i >= list.size()) {
				return "too little recorded messages, only " + i + " available";
			}
			String str1 = compare[i];
			String str2 = list.get(i);
			if (!str2.equals(str1)) {
				return "match failed at position " + i + ", requested [" + str1 + "] got [" + str2 + "]";
			}
		}
		if (list.size() > compare.length) {
			return "more recorded messages then tested";
		}
		return "";
	}
}
