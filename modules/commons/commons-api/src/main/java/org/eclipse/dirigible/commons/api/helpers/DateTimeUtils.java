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
package org.eclipse.dirigible.commons.api.helpers;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class DateTimeUtils {
	
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(""
	        + "[yyyy/MM/dd]"
	        + "[yyyy-MM-dd]"
	        + "[dd[ ]MMM[ ]yyyy"
	    , Locale.ENGLISH);
	
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(""
	        + "[HH:mm:ss.SSSSSS]"
	        + "[yyyy-MM-dd]"
	        + "[HH:mm:ss[.SSS][ Z]]"
	    , Locale.ENGLISH);
	
	private static final DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(""
	        + "[yyyy/MM/dd HH:mm:ss.SSSSSS]"
	        + "[yyyy-MM-dd HH:mm:ss.SSSSSS]"
	        + "[yyyy/MM/dd HH:mm:ss[.SSS][ Z]]"
	        + "[yyyy-MM-dd HH:mm:ss[.SSS][ Z]]"
	        + "[dd[ ]MMM[ ]yyyy:HH:mm:ss.SSS[ Z]]"
	    , Locale.ENGLISH);
	
	public static Date parseDate(String value) {
		value = sanitize(value);
		return Date.valueOf(LocalDate.parse(value, dateFormatter));
	}
	
	public static Time parseTime(String value) {
		value = sanitize(value);
		value = timezonize(value);
		return Time.valueOf(LocalTime.parse(value, timeFormatter));
	}

	public static Timestamp parseDateTime(String value) {
		value = sanitize(value);
		value = timezonize(value);
		return Timestamp.valueOf(LocalDateTime.parse(value, datetimeFormatter));
	}
	
	private static String sanitize(String value) {
		if (value != null && value.startsWith("\"") && value.endsWith("\"")) {
			value = value.substring(1, value.length() - 1);
		}
		if (value != null && value.startsWith("'") && value.endsWith("'")) {
			value = value.substring(1, value.length() - 1);
		}
		return value.trim();
	}
	
	private static String timezonize(String value) {
		if (value != null && value.indexOf('.') == value.length()-8) {
			value = value.substring(0, value.indexOf('.') + 4) + " +" + value.substring(value.indexOf('.') + 4);
		}
		return value;
	}
	
	private String numberize(String value) {
		if (StringUtils.isEmpty(value)) {
			value = "0";
		}
		return value;
	}

}
