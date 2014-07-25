package org.geomajas.layer.common.proxy;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

public class MockInterceptor implements HttpRequestInterceptor {

	private boolean called;

	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		called = true;
	}

	public boolean isCalled() {
		return called;
	}

}
