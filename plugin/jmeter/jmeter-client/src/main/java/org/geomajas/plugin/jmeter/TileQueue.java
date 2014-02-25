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
package org.geomajas.plugin.jmeter;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.geomajas.command.CommandRequest;

/**
 * Concurrent queue for tile requests.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TileQueue {

	private ConcurrentLinkedQueue<CommandRequest> queue = new ConcurrentLinkedQueue<CommandRequest>();

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public void addRequest(CommandRequest request) {
		queue.offer(request);
	}

	public CommandRequest getNextRequest() {
		return queue.poll();
	}

	public int getSize() {
		return queue.size();
	}

}
