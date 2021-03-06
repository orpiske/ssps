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
package net.orpiske.ssps.common.repository.search;

import java.util.List;

import net.orpiske.ssps.common.repository.PackageInfo;

/**
 * The interface on which to implement package searching
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public interface RepositoryFinder {
	
	PackageInfo find(final String repositoryName, final String groupId, final String packageName, final String version);
	List<PackageInfo> find(final String groupId, final String packageName, final String version);
	List<PackageInfo> find(final String packageName, final String version);
	List<PackageInfo> find(final String packageName);
	List<PackageInfo> allPackages();
	
	
	PackageInfo findFirst(final String groupId, final String packageName, final String version);
	PackageInfo findFirst(final String packageName, final String version);
	PackageInfo findFirst(final String packageName);

	
}
