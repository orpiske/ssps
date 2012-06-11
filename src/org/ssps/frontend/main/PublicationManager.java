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
package org.ssps.frontend.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.frontend.archive.dbm.DbmDocument;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class PublicationManager {

    private static final Logger logger = Logger
	    .getLogger(PublicationManager.class);
    private static final PropertiesConfiguration config = ConfigurationWrapper
	    .getConfig();

    private DbmDocument dbmDocument;
    private Sardine sardine;

    private String url;

    public PublicationManager() {
	String repositoryUser = config.getString("default.repository.username");
	String repositoryPassword = config
		.getString("default.repository.password");

	url = config.getString("default.repository.url");
	sardine = SardineFactory.begin(repositoryUser, repositoryPassword);
    }

    public PublicationManager(final String dbmFile) throws XmlDocumentException {
	dbmDocument = new DbmDocument(dbmFile);

	String repositoryUser = dbmDocument.getRepositoryUser();
	String repositoryPassword = dbmDocument.getRepositoryPassword();

	if (repositoryUser == null) {
	    repositoryUser = config.getString("default.repository.username");
	    repositoryPassword = config
		    .getString("default.repository.password");
	}

	if (repositoryPassword == null) {
	    repositoryPassword = "";
	}

	url = dbmDocument.getRepositoryUrl();

	if (url == null) {
	    url = config.getString("default.repository.url");
	}
	sardine = SardineFactory.begin(repositoryUser, repositoryPassword);
    }

    private String getGroupRoot(final String group) {
	StringBuffer buffer = new StringBuffer(url);

	buffer.append("/");
	buffer.append(group);

	return buffer.toString();
    }

    private String getGroupRoot() {
	return getGroupRoot(dbmDocument.getProjectGroup());
    }

    private String getNameRoot(final String group, final String name) {
	StringBuffer buffer = new StringBuffer(url);

	buffer.append("/");
	buffer.append(group);
	buffer.append("/");
	buffer.append(name);

	return buffer.toString();
    }

    private String getNameRoot() {
	return getNameRoot(dbmDocument.getProjectGroup(),
		dbmDocument.getProjectName());
    }

    private String getPath(final String group, final String name,
	    final String version) {
	StringBuffer buffer = new StringBuffer(url);

	buffer.append("/");
	buffer.append(group);
	buffer.append("/");
	buffer.append(name);
	buffer.append("/");
	buffer.append(version);
	buffer.append("/");

	return buffer.toString();
    }

    private String getPath() {
	return getPath(dbmDocument.getProjectGroup(),
		dbmDocument.getProjectName(), dbmDocument.getProjectVersion());
    }

    public void upload(final String filename) throws IOException {
	InputStream stream = new FileInputStream(filename);

	// byte[] data = FileUtils.readFileToByteArray(new File(filename));

	logger.info("Checking if project group directory exists");
	if (!sardine.exists(getGroupRoot())) {
	    logger.info("Project group directory does not exists. Creating it ...");
	    sardine.createDirectory(getGroupRoot());
	}

	logger.info("Checking if project directory exists");
	if (!sardine.exists(getNameRoot())) {
	    logger.info("Project directory does not exists. Creating it ...");
	    sardine.createDirectory(getNameRoot());
	}

	logger.info("Checking if deliverable directory exists");
	if (!sardine.exists(getPath())) {
	    logger.info("Deliverable directory does not exists. Creating it ...");
	    sardine.createDirectory(getPath());
	}

	sardine.put(getPath() + FilenameUtils.getName(filename), stream);
    }

    public void download(final String group, final String name,
	    final String version, final String destination) throws IOException {
	String path = getPath(group, name, version);

	List<DavResource> resources = sardine.list(path);

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
