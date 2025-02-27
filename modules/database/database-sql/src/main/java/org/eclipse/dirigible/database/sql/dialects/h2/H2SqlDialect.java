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
package org.eclipse.dirigible.database.sql.dialects.h2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.dirigible.database.sql.builders.AlterBranchingBuilder;
import org.eclipse.dirigible.database.sql.builders.CreateBranchingBuilder;
import org.eclipse.dirigible.database.sql.builders.DropBranchingBuilder;
import org.eclipse.dirigible.database.sql.builders.records.DeleteBuilder;
import org.eclipse.dirigible.database.sql.builders.records.InsertBuilder;
import org.eclipse.dirigible.database.sql.builders.records.SelectBuilder;
import org.eclipse.dirigible.database.sql.builders.records.UpdateBuilder;
import org.eclipse.dirigible.database.sql.dialects.DefaultSqlDialect;

/**
 * The H2 SQL Dialect.
 */
public class H2SqlDialect extends
		DefaultSqlDialect<SelectBuilder, InsertBuilder, UpdateBuilder, DeleteBuilder, CreateBranchingBuilder, AlterBranchingBuilder, DropBranchingBuilder, H2NextValueSequenceBuilder, H2LastValueIdentityBuilder> {

	public static final Set<String> FUNCTIONS = Collections.synchronizedSet(new HashSet<String>(Arrays.asList(new String[] {
			"abs",
			"acos",
			"asin",
			"atan",
			"cos",
			"cosh",
			"cot",
			"sin",
			"sinh",
			"tan",
			"tanh",
			"atan2",
			"bitand",
			"bitget",
			"bitnot",
			"bitor",
			"bitxor",
			"lshift",
			"rshift",
			"mod",
			"ceiling",
			"degrees",
			"exp",
			"floor",
			"ln",
			"log",
			"log10",
			"ora_hash",
			"radians",
			"sqrt",
			"pi",
			"power",
			"rand",
			"random_uuid",
			"round",
			"roundmagic",
			"secure_rand",
			"sign",
			"encrypt",
			"decrypt",
			"hash",
			"truncate",
			"compress",
			"expand",
			"zero",
			"ascii",
			"bit_length",
			"length",
			"octet_length",
			"char",
			"concat",
			"concat_ws",
			"difference",
			"hextoraw",
			"rawtohex",
			"instr",
			"lower",
			"upper",
			"left",
			"right",
			"locate",
			"position",
			"lpad",
			"rpad",
			"ltrim",
			"rtrim",
			"trim",
			"regexp_replace",
			"regexp_like",
			"repeat",
			"replace",
			"soundex",
			"space",
			"stringdecode",
			"stringencode",
			"stringtoutf8",
			"substring",
			"utf8tostring",
			"quote_ident",
			"xmlattr",
			"xmlnode",
			"xmlcomment",
			"xmlcdata",
			"xmlstartdoc",
			"xmltext",
			"to_char",
			"translate",
			"current_date",
			"current_time",
			"current_timestamp",
			"localtime",
			"localtimestamp",
			"dateadd",
			"datediff",
			"dayname",
			"day_of_month",
			"day_of_week",
			"iso_day_of_week",
			"day_of_year",
			"extract",
			"formatdatetime",
			"hour",
			"minute",
			"month",
			"monthname",
			"parsedatetime",
			"quarter",
			"second",
			"week",
			"iso_week",
			"year",
			"iso_year",
			"array_get",
			"array_length",
			"array_contains",
			"array_cat",
			"array_append",
			"array_slice",
			"autocommit",
			"cancel_session",
			"cast",
			"coalesce",
			"convert",
			"currval",
			"csvread",
			"csvwrite",
			"current_schema",
			"current_catalog",
			"database_path",
			"decode",
			"disk_space_used",
			"signal",
			"estimated_envelope",
			"file_read",
			"file_write",
			"greatest",
			"identity",
			"ifnull",
			"least",
			"lock_mode",
			"lock_timeout",
			"link_schema",
			"memory_free",
			"memory_used",
			"nextval",
			"nullif",
			"nvl2",
			"readonly",
			"rownum",
			"scope_identity",
			"session_id",
			"set",
			"table",
			"transaction_id",
			"truncate_value",
			"unnest",
			"user",
			"h2version",
			"json functions",
			"json_object",
			"json_array",

			"avg",
			"max",
			"min",
			"sum",
			"every",
			"any",
			"count",
			
			
			"stddev_pop",
			"stddev_samp",
			"var_pop",
			"var_samp",
			"bit_and",
			"bit_or",
			"selectivity",
			"envelope",
			"listagg",
			"array_agg",
			"rank",
			"dense_rank",
			"percent_rank",
			"cume_dist",
			"percentile_cont",
			"percentile_disc",
			"median",
			"mode",
			"json",
			"json_objectagg",
			"json_arrayagg",
			
			"and",
			"or",
			"between",
			"binary",
			"case",
			"div",
			"in",
			"is",
			"not",
			"null",
			"like",
			"rlike",
			"xor"

			})));
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.dialects.DefaultSqlDialect#nextval(java.lang.String)
	 */
	@Override
	public H2NextValueSequenceBuilder nextval(String sequence) {
		return new H2NextValueSequenceBuilder(this, sequence);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.dialects.DefaultSqlDialect#nextval(java.lang.String)
	 */
	@Override
	public H2LastValueIdentityBuilder lastval(String... args) {
		return new H2LastValueIdentityBuilder(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.ISqlDialect#isSynonymSupported()
	 */
	@Override
	public boolean isSynonymSupported() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.ISqlDialect#getFunctionsNames()
	 */
	@Override
	public Set<String> getFunctionsNames() {
		return FUNCTIONS;
	}

}
