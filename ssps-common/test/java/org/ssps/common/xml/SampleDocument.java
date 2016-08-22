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
package org.ssps.common.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SampleDocument extends XmlDocument {
    

    /**
     * @param input
     * @throws ParserConfigurationException
     * @throws XmlDocumentException 
     */
    public SampleDocument()
	    throws ParserConfigurationException, SAXException, IOException, XmlDocumentException {
	
	InputStream stream = getClass().getResourceAsStream("sample.xml");
	
	super.openDocument(stream);
    }
    
    
    
    public String getFirst() {
	Element root = (Element) getDocument().getFirstChild();
	Element element = super.find("//first", root);
	
	if (element != null) {
	    return element.getTextContent();
	}
	
	return null;
    }
    
    
    public String getSecond() {
	Element root = (Element) getDocument().getFirstChild();
	Element element = super.find("//second", root);
	
	if (element != null) {
	    return element.getTextContent();
	}
	
	return null;
    }
    
    public String getFourth() {
	Element root = (Element) getDocument().getFirstChild();
	Element element = super.find("//third/fourth", root);
	
	if (element != null) {
	    return element.getTextContent();
	}
	
	return null;
    }

}
