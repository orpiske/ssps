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
package org.ssps.common.resource;

/**
 * Holds resource information
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class ResourceInfo {
	private long size;
	private long lastModified;
	
	/**
	 * Resource size
	 * @return
	 */
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public long getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
