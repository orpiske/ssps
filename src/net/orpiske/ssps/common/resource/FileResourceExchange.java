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
package net.orpiske.ssps.common.resource;

import net.orpiske.ssps.common.resource.exceptions.ResourceExchangeException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.io.File;


/**
 * Implements file/local resource exchange
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class FileResourceExchange implements ResourceExchange {
	private FileInputStream inputStream;
	
	@Override
	public Resource<InputStream> get(URI uri) throws ResourceExchangeException {
		File file = new File(uri);
		Resource<InputStream> ret = new Resource<InputStream>();
		
		try {
			inputStream = FileUtils.openInputStream(file);

			ret.setPayload(inputStream);

			ResourceInfo info = new ResourceInfo();

			info.setSize(FileUtils.sizeOf(file));
			info.setLastModified(file.lastModified());
			
			ret.setResourceInfo(info);
		} catch (IOException e) {
			throw new ResourceExchangeException("I/O error: " + e.getMessage(),
					e);
		}
		
		return ret;
		
	}

	@Override
	public ResourceInfo info(URI uri) throws ResourceExchangeException {
		File file = new File(uri);
		
		ResourceInfo ret = new ResourceInfo();

		ret.setSize(FileUtils.sizeOf(file));
		ret.setLastModified(file.lastModified());
		
		return ret;
	}

	@Override
	public void release() {
		IOUtils.closeQuietly(inputStream);
	}
}
