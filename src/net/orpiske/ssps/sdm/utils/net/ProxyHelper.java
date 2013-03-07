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
package net.orpiske.ssps.sdm.utils.net;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;

import org.apache.commons.configuration.PropertiesConfiguration;

public final class ProxyHelper {
	
	
	private static PropertiesConfiguration config = ConfigurationWrapper.getConfig();
	
	/**
	 * Restricted constructor
	 */
	private ProxyHelper() {
		
	}

	
	private static void setupHttpProxy() {
		String host = config.getString("http.proxy.server");
		String port = config.getString("http.proxy.port");
		String hostList = config.getString("http.proxy.ignore");
		
		
		if (host != null && port != null) { 
			Proxy proxy = new HttpProxy();
			
			proxy.setProxy(host, port);
			proxy.setIgnored(hostList);
		}
	}
	
	
	private static void setupHttpsProxy() {
		boolean dedicated = config.getBoolean("https.server.dedicated",
				false);
		String host; 
		String port;
		String hostList;
		
		if (dedicated) {
			host = config.getString("https.proxy.server");
			port = config.getString("https.proxy.port");
			hostList = config.getString("https.proxy.ignore");
		}
		else { 
			host = config.getString("http.proxy.server");
			port = config.getString("http.proxy.port");
			hostList = config.getString("http.proxy.ignore");
		}
		
		if (host != null && port != null) { 
			Proxy proxy = new HttpsProxy();
			
			proxy.setProxy(host, port);
			proxy.setIgnored(hostList);
		}
	}
	
	

	private static void setupFtpProxy() {
		boolean dedicated = config.getBoolean("ftp.server.dedicated",
				false);
		String host; 
		String port;
		String hostList;
		
		if (dedicated) {
			host = config.getString("ftp.proxy.server");
			port = config.getString("ftp.proxy.port");
			hostList = config.getString("ftp.proxy.ignore");
		}
		else { 
			host = config.getString("http.proxy.server");
			port = config.getString("http.proxy.port");
			hostList = config.getString("http.proxy.ignore");
		}
		
		if (host != null && port != null) { 
			Proxy proxy = new HttpsProxy();
			
			proxy.setProxy(host, port);
			proxy.setIgnored(hostList);
		}
	}
	
	
	private static void setupSocksProxy() {
		String host = config.getString("socks.proxy.server");
		String port = config.getString("socks.proxy.port");
		
		
		if (host != null && port != null) { 
			Proxy proxy = new FtpProxy();
			
			proxy.setProxy(host, port);
		}
	}
	
	
	public static void setup() {
		setupHttpProxy();
		setupHttpsProxy();
		setupFtpProxy();
		setupSocksProxy();
	}
}
