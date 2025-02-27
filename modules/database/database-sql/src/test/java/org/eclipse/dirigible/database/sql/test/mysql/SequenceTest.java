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
package org.eclipse.dirigible.database.sql.test.mysql;

import org.eclipse.dirigible.database.sql.SqlFactory;
import org.eclipse.dirigible.database.sql.dialects.mysql.MySQLSqlDialect;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The Class SequenceTest.
 */
public class SequenceTest {

    /**
     * Creates the sequence.
     */
    @Test
    public void createSequence() {
        try {
            String sql = SqlFactory.getNative(new MySQLSqlDialect()).create().sequence("CUSTOMERS_SEQUENCE").build();
        } catch (Exception e) {
            return;
        }

        fail("Does MySQL support Sequences?");
    }

    @Test
    public void alterSequence() {
        String sql = SqlFactory.getNative(new MySQLSqlDialect())
                .alter()
                .sequence("CUSTOMERS_SEQUENCE")
                .build();

        assertNotNull(sql);
        assertEquals("ALTER SEQUENCE CUSTOMERS_SEQUENCE", sql);
    }

    /**
     * Drop sequnce.
     */
    @Test
    public void dropSequnce() {
        try {
            String sql = SqlFactory.getNative(new MySQLSqlDialect()).drop().sequence("CUSTOMERS_SEQUENCE").build();
        } catch (Exception e) {
            return;
        }

        fail("Does MySQL support Sequences?");
    }

    /**
     * Nextval sequnce.
     */
    @Test
    public void nextvalSequnce() {
        try {
            String sql = SqlFactory.getNative(new MySQLSqlDialect()).nextval("CUSTOMERS_SEQUENCE").build();
        } catch (Exception e) {
            return;
        }

        fail("Does MySQL support Sequences?");
    }

}
