package org.geomajas.plugin.jmeter;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.geomajas.command.CommandRequest;

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
