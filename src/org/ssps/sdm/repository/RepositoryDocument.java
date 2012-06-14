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
package org.ssps.sdm.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import net.orpiske.ssps.repository.ObjectFactory;
import net.orpiske.ssps.repository.Repository;

import org.ssps.common.xml.XmlParserUtils;
import org.ssps.common.xml.XmlWriterUtils;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.sdm.repository.exceptions.InvalidRepository;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class RepositoryDocument {
	
	private static final String DOCUMENT_NAME = ".repository.xml";
	
	private String path;
	private Repository document;
	
	public RepositoryDocument(Repository document) throws XmlDocumentException {
		setDocument(document);
		
		write();
	}
	
	public RepositoryDocument(final String path) throws XmlDocumentException, InvalidRepository {
		this.setPath(path);

		try {
			InputStream stream = new FileInputStream(path);

			setDocument(XmlParserUtils.unmarshal(Repository.class, stream));
			
		} catch (FileNotFoundException e) {
			throw new InvalidRepository(e.getMessage(), e);
		} catch (JAXBException e) {
			throw new XmlDocumentException("Unable to umarhsall document", e);
		}
	}
	
	public RepositoryDocument() throws XmlDocumentException, InvalidRepository {
		this(DOCUMENT_NAME);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Repository getDocument() {
		return document;
	}

	public void setDocument(Repository document) {
		this.document = document;
	} 
	
	
	public void write() throws XmlDocumentException {
		File repoFile = new File(DOCUMENT_NAME);
		
		if (repoFile.exists()) {
			throw new XmlDocumentException("This repository is already setup");
		}
		
		try {
			repoFile.createNewFile();
		}
		catch (IOException e) {
			throw new XmlDocumentException(
					"Unable to create a new repository document file: " 
					+ e.getMessage(), e);
		}

		try {
			ObjectFactory of = new ObjectFactory();
			
			JAXBElement<Repository> element = of.createRepository(document);
			
			XmlWriterUtils.marshal(element, document, repoFile);
		} catch (FileNotFoundException e) {
			throw new XmlDocumentException(
					"Unable to write the new repository document: "
						+ e.getMessage(), e);
		} catch (JAXBException e) {
			throw new XmlDocumentException(
					"Unable to create the new repository document: "
							+ e.getMessage(), e);
		} catch (IOException e) {
			throw new XmlDocumentException(
					"Unable to create the new repository document: " 
							+ e.getMessage(), e);
		}
	}

}
