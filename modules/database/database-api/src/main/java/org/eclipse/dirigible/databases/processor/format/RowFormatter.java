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
package org.eclipse.dirigible.databases.processor.format;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * The Row Formatter.
 *
 * @param <T>
 *            the generic type
 */
public interface RowFormatter<T> {

	/**
	 * Write formatted row.
	 *
	 * @param columnDescriptors
	 *            the column descriptors
	 * @param resultSetMetaData
	 *            the result set meta data
	 * @param resultSet
	 *            the result set
	 * @return the t
	 * @throws SQLException
	 *             the SQL exception
	 */
	T write(List<ColumnDescriptor> columnDescriptors, ResultSetMetaData resultSetMetaData, ResultSet resultSet) throws SQLException;
}
