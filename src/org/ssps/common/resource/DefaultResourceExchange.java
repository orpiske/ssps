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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.conn.params.ConnRoutePNames;
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
	
	/**
	 * Exchange properties
	 */
	public class Properties {
		/**
		 * HTTP Proxy URL
		 */
		public static final String HTTP_PROXY = "HTTP_PROXY";
		
		/**
		 * HTTP port
		 */
		public static final String PROXY_PORT = "PROXY_PORT";
	}
	
	
	private HttpClient httpclient = new DefaultHttpClient();
	
	
	
	/**
	 * Constructor
	 */
	public DefaultResourceExchange() {
		
	}
	
	/**
	 * Constructor using connection properties (at the moment, it supports only
	 * unauthenticated proxies).
	 * @param connectionProperties A Hashmap of connection properties to use to 
	 * setup the connection (ex.: proxy)
	 */
	public DefaultResourceExchange(HashMap<String, Object> connectionProperties) {
		String proxy = (String) connectionProperties.get(Properties.HTTP_PROXY);
		Integer port = (Integer) connectionProperties.get(Properties.PROXY_PORT);
		
		if (proxy != null) { 
			HttpHost proxyHost = new HttpHost(proxy,port.intValue());
			
			httpclient.getParams().setParameter(
					ConnRoutePNames.DEFAULT_PROXY, proxyHost);
		}
	}
	
	/**
	 * Gets the last modified value from the header
	 * @param response the HTTP response
	 * @return The content length
	 */
	private long getLastModified(HttpResponse response) {
		Header header = response.getFirstHeader(HttpHeaders.LAST_MODIFIED);
		
		String tmp = header.getValue();
		//Tue, 26 Jun 2012 02:25:57 GMT
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		Date date;
		try {
			date = dateFormat.parse(tmp);
		} catch (ParseException e) {
			logger.warn("The last modified date provided by the server is invalid");
			return 0;
		}
		
		return date.getTime();
	}
	
	/**
	 * Gets the content length value from the header
	 * @param response the HTTP response
	 * @return The content length
	 */
	private long getContentLength(HttpResponse response) {
		Header header = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH);
		
		String tmp = header.getValue();
		try { 
			return Long.parseLong(tmp);
		}
		catch (NumberFormatException e) {
			logger.warn("The server provided an invalid content lenght value");
		}
		
		return 0;
	}
	
	
	/*
	 * Gets information about a resource
	 * @see org.ssps.common.resource.ResourceExchange#get(java.net.URI)
	 */
	public ResourceInfo info(URI uri) throws ResourceExchangeException {
		HttpHead httpHead = new HttpHead(uri);
		HttpResponse response;
		try {
			response = httpclient.execute(httpHead);
			
			int statusCode = response.getStatusLine().getStatusCode(); 
			
			if (statusCode == HttpStatus.SC_OK) { 
				HttpEntity entity = response.getEntity();

				long length = getContentLength(response);
				logger.debug("Reading " + length + " bytes from the server");
				
				ResourceInfo ret = new ResourceInfo();
				
				ret.setSize(length);
				ret.setLastModified(getLastModified(response));
				
				return ret;
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
	}

	/*
	 * Gets a resource
	 * @see org.ssps.common.resource.ResourceExchange#get(java.net.URI)
	 */
	public Resource<InputStream> get(URI uri) throws ResourceExchangeException {
		HttpGet httpget = new HttpGet(uri);
		HttpResponse response;
		try {
			
			response = httpclient.execute(httpget);
			
			int statusCode = response.getStatusLine().getStatusCode(); 
			
			if (statusCode == HttpStatus.SC_OK) { 
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
					logger.debug("Reading " + entity.getContentLength() 
							+ " bytes from the server");
					
					Resource<InputStream> ret = new Resource<InputStream>();
					
					ret.setPayload(entity.getContent());
					
					ResourceInfo info = new ResourceInfo();
					
					info.setSize(entity.getContentLength());
					info.setLastModified(getLastModified(response));
					ret.setResourceInfo(info);
					
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
