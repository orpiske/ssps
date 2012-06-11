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

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.archive.usa.UsaArchive;
import org.ssps.common.configuration.ConfigurationWrapper;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Unpacker {
    private static final PropertiesConfiguration config = ConfigurationWrapper
	    .getConfig();
    private static final Logger logger = Logger
	    .getLogger(Unpacker.class);
    
    private UsaArchive archive = new UsaArchive();

    public void unpack(final String source) throws SspsArchiveException {
	final String destination = config.getString("temp.work.dir", 
		    FileUtils.getTempDirectoryPath() + File.separator + "work");
	archive.unpack(source, destination);
    }
}
