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

package org.geomajas.servlet.mvc;

import org.geomajas.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor which can be applied to controllers to ensure the open-session-in-view transaction management.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
public class TransactionalInterceptor extends HandlerInterceptorAdapter {

	private static final String TX_KEY = "org.geomajas.servlet.mvc.TransactionalInterceptor";

	@Autowired(required = false)
	private PlatformTransactionManager transactionManager;

	private TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();

	/**
	 * Set the transaction definition which should be used.
	 *
	 * @param transactionDefinition transaction definition
	 */
	public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (null != transactionManager) {
			TransactionStatus tx = transactionManager.getTransaction(transactionDefinition);
			TransactionSynchronizationManager.bindResource(TX_KEY, tx);
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (null != transactionManager) {
			TransactionStatus tx = (TransactionStatus) TransactionSynchronizationManager.unbindResource(TX_KEY);
			if (tx.isRollbackOnly()) {
				transactionManager.rollback(tx);
			} else {
				transactionManager.commit(tx);
			}
		}
		super.afterCompletion(request, response, handler, ex);
	}
}
