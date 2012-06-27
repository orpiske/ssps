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
package org.ssps.common.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.ssps.common.resource.exceptions.ResourceExchangeException;

/**
 * Implements network resource exchange via HTTP
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class DefaultResourceExchange implements ResourceExchange {
	private static final Logger logger = Logger.getLogger(DefaultResourceExchange.class);
	private HttpClient httpclient = new DefaultHttpClient();
	
	/**
	 * Constructor
	 */
	public DefaultResourceExchange() {
		
	}

	public Resource<InputStream> get(URI uri) throws ResourceExchangeException {
		HttpGet httpget = new HttpGet(uri);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			
			int statusCode = response.getStatusLine().getStatusCode(); 
			
			if (statusCode == HttpStatus.SC_OK) { 
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
					logger.info("Starding to read " + entity.getContentLength() 
							+ " bytes from the server");
					
					Resource<InputStream> ret = new Resource<InputStream>();
					
					ret.setPayload(entity.getContent());
					ret.setSize(entity.getContentLength());
					
					return ret;
				}
			}
			else {
				switch (statusCode) {
				case HttpStatus.SC_NOT_FOUND: 
					throw new ResourceExchangeException("Remote file not found");
				case HttpStatus.SC_BAD_REQUEST: 
					throw new ResourceExchangeException(
							"The client sent a bad request");
				case HttpStatus.SC_FORBIDDEN: 
					throw new ResourceExchangeException(
							"Accessing the resource is forbidden");
				case HttpStatus.SC_UNAUTHORIZED: 
					throw new ResourceExchangeException("Unathorized");
				case HttpStatus.SC_INTERNAL_SERVER_ERROR:
					throw new ResourceExchangeException("Internal server error");
				default:
					throw new ResourceExchangeException(
							"Unable to download file: http status code " + statusCode);
				}
			}
		} catch (ClientProtocolException e) {
			throw new ResourceExchangeException("Unhandled protocol erro: " 
					+ e.getMessage(), e);
		} catch (IOException e) {		
			throw new ResourceExchangeException("I/O error: " + e.getMessage(), 
					e);
		}
		
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ssps.common.resource.ResourceExchange#release()
	 */
	public void release() {
		if (httpclient != null) { 
			httpclient.getConnectionManager().shutdown();	
		}
	}

}
