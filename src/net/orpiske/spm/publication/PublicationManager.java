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
package net.orpiske.spm.publication;

import java.io.File;
import java.io.IOException;

import net.orpiske.ssps.common.resource.exceptions.ResourceExchangeException;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public interface PublicationManager {

	void upload(final File file) throws IOException, ResourceExchangeException;
	void upload(final String filename) throws IOException, ResourceExchangeException;
	
	void upload(final String filename, boolean overwrite) throws IOException, ResourceExchangeException;
	void upload(final File file, boolean overwrite) throws IOException, ResourceExchangeException;
	
	void delete(final String address) throws IOException, ResourceExchangeException;
}
