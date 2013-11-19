package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.plugin.staticsecurity.security.dto.AbstractUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilterVisitor;


public class CustomFilter extends AbstractUserFilter {

	private String userNamePref;

	public CustomFilter(String userNamePref) {
		this.userNamePref = userNamePref;
	}
	
	public String getUserNamePref() {
		return userNamePref;
	}

	@Override
	public Object accept(UserFilterVisitor filterVisitor, Object extraData) {
		return filterVisitor.visit(this, extraData);
	}

}
