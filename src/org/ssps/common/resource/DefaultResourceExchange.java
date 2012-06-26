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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;
import org.ssps.common.resource.exceptions.ResourceExchangeException;

/**
 * Implements network resource exchange via HTTP
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class DefaultResourceExchange implements ResourceExchange {
	
	
	/**
	 * Constructor
	 */
	public DefaultResourceExchange() {
		
	}

	public InputStream get(URI uri) throws ResourceExchangeException {
		FileSystemManager fsManager;
		try {
			fsManager = VFS.getManager();
			FileObject file = fsManager.resolveFile(uri.toString());
			
			FileType type = file.getType();
			if (type == FileType.FILE) { 
				
				FileContent content = file.getContent();
			
				if (content != null) {
					return content.getInputStream();
				}
			}
			else {
				throw new ResourceExchangeException("Unsupported file type");
			}
			return null;
		} catch (FileSystemException e) {
			throw new ResourceExchangeException("Unable to obtain file resource from "
					+ uri.toString(), e);
		}
	}

}
