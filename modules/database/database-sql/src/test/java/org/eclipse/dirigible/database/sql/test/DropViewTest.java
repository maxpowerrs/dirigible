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
package org.eclipse.dirigible.database.sql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.database.sql.SqlFactory;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class DropViewTest.
 */
public class DropViewTest {

	/**
	 * Drop table.
	 */
	@Test
	public void dropTable() {
		String sql = SqlFactory.getDefault().drop().view("CUSTOMERS_VIEW").build();

		assertNotNull(sql);
		assertEquals("DROP VIEW CUSTOMERS_VIEW", sql);
	}

	/**
	 * Drop table case sensitive.
	 */
	@Test
	public void dropTableCaseSensitive() {
		Configuration.set("DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE", "true");
		try {
			String sql = SqlFactory.getDefault().drop().view("CUSTOMERS_VIEW").build();
			
			assertNotNull(sql);
			assertEquals("DROP VIEW \"CUSTOMERS_VIEW\"", sql);
		} finally {
			Configuration.set("DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE", "false");
		}
	}
}
