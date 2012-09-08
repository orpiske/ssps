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
package org.ssps.sdm.adm.util;

import java.util.HashMap;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.resource.DefaultResourceExchange;
import org.ssps.common.resource.DefaultResourceExchange.Properties;
import org.ssps.common.resource.ResourceExchange;

/**
 * Factory for resource exchanges
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public final class ResourceExchangeFactory {
	
	private static final PropertiesConfiguration config  
		= ConfigurationWrapper.getConfig();
	
	/**
	 * Creates a new ResourceExchange object, sets the proxy, etc.
	 * @return a new ResourceExchange object
	 */
	public static ResourceExchange newResourceExchange() {
		ResourceExchange resourceExchange;
		
		String proxyHost = config.getString("http.proxy.url");
		
		
		if (proxyHost != null) {
			Integer port = config.getInteger("http.proxy.port", 80);
			
			HashMap<String, Object> connectionProperties = 
					new HashMap<String, Object>();
			
			connectionProperties.put(Properties.HTTP_PROXY, proxyHost);
			connectionProperties.put(Properties.PROXY_PORT, port);
			
			resourceExchange = new DefaultResourceExchange(connectionProperties);
		}
		else {
			resourceExchange = new DefaultResourceExchange();	
		}
		
		return resourceExchange;
		
	}

}
