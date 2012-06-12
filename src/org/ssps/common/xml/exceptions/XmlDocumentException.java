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
package org.ssps.common.xml.exceptions;

import org.ssps.common.exceptions.SspsException;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class XmlDocumentException extends SspsException {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public XmlDocumentException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param t
	 */
	public XmlDocumentException(String message, Throwable t) {
		super(message, t);
	}

}
