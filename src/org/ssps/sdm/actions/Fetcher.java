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
package org.ssps.sdm.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.repository.PathUtils;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Fetcher {
    private static final PropertiesConfiguration config = ConfigurationWrapper
	    .getConfig();
    
    private Sardine sardine;
    private String url;
    
    
    
    public Fetcher(String url, String username, String password) 
    {
	
	if (url == null) {
	    url = config.getString("default.repository.url");
	}
	
	if (username == null) {
	    username = config.getString("default.repository.username");
	    password = config.getString("default.repository.password");
	}
	
	this.url = url;
	sardine = SardineFactory.begin(username, password);
    }
    
    public void fetch(final String group, final String name,
	    final String version, String destination) throws IOException {
	String path = (new PathUtils(url)).getPath(group, name, version);

	List<DavResource> resources = sardine.list(path);
	
	if (destination == null) {
	    destination = config.getString("temp.work.dir", 
		    FileUtils.getTempDirectoryPath());
	}

	for (DavResource resource : resources) {

	    if (!resource.isDirectory()) {
		File newFile = new File(destination
			+ File.separator + resource.getName());
		
		if (newFile.exists()) {
		    continue;
		}
		else {
		    newFile.createNewFile();
		}
		
		InputStream stream = new FileInputStream(newFile);
		stream = sardine.get(resource.getHref().toString());

		stream.close();
	    }
	}
    }

}
