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
'use strict';
angular.module("ngLocale", [], ["$provide", function($provide) {
var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
$provide.value("$locale", {
  "DATETIME_FORMATS": {
    "AMPMS": [
      "vm.",
      "nm."
    ],
    "DAY": [
      "Sondag",
      "Maandag",
      "Dinsdag",
      "Woensdag",
      "Donderdag",
      "Vrydag",
      "Saterdag"
    ],
    "ERANAMES": [
      "voor Christus",
      "na Christus"
    ],
    "ERAS": [
      "v.C.",
      "n.C."
    ],
    "FIRSTDAYOFWEEK": 6,
    "MONTH": [
      "Januarie",
      "Februarie",
      "Maart",
      "April",
      "Mei",
      "Junie",
      "Julie",
      "Augustus",
      "September",
      "Oktober",
      "November",
      "Desember"
    ],
    "SHORTDAY": [
      "So",
      "Ma",
      "Di",
      "Wo",
      "Do",
      "Vr",
      "Sa"
    ],
    "SHORTMONTH": [
      "Jan.",
      "Feb.",
      "Mrt.",
      "Apr",
      "Mei",
      "Jun",
      "Jul",
      "Aug",
      "Sep",
      "Okt",
      "Nov",
      "Des"
    ],
    "WEEKENDRANGE": [
      5,
      6
    ],
    "fullDate": "EEEE, dd MMMM y",
    "longDate": "dd MMMM y",
    "medium": "dd MMM y h:mm:ss a",
    "mediumDate": "dd MMM y",
    "mediumTime": "h:mm:ss a",
    "short": "y-MM-dd h:mm a",
    "shortDate": "y-MM-dd",
    "shortTime": "h:mm a"
  },
  "NUMBER_FORMATS": {
    "CURRENCY_SYM": "R",
    "DECIMAL_SEP": ",",
    "GROUP_SEP": "\u00a0",
    "PATTERNS": [
      {
        "gSize": 3,
        "lgSize": 3,
        "maxFrac": 3,
        "minFrac": 0,
        "minInt": 1,
        "negPre": "-",
        "negSuf": "",
        "posPre": "",
        "posSuf": ""
      },
      {
        "gSize": 3,
        "lgSize": 3,
        "maxFrac": 2,
        "minFrac": 2,
        "minInt": 1,
        "negPre": "-\u00a4",
        "negSuf": "",
        "posPre": "\u00a4",
        "posSuf": ""
      }
    ]
  },
  "id": "af",
  "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
});
}]);
