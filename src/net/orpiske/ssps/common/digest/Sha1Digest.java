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
package net.orpiske.ssps.common.digest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * SHA-1 message digest implementation
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Sha1Digest implements MessageDigest {
	private static Logger logger = Logger.getLogger(Sha1Digest.class);
	
	public static String HASH_NAME = "sha1";
	
	public String calculate(final InputStream inputStream) throws IOException {
		logger.info("Calculating message digest");
		return DigestUtils.shaHex(inputStream);
	}
	
	public String calculate(final File file) throws IOException {
		InputStream fileInputStream = null; 
		
		try { 
			fileInputStream = new FileInputStream(file);
			
			return calculate(fileInputStream);
		}
		finally { 
			IOUtils.closeQuietly(fileInputStream);
		}
	}
	

	/* (non-Javadoc)
	 * @see org.ssps.common.digest.MessageDigest#calculate(java.lang.String)
	 */
	public String calculate(String path) throws IOException {
		File file = new File(path);

		return calculate(file);
	}
	
	/* (non-Javadoc)
	 * @see org.ssps.common.digest.MessageDigest#verify(java.lang.String)
	 */
	public boolean verify(String source) throws IOException {
		InputStream stream = null; 
		
		try { 
			stream = new FileInputStream(source + "." + HASH_NAME);
			
			String digest = IOUtils.toString(stream);
			
			digest = digest.trim();
			return verify(source, digest);
		}
		finally { 
			IOUtils.closeQuietly(stream);
		}
	}
	

	/* (non-Javadoc)
	 * @see org.ssps.common.digest.MessageDigest#verify(java.lang.String, java.lang.String)
	 */
	public boolean verify(String source, String digest) throws IOException {
		logger.info("Verifying message digest");
		String sourceDigest = null;
	
		sourceDigest = calculate(source);
		
		if (sourceDigest.equals(digest)) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.ssps.common.digest.MessageDigest#save(java.lang.String)
	 */
	public void save(String source) throws IOException {
		String digest = null;
		FileOutputStream output = null;
		
		try { 
			digest = calculate(source);
			
			File file = new File(source + "." + HASH_NAME);
			
			if (file.exists()) {
				file.delete();
			}
			else {
				file.createNewFile();
			}
			
			output = new FileOutputStream(file);
			output.write(digest.getBytes());
			output.flush();
		}
		finally { 
			IOUtils.closeQuietly(output);
		}
	}
}
