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

package net.orpiske.sdm.lib.rdc;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;


/**
 * This class loads the relevant run time data that can be used by the scripts
 */
public class RuntimeDataLoader {
	private File scriptFile;
	private HashMap<String, Object> container = new HashMap<String, Object>();
	private static final PropertiesConfiguration config = ConfigurationWrapper.getConfig();

	/**
	 * Constructor
	 * @param scriptFile the scripting being processed
	 */
	public RuntimeDataLoader(final File scriptFile) {
		this.scriptFile = scriptFile;
	}

	private String getResourcesDir(final File dir) {
		return dir.getPath() + File.separator + "resources";
	}

	/**
	 * Loads script file information
	 */
	private void loadFileInformation() {
		// We always use the full path, so it *should* be fine
		File scriptDir = scriptFile.getParentFile();

		container.put("script.dir", scriptDir.getPath());
		container.put("script.resources.dir", getResourcesDir(scriptDir));

		File packageDir = scriptDir.getParentFile().getParentFile();
		container.put("package.dir", packageDir.getPath());
		container.put("package.resources.dir", getResourcesDir(packageDir));

		File groupDir = packageDir.getParentFile();
		container.put("group.dir", groupDir.getPath());
		container.put("group.resources.dir", getResourcesDir(groupDir));

		File repositoryDir = groupDir.getParentFile().getParentFile();
		container.put("repository.dir", repositoryDir.getPath());
		container.put("repository.resources.dir", getResourcesDir(repositoryDir));
	}

	private void loadConfigurationInformation() {
		Iterator<String> i  = config.getKeys("runtime");

		while (i.hasNext()) {
			String key = i.next();

			container.put(key, config.getString(key));
		}
	}

	/**
	 * Loads the run time information
	 */
	public void load() {
		loadFileInformation();
		loadConfigurationInformation();

		RuntimeDataContainer rdc = RuntimeDataContainer.getInstance();
		rdc.setContainer(container);
	}
}
