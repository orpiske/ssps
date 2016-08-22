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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;
import net.orpiske.ssps.common.version.Version;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class FileSystemRepositoryFinder implements RepositoryFinder {
	
	List<PackageInfo> packageList;
	
	
	public FileSystemRepositoryFinder() {
		String path = RepositoryUtils.getUserRepository();
		File repository = new File(path);
		
		RepositoryWalker walker = new RepositoryWalker();
		packageList = walker.load(repository);
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.search.RepositoryFinder#find(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public PackageInfo find(String repositoryName, String groupId,
			String packageName, String version) {
		
		for (PackageInfo packageInfo : packageList) {
			if (!packageInfo.getName().equals(packageName)) {
				continue;
			}
			
			if (!packageInfo.getGroupId().equals(groupId)) {
				continue;
			}
			
			if (!packageInfo.getRepository().equals(repositoryName)) {
				continue;
			}
			
			if (!packageInfo.getVersion().equals(Version.toVersion(version))) {
				continue;
			}
			
			
			return packageInfo;
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.search.RepositoryFinder#find(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<PackageInfo> find(String groupId, String packageName,
			String strVersion) {
		ArrayList<PackageInfo> ret = new ArrayList<PackageInfo>();
		
		for (PackageInfo packageInfo : packageList) {
			if (!packageInfo.getName().equals(packageName) && packageName != null) {
				continue;
			}
			
			if (!packageInfo.getGroupId().equals(groupId) && groupId != null) {
				continue;
			}
			
			
			if (strVersion != null) {
				Version version = Version.toVersion(strVersion);
				
				if (!packageInfo.getVersion().equals(version)) {
					continue;
				}
			}
			
			ret.add(packageInfo);
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.search.RepositoryFinder#find(java.lang.String, java.lang.String)
	 */
	@Override
	public List<PackageInfo> find(String packageName, String version) {
		return find(null, packageName, version);
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.search.RepositoryFinder#find(java.lang.String)
	 */
	@Override
	public List<PackageInfo> find(String packageName) {
		return find(null, packageName, null);
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.search.RepositoryFinder#findFirst(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public PackageInfo findFirst(String groupId, String packageName,
			String strVersion) {
		
		for (PackageInfo packageInfo : packageList) {
			if (!packageInfo.getName().equals(packageName) && packageName != null) {
				continue;
			}
			
			if (!packageInfo.getGroupId().equals(groupId) && groupId != null) {
				continue;
			}
			
			
			if (strVersion != null) {
				Version version = Version.toVersion(strVersion);
				
				if (!packageInfo.getVersion().equals(version)) {
					continue;
				}
			}
			
			
			return packageInfo;
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.search.RepositoryFinder#findFirst(java.lang.String, java.lang.String)
	 */
	@Override
	public PackageInfo findFirst(String packageName, String version) {
		return findFirst(null, packageName, version);
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.search.RepositoryFinder#findFirst(java.lang.String)
	 */
	@Override
	public PackageInfo findFirst(String packageName) {
		return findFirst(null, packageName, null);
	}

}
