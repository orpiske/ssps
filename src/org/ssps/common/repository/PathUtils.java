/**
   Copyright 2012 Otavio Rodolfo Piske

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.ssps.common.repository;

/**
 * Repository path utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class PathUtils {
	private String url;

	/**
	 * Constructor
	 * 
	 * @param url
	 *            The repository URL
	 */
	public PathUtils(final String url) {
		this.url = url;
	}

	/**
	 * Gets the root directory for a project group
	 * 
	 * @param group
	 *            the name of the group
	 * @return the URL
	 */
	public static String getGroupRoot(final String url, final String group) {
		StringBuffer buffer = new StringBuffer(url);

		buffer.append("/");
		buffer.append(group);

		return buffer.toString();
	}

	/**
	 * Gets the root directory for a project group
	 * 
	 * @param group
	 *            the name of the group
	 * @return the URL
	 */
	public String getGroupRoot(final String group) {
		StringBuffer buffer = new StringBuffer(url);

		buffer.append("/");
		buffer.append(group);

		return buffer.toString();
	}

	/**
	 * Gets the root directory for a project within a project group
	 * 
	 * @param group
	 *            the name of the group
	 * @param name
	 *            the project name
	 * @return the URL
	 */
	public String getNameRoot(final String group, final String name) {
		StringBuffer buffer = new StringBuffer(url);

		buffer.append("/");
		buffer.append(group);
		buffer.append("/");
		buffer.append(name);

		return buffer.toString();
	}

	/**
	 * Gets the root directory for a project version
	 * 
	 * @param group
	 *            the name of the group
	 * @param name
	 *            the project name
	 * @param version
	 *            the project version
	 * @return the URL
	 */
	public String getPath(final String group, final String name,
			final String version) {
		StringBuffer buffer = new StringBuffer(url);

		buffer.append("/");
		buffer.append(group);
		buffer.append("/");
		buffer.append(name);
		buffer.append("/");
		buffer.append(version);
		buffer.append("/");

		return buffer.toString();
	}

}
