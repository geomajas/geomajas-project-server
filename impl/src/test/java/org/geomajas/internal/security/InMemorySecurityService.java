package org.geomajas.internal.security;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.security.Authentication;
import org.geomajas.security.SecurityService;

/**
 * Simple in memory security service.
 * 
 * @author Jan De Moerloose
 * 
 */
public class InMemorySecurityService implements SecurityService {

	private Map<String, Authentication> authentications = new HashMap<String, Authentication>();

	public String getId() {
		return "InMemorySecurityService";
	}

	public Authentication getAuthentication(String authenticationToken) {
		return authentications.get(authenticationToken);
	}

	public Authentication put(String key, Authentication value) {
		return authentications.put(key, value);
	}

	public Authentication remove(String key) {
		return authentications.remove(key);
	}

	public void clear() {
		authentications.clear();
	}

}
