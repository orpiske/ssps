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
package net.orpiske.ssps.common.xml;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

/**
 * XML parsing utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class XmlParserUtils {
	private static final Logger logger = Logger.getLogger(XmlParserUtils.class);
	
	/**
	 * Restricted constructor
	 */
	private XmlParserUtils() {}

	
	/**
	 * Unmarshal a XML document building a new "JAXB" object out of it 
	 * @param docClass the document class
	 * @param inputStream the input stream pointing to the XML document
	 * @return A "JAXB object" of type T.
	 * @throws JAXBException if unable to parse the document
	 */
	public static <T> T unmarshal(final Class<T> docClass, 
			final InputStream inputStream)
			throws JAXBException 
	{
		logger.trace("Unmarshalling the XML data");
		
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Unmarshaller u = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<T> doc = (JAXBElement<T>) u.unmarshal(inputStream);

		return doc.getValue();
	}
}
