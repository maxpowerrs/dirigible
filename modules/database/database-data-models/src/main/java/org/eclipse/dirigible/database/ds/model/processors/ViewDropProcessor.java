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
package org.eclipse.dirigible.database.ds.model.processors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.database.ds.model.DataStructureViewModel;
import org.eclipse.dirigible.database.ds.model.IDataStructureModel;
import org.eclipse.dirigible.database.sql.SqlFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The View Drop Processor.
 */
public class ViewDropProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ViewDropProcessor.class);

	/**
	 * Execute the corresponding statement.
	 *
	 * @param connection the connection
	 * @param viewModel the view model
	 * @throws SQLException the SQL exception
	 */
	public static void execute(Connection connection, DataStructureViewModel viewModel) throws SQLException {
		boolean caseSensitive = Boolean.parseBoolean(Configuration.get(IDataStructureModel.DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE, "false"));
		String viewName = viewModel.getName();
		if (caseSensitive) {
			viewName = "\"" + viewName + "\"";
		}
		logger.info("Processing Drop View: " + viewName);
		if (SqlFactory.getNative(connection).exists(connection, viewName)) {
			String sql = SqlFactory.getNative(connection).drop().view(viewName).build();
			logger.info(sql);
			PreparedStatement statement = connection.prepareStatement(sql);
			try {
				statement.executeUpdate();
			} catch (SQLException e) {
				logger.error(sql);
				logger.error(e.getMessage(), e);
			} finally {
				if (statement != null) {
					statement.close();
				}
			}
		}
	}

}
