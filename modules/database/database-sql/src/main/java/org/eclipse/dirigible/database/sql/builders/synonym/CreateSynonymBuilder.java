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
package org.eclipse.dirigible.database.sql.builders.synonym;

import org.eclipse.dirigible.database.sql.ISqlDialect;
import org.eclipse.dirigible.database.sql.builders.AbstractCreateSqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Create Synonym Builder.
 */
public class CreateSynonymBuilder extends AbstractCreateSqlBuilder {

	private static final Logger logger = LoggerFactory.getLogger(CreateSynonymBuilder.class);

	private String synonym = null;
	
	private String source = null;


	/**
	 * Instantiates a new creates the synonym builder.
	 *
	 * @param dialect
	 *            the dialect
	 * @param synonym
	 *            the synonym
	 */
	public CreateSynonymBuilder(ISqlDialect dialect, String synonym) {
		super(dialect);
		this.synonym = synonym;
	}

	/**
	 * Source.
	 *
	 * @param source
	 *            the source
	 * @return the creates the synonym builder
	 */
	public CreateSynonymBuilder forSource(String source) {
		logger.trace("source: " + source);
		this.source = source;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.ISqlBuilder#generate()
	 */
	@Override
	public String generate() {

		StringBuilder sql = new StringBuilder();

		// CREATE
		generateCreate(sql);

		// SYNONYM
		generateSynonym(sql);

		// Source
		generateSource(sql);

		String generated = sql.toString();

		logger.trace("generated: " + generated);

		return generated;
	}

	/**
	 * Generate synonym.
	 *
	 * @param sql
	 *            the sql
	 */
	protected void generateSynonym(StringBuilder sql) {
		String synonymName = (isCaseSensitive()) ? encapsulate(this.getSynonym()) : this.getSynonym();
		sql.append(SPACE)/*.append(KEYWORD_PUBLIC).append(SPACE)*/.append(KEYWORD_SYNONYM).append(SPACE).append(synonymName);
	}

	/**
	 * Generate start.
	 *
	 * @param sql
	 *            the sql
	 */
	protected void generateSource(StringBuilder sql) {
		String sourceName = (isCaseSensitive()) ? encapsulate(this.getSource()) : this.getSource();
		 sql.append(SPACE)
			 .append(KEYWORD_FOR)
			 .append(SPACE)
			 .append(sourceName);
	}

	/**
	 * Gets the synonym.
	 *
	 * @return the synonym
	 */
	public String getSynonym() {
		return synonym;
	}
	
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

}
