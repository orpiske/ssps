/**
 * Copyright 2013 Otavio Rodolfo Piske
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.orpiske.ssps.common.groovy;


import groovy.lang.GroovyClassLoader;
import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Utility class to help managing the groovy classpath
 */
public class GroovyClasspathHelper {

	private static final Logger logger
			= Logger.getLogger(GroovyClasspathHelper.class);

	private static final PropertiesConfiguration config
			= ConfigurationWrapper.getConfig();

	private static GroovyClasspathHelper instance;

	private GroovyClassLoader loader;

	private GroovyClasspathHelper() {
		loader = new GroovyClassLoader(getClass().getClassLoader());
	}


	/**
	 * Adds a path to the classpath
	 * @param path the path
	 */
	public void addClasspath(final String path) {
		loader.addClasspath(path);
	}


	/**
	 * Adds a file or directory to the classpath
	 * @param file the file object pointing to the file or directory
	 */
	public void addClasspath(final File file) throws IOException {
		if (file.exists()) {
			loader.addClasspath(file.getCanonicalPath());
		}
	}


	/**
	 * Gets the Groovy class loader instance
	 * @return the Groovy class loader instance
	 */
	public GroovyClassLoader getLoader() {
		return loader;
	}


	/**
	 * Gets the classpath helper instance
	 * @return the Groovy class loader instance
	 */
	public static GroovyClasspathHelper getInstance() {
		if (instance == null) {
			instance = new GroovyClasspathHelper();
		}

		return instance;
	}
}
