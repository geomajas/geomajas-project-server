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
package org.geomajas.layer.geotools;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Transaction manager for GeoTools layers. The functionality of this class is taken over by
 * {@link GeoToolsTransactionSynchronization}. This class only exists for backward compatibility reasons and for
 * installing a dummy transaction manager if otherwise no Spring transaction manager is defined. If you are already
 * using a Spring JDBC or Hibernate transaction manager, just configure it as usual and
 * {@link GeoToolsTransactionSynchronization} will automatically synchronize Geotools connections/transactions.
 * 
 * @author Jan De Moerloose
 */
public class GeoToolsTransactionManager extends AbstractPlatformTransactionManager {

	private static final long serialVersionUID = 190L;

	/**
	 * Constructs a new GeoToolsTransactionManager.
	 */
	public GeoToolsTransactionManager() {
		super();
		setNestedTransactionAllowed(false);
	}

	@Override
	protected Object doGetTransaction() throws TransactionException {
		// dummy transaction
		return new Object();
	}

	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
	}

}
