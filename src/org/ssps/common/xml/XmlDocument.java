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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This is a very simple (naive) implementation of the basis of a XmlDocument
 * 
 * TODO: this clearly needs to be improved in the future. Avoid relying on this
 * code.
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
@Deprecated
public abstract class XmlDocument {
	private static final Logger logger = Logger.getLogger(XmlDocument.class);

	private Document document;

	/**
	 * Default constructor
	 */
	public XmlDocument() {

	}

	/**
	 * Creates a new XML document from the input
	 * 
	 * @param input
	 *            A input stream associated with the XML document
	 * @throws XmlDocumentException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XmlDocument(final InputStream input) throws XmlDocumentException {
		openDocument(input);
	}

	/**
	 * Opens the document
	 * 
	 * @param input
	 *            A input stream associated with the XML document
	 * @throws XmlDocumentException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	protected void openDocument(final InputStream input)
			throws XmlDocumentException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			setDocument(builder.parse(input));
		} catch (ParserConfigurationException e) {
			throw new XmlDocumentException("Unable to open document", e);
		} catch (SAXException e) {
			throw new XmlDocumentException(
					"Unable to open document (Invalid document?)", e);
		} catch (IOException e) {
			throw new XmlDocumentException("Unhandled I/O exception", e);
		}

	}

	/**
	 * Finds a node using an XPath expression
	 * 
	 * @param expression
	 *            The XPath expression
	 * @param node
	 * @return the node
	 */
	protected Element find(final String expression, Node node) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		Element ret = null;

		try {
			ret = (Element) xpath.evaluate(expression, node,
					XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			logger.warn("Unable to evaluate expression " + expression, e);
		}

		return ret;
	}

	/**
	 * Gets the XML Document pointer
	 * 
	 * @return The org.w3c.dom.Document pointing to this document
	 */
	protected Document getDocument() {
		return document;
	}

	/**
	 * Sets the XML Document pointer
	 * 
	 * @param document
	 *            The org.w3c.dom.Document pointing to this document
	 */
	protected void setDocument(Document document) {
		this.document = document;
	}
}
