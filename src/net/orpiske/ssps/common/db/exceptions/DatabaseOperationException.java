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
package net.orpiske.ssps.common.db.exceptions;

import net.orpiske.ssps.common.exceptions.SspsException;

/**
 * This class abstract the exceptions during a database operation (INSERT, UPDATE, 
 * SELECT, etc)
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class DatabaseOperationException extends SspsException {

	/**
	 * Default constructor
	 * @param message exception message
	 * @param t root cause
	 */
	public DatabaseOperationException(final String message, final Throwable t) {
		super(message, t);
	}

	/**
	 * Constructor
	 * @param message exception message
	 */
	public DatabaseOperationException(final String message) {
		super(message);
	}

}
