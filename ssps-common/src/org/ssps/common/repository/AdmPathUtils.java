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
 * ADM file path utils
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class AdmPathUtils {

	private String url;

	/**
	 * Constructor
	 * 
	 * @param url
	 *            The repository URL
	 */
	public AdmPathUtils(final String url) {
		this.url = url;
	}
	
	public static String getName(final String name, final String version) {
		return name + "-" + version + ".xml";
	}

	/**
	 * Gets the path of the ADM file in a repository
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
		String path = (new PathUtils(url)).getPath(group, name, version);

		return path + "/" + getName(name, version);
	}
}
