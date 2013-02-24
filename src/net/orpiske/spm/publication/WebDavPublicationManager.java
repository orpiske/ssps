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
package net.orpiske.spm.publication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.orpiske.spm.common.adm.AdmDocument;
import net.orpiske.ssps.adm.Adm;
import net.orpiske.ssps.adm.Artifact;
import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import net.orpiske.ssps.common.repository.PathUtils;
import net.orpiske.ssps.common.repository.RepositoryInfo;
import net.orpiske.ssps.common.resource.exceptions.ResourceExchangeException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class WebDavPublicationManager implements PublicationManager {

	private static final Logger logger = Logger
			.getLogger(WebDavPublicationManager.class);
	private static final PropertiesConfiguration config = ConfigurationWrapper
			.getConfig();

	
	private Sardine sardine;

	private String url;
	
	private Adm adm;

	public WebDavPublicationManager(final Adm adm) {
		this.adm = adm;
		
		String repositoryUser = config.getString("default.repository.username");
		String repositoryPassword = config
				.getString("default.repository.password");

		url = config.getString("default.repository.url");
		sardine = SardineFactory.begin(repositoryUser, repositoryPassword);
	}
	
	public WebDavPublicationManager(final RepositoryInfo repositoryInfo, final Adm adm) {
		this.adm = adm;
		
		url = repositoryInfo.getUrl().toString();
		
		sardine = SardineFactory.begin(repositoryInfo.getUserName(), 
					repositoryInfo.getPassword());
	}
	
	private String getGroupRoot() {
		Artifact artifact = adm.getArtifact();
		
		String group = artifact.getGroupId();
		if (group == null) {
			group = "";
		}
		
		
		return (new PathUtils(url)).getGroupRoot(group);
	}

	private String getNameRoot() {
		Artifact artifact = adm.getArtifact();
		
		String group = artifact.getGroupId();
		if (group == null) {
			group = "";
		}
		
		return (new PathUtils(url)).getNameRoot(group, artifact.getName());
	}

	private String getPath() {
		Artifact artifact = adm.getArtifact();
		
		String group = artifact.getGroupId();
		if (group == null) {
			group = "";
		}
		
		return (new PathUtils(url)).getPath(group, artifact.getName(), 
				artifact.getVersion());
	}
	
	
	/* (non-Javadoc)
	 * @see net.orpiske.spm.publication.PublicationManager#upload(java.lang.String)
	 */
	public void upload(final String filename) throws IOException, ResourceExchangeException {	
		upload(filename, false);
	}
	
	/* (non-Javadoc)
	 * @see net.orpiske.spm.publication.PublicationManager#upload(java.io.File)
	 */
	public void upload(File file) throws IOException,
			ResourceExchangeException {
		upload(file, false);
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.spm.publication.PublicationManager#upload(java.lang.String, boolean)
	 */
	public void upload(String filename, boolean overwrite) throws IOException, ResourceExchangeException {
		InputStream stream = null; 
		
		try {
			File file = new File(filename);
			
			if (!file.exists()) {
				throw new IOException("File does not exist: " + filename );
			}
			
			if (!file.canRead()) {
				throw new IOException("Permission denied: " + filename );
			}
			
			
			stream = new FileInputStream(filename);
			
			String destination = getPath() + FilenameUtils.getName(filename);
			
			upload(stream, destination, overwrite);
		}
		finally {
			IOUtils.closeQuietly(stream);
		}
	}
	

	/* (non-Javadoc)
	 * @see net.orpiske.spm.publication.PublicationManager#upload(java.io.File, boolean)
	 */
	public void upload(File file, boolean overwrite) throws IOException,
			ResourceExchangeException {
		InputStream stream = null; 
		
		try {			
			if (!file.exists()) {
				throw new IOException("File does not exist: " 
						+ file.getCanonicalFile());
			}
			
			if (!file.canRead()) {
				throw new IOException("Permission denied: " 
						+ file.getCanonicalFile());
			}
			
			stream = new FileInputStream(file);
			
			String destination = getPath() + FilenameUtils.getName(file.getName());
			
			upload(stream, destination, overwrite);
		}
		finally {
			IOUtils.closeQuietly(stream);
		}
	}
	
	
	private void createDirectories() throws IOException {
		String groupRoot = getGroupRoot();
		logger.info("Checking if project group '" + groupRoot 
				+ "' directory exists");
		if (!sardine.exists(getGroupRoot())) {
			logger.info("Project group directory does not exists. Creating it ...");
			sardine.createDirectory(getGroupRoot());
		}

		String projectDirectory = getNameRoot();
		logger.info("Checking if project directory '" + projectDirectory 
				+ "' directory exists");
		if (!sardine.exists(projectDirectory)) {
			logger.info("Project directory does not exists. Creating it ...");
			sardine.createDirectory(getNameRoot());
		}

		String deliverablePath = getPath();
		logger.info("Checking if deliverable directory '" + deliverablePath 
				+ "' directory exists");
		if (!sardine.exists(deliverablePath)) {
			logger.info("Deliverable directory does not exists. Creating it ...");
			sardine.createDirectory(getPath());
		}
	}
	
	/* (non-Javadoc)
	 * @see net.orpiske.spm.publication.PublicationManager#upload(java.lang.String, boolean)
	 */
	private void upload(final InputStream stream, final String destination, boolean overwrite) throws IOException, ResourceExchangeException {
		createDirectories();
		
		if (sardine.exists(destination)) {
			if (!overwrite) {
				throw new ResourceExchangeException(
						"The resource already exists at " + destination);
			}
			else {
				delete(destination);
			}
		}
		
		sardine.put(destination, stream);
		logger.info("Deliverable uploaded successfully to " 
				+ destination);	
	}

	/* (non-Javadoc)
	 * @see net.orpiske.spm.publication.PublicationManager#delete(java.lang.String)
	 */
	public void delete(final String address) throws IOException {
		sardine.delete(address);
		logger.info("Removing folder " + address);
		
	}
}
