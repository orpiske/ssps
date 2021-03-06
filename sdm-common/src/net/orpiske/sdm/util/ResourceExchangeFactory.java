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
package net.orpiske.sdm.util;

import net.orpiske.ssps.common.resource.FileResourceExchange;
import net.orpiske.ssps.common.resource.HttpResourceExchange;
import net.orpiske.ssps.common.resource.ResourceExchange;

import java.net.URI;

/**
 * Factory for resource exchanges
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public final class ResourceExchangeFactory {

	
	/**
	 * Creates a new ResourceExchange object, sets the proxy, etc.
	 * @return a new ResourceExchange object
	 */
	public static ResourceExchange newResourceExchange(URI uri) {
		ResourceExchange resourceExchange;
		
		if (uri.getScheme().equalsIgnoreCase("file")) {
			resourceExchange = new FileResourceExchange();
		}
		else {
			resourceExchange = new HttpResourceExchange();
		}
		
		return resourceExchange;
		
	}

}
