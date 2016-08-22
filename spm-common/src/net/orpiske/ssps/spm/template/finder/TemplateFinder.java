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
package net.orpiske.ssps.spm.template.finder;

import java.io.File;
import java.io.IOException;

/**
 * Template finder
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public interface TemplateFinder {
	
	/**
	 * Finds template by name
	 * @param name the template name
	 * @return A file object pointing to the template file
	 * @throws IOException if the file does not exist or if the file is unreadable
	 */
	File find(final String name) throws IOException;
}
