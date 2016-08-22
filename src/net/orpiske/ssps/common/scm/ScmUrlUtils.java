/**
 Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.ssps.common.scm;

/**
 * SCM URL Utilities
 */
public class ScmUrlUtils {

	/**
	 * Checks whether the input URL is a git SCM URL
	 * @param url the URL
	 * @return true if if it's a Git URL or false otherwise
	 */
	public static boolean isGit(final String url) {
		if (url.startsWith("git://")) {
			return true;
		}
		
		if (url.endsWith(".git")) {
			return true;
		}
		
		return false;
	}


	/**
	 * Checks whether the input URL is a SVN SCM URL
	 * @param url the URL
	 * @return true if if it's a SVN URL or false otherwise
	 */
	public static boolean isSvn(final String url) {
		if (url.startsWith("svn://")) {
			return true;
		}

		if (url.endsWith("/trunk")) {
			return true;
		}

		return false;
	}


	/**
	 * Checks whether the input URL is a known SCM URL
	 * @param url the URL
	 * @return true if if it's a known URL or false otherwise
	 */
	public static boolean isValid(final String url) {
		return isGit(url) || isSvn(url); 
	}
	
}
