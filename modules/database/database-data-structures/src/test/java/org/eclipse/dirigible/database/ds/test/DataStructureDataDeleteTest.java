/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.database.ds.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.core.test.AbstractDirigibleTest;
import org.eclipse.dirigible.database.ds.model.DataStructureDataDeleteModel;
import org.eclipse.dirigible.database.ds.model.DataStructureModelFactory;
import org.eclipse.dirigible.database.ds.synchronizer.DataStructuresSynchronizer;
import org.eclipse.dirigible.database.persistence.PersistenceManager;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class DataStructureDataDeleteTest.
 */
public class DataStructureDataDeleteTest extends AbstractDirigibleTest {

	/** The data structure core service. */
	private DataStructuresSynchronizer dataStructuresSynchronizer;

	/** The datasource */
	private DataSource dataSource;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		this.dataStructuresSynchronizer = new DataStructuresSynchronizer();
		this.dataSource = (DataSource) StaticObjects.get(StaticObjects.DATASOURCE);
	}

	/**
	 * Delete data
	 */
	@Test
	public void deleteData() {
		try {
			InputStream in = DataStructureDataDeleteTest.class.getResourceAsStream("/orders.delete");
			try {
				String dataFile = IOUtils.toString(in, StandardCharsets.UTF_8);
				DataStructureDataDeleteModel data = DataStructureModelFactory.parseDelete("/orders.delete", dataFile);
				assertEquals("1|Order 1|11.11", data.getContent()); // only the first column matters anyway
				Connection connection = null;
				try {
					connection = dataSource.getConnection();

					PersistenceManager<Order> persistenceManager = new PersistenceManager<Order>();
					if (!persistenceManager.tableExists(connection, Order.class)) {
						persistenceManager.tableCreate(connection, Order.class);
					} else {
						persistenceManager.tableDrop(connection, Order.class);
						persistenceManager.tableCreate(connection, Order.class);
					}

					Order order = new Order();
					order.setId(1);
					order.setSubject("Subject 1");
					order.setAmount(54.54);
					persistenceManager.insert(connection, order);

					List<Order> orders = persistenceManager.query(connection, Order.class, "SELECT * FROM ORDERS");
					assertEquals(1, orders.size());
					Order orderFind = orders.get(0);

					assertEquals("Subject 1", orderFind.getSubject());

					dataStructuresSynchronizer.executeDeleteUpdate(data);

					Order orderDelete = persistenceManager.find(connection, Order.class, 1);

					assertNull(orderDelete);

					persistenceManager.tableDrop(connection, Order.class);
					boolean exists = persistenceManager.tableExists(connection, Order.class);
					assertFalse(exists);

				} finally {
					if (connection != null) {
						connection.close();
					}
				} 
			} finally {
				if (in != null) {
					in.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
