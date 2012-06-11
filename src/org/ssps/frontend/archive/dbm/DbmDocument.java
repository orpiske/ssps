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
package org.ssps.frontend.archive.dbm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.ssps.common.xml.XmlDocument;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.w3c.dom.Element;

/**
 * Abstracts the Deliverable Build Model (DBM) document
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class DbmDocument extends XmlDocument {
    public static final String DEFAULT_SOURCE_DIRECTORY = "${basedir}/src/dbm";
    public static final String DEFAULT_OUTPUT_DIRECTORY = "${basedir}/target/installroot";
    public static final String DEF_DELIVERABLE_OUTPUT_DIRECTORY = "${basedir}/target";

    private String path;

    public DbmDocument(final String path) throws XmlDocumentException {
	this.setPath(path);

	try {
	    InputStream stream = new FileInputStream(path);

	    super.openDocument(stream);
	} catch (FileNotFoundException e) {
	    throw new XmlDocumentException(e.getMessage(), e);
	}
    }

    private String getElementByExpression(final String expression) {
	Element root = (Element) getDocument().getFirstChild();
	Element element = super.find(expression, root);

	if (element != null) {
	    return element.getTextContent();
	}

	return null;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public String getProjectGroup() {
	return getElementByExpression("//project/group");
    }
    
    public String getProjectName() {
	return getElementByExpression("//project/name");
    }

    public String getProjectVersion() {
	return getElementByExpression("//project/version");
    }

    public String getBuildSourceDirectory() {
	String ret = getElementByExpression("//build/sourceDirectory");

	if (ret == null) {
	    ret = DEFAULT_SOURCE_DIRECTORY;
	}

	return VariableSupport.parse(ret, this);
    }

    public String getBuildOutputDirectory() {
	String ret = getElementByExpression("//build/outputDirectory");

	if (ret == null) {
	    ret = DEFAULT_OUTPUT_DIRECTORY;
	}

	return VariableSupport.parse(ret, this);
    }

    public String getBuildArtifact() {
	String ret = getElementByExpression("//build/artifact");

	if (ret != null) {
	    ret = VariableSupport.parse(ret, this);
	}

	return ret;
    }

    public String getDeliverableName() {
	return getProjectName() + "-" + getProjectVersion();
    }

    public String getDeliverableOutputDirectory() {
	return VariableSupport.parse(DEF_DELIVERABLE_OUTPUT_DIRECTORY, this);
    }
    
    
    public String getRepositoryUser() {
	return getElementByExpression("//repository/username");
    }
    
    public String getRepositoryPassword() {
	return getElementByExpression("//repository/password");
    }

    public String getRepositoryUrl() {
	return getElementByExpression("//repository/url");
    }
}