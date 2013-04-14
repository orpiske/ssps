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
package net.orpiske.ssps.common.archive;

import net.orpiske.ssps.common.archive.exceptions.SspsArchiveException;

/**
 * SSPS Archive interface
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public interface Archive {


	/**
	 * Unpack 'file' to 'directory'
	 * 
	 * @param source
	 *            The source file
	 * @param destination
	 *            The destination directory
	 * @return The number of bytes unpacked
	 * @throws SspsArchiveException if any the unpack operation is unable to continue. Underlying exception will be
	 * provided as the cause if applicable
	 */
	long unpack(final String source, final String destination)
			throws SspsArchiveException;

}
