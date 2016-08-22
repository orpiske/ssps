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

import java.util.HashMap;


/**
 * This class contains the relevant run time information that can be accessed by the scripts
 */
public class RuntimeDataContainer {
	private HashMap<String, Object> container;

	private static RuntimeDataContainer instance;

	/**
	 * Constructor
	 */
	private RuntimeDataContainer() {
	}


	/**
	 * Gets a (copy of) the data container
	 * @return a copy of the data container
	 */
	public HashMap<String, Object> getContainer() {
		return new HashMap<String, Object>(container);
	}


	/**
	 * Sets the data container
	 * @param container the data container
	 */
	void setContainer(HashMap<String, Object> container) {
		this.container = container;
	}

	/**
	 * Gets a property by name/key
	 * @param name The name/key of the property
	 * @return the value or null if not found
	 */
	public Object getProperty(final String name) {
		return container.get(name);
	}


	/**
	 * Returns the string representation of the container
	 * @return a string representation of the container
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (String key : container.keySet()) {
			builder.append(key);
			builder.append('=');
			builder.append(container.get(key));
			builder.append('\n');
		}

		return builder.toString();
	}


	/**
	 * Gets the runtime data container instance
	 * @return the instance
	 */
	public static RuntimeDataContainer getInstance() {
		if (instance == null) {
			instance = new RuntimeDataContainer();
		}

		return instance;
	}
}
