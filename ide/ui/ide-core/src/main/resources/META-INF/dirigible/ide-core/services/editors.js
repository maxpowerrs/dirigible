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
let extensions = require('core/v4/extensions');
let response = require('http/v4/response');

let editors = [];
let editorExtensions = extensions.getExtensions('ide-editor');

for (let i = 0; editorExtensions != null && i < editorExtensions.length; i++) {
	let module = editorExtensions[i];
	try {
		let editorExtension = require(module);
		let editor = editorExtension.getEditor();
		editors.push(editor);
	} catch (error) {
		console.error('Error occured while loading metadata for the editor: ' + module);
		console.error(error);
	}
}

editors = editors.sort(function (a, b) {
	if (a.defaultEditor) {
		return -1;
	} else if (b.defaultEditor) {
		return 1;
	}
	return 0;
});

response.println(JSON.stringify(editors));