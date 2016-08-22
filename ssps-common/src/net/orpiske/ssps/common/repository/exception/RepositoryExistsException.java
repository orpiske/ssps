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
package net.orpiske.ssps.common.repository.exception;


/**
 * This exception is thrown when an operation on a repository fails because it already 
 * exists. This is expected if you try to add the same repository twice.
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class RepositoryExistsException extends RepositorySetupException {

	/**
	 * @param message
	 * @param t
	 */
	public RepositoryExistsException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * @param message
	 */
	public RepositoryExistsException(String message) {
		super(message);
	}
	

}
