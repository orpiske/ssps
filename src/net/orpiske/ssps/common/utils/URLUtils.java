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
package net.orpiske.ssps.common.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * URL manipulation utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public final class URLUtils {

	public static final String URL_SEPARATOR = "/";
	private static final String PARAMETERS_START = "?";

	/**
	 * Restricted constructor
	 */
	private URLUtils() {
	};

	
	/**
	 * Given a filename with (mostly) standard parameters (ie.: 
	 * abc.php?c=123&d=456)
	 * it will remove the parameters and return only the file name
	 * @param filename the input file name
	 * @return the file name without parameters
	 * TODO: this method is very naive. We need a better implementation
	 */
	public static String removeParametersFromName(final String filename) {
		if (!filename.contains(PARAMETERS_START)) { 
			return filename;
		}
		
		int firstParameter = filename.indexOf(PARAMETERS_START);
		
		
		return filename.substring(0, firstParameter);
	}

	/**
	 * Gets the file name of a given URI
	 * 
	 * @param uri
	 *            the input URI object
	 * @return the file name part of the URI
	 * @throws MalformedURLException
	 *             if the URI is invalid or null
	 */
	public static String getFilename(final URI uri)
			throws MalformedURLException {
		URL url;

		if (uri == null) {
			throw new MalformedURLException("The input URL 'null' is not valid");
		}

		url = uri.toURL();

		String filePath = url.getFile();
		int lastIndex = filePath.lastIndexOf(URL_SEPARATOR);

		String filenameWithParameters = filePath.substring(lastIndex + 1);
		
		return removeParametersFromName(filenameWithParameters);
	}

	/**
	 * @param uri
	 *            the input URI
	 * @return the file name part of the URI
	 * @throws URISyntaxException
	 *             if the syntax is invalid
	 * @throws MalformedURLException
	 *             if the URI is invalid or null
	 */
	public static String getFilename(String uri) throws MalformedURLException,
			URISyntaxException {
		return getFilename(new URI(uri));
	}

}
