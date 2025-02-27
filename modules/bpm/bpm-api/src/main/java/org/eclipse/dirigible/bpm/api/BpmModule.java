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
package org.eclipse.dirigible.bpm.api;

import static java.text.MessageFormat.format;

import java.util.ServiceLoader;

import org.eclipse.dirigible.commons.api.module.AbstractDirigibleModule;
import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Module for managing BPM Providers instantiation and binding.
 */
public class BpmModule extends AbstractDirigibleModule {

	private static final Logger logger = LoggerFactory.getLogger(BpmModule.class);

	private static final ServiceLoader<IBpmProvider> BPM_PROVIDERS = ServiceLoader.load(IBpmProvider.class);

	private static final String MODULE_NAME = "BPM Module";

	private static IBpmProvider provider;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.dirigible.commons.api.module.AbstractDirigibleModule#configure()
	 */
	@Override
	public void configure() {
		Configuration.loadModuleConfig("/dirigible-bpm.properties");

		String bpmProvider = Configuration.get(IBpmProvider.DIRIGIBLE_BPM_PROVIDER);
		if (bpmProvider == null) {
			throw new IllegalStateException("No BPM Process Engine available");
		}
		for (IBpmProvider next : BPM_PROVIDERS) {
			logger.trace(format("Installing BPM Provider [{0}:{1}] ...", next.getType(), next.getName()));
			if (next.getType().equals(bpmProvider)) {
				StaticObjects.set(StaticObjects.BPM_PROVIDER, next);
				provider = next;
				break;
			}
			logger.trace(format("Done installing BPM Provider [{0}:{1}].", next.getType(), next.getName()));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.dirigible.commons.api.module.AbstractDirigibleModule#getName(
	 * )
	 */
	@Override
	public String getName() {
		return MODULE_NAME;
	}

	/**
	 * The bound process engine
	 *
	 * @return the process engine
	 */
	public static Object getProcessEngine() {
		if (provider != null) {
			return provider.getProcessEngine();
		}
		throw new IllegalStateException("BPM Provider has not been initialized.");
	}
	
	/**
	 * The bound process engine provider
	 *
	 * @return the process engine provider
	 */
	public static IBpmProvider getProcessEngineProvider() {
		if (provider != null) {
			return provider;
		}
		throw new IllegalStateException("BPM Provider has not been initialized.");
	}

}
