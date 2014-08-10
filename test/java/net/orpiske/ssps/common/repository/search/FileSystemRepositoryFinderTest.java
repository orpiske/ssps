/**
 Copyright 2014 Otavio Rodolfo Piske

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

import net.orpiske.ssps.common.repository.PackageInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class FileSystemRepositoryFinderTest {
    private File repositoryFile;

    @Before
    public void init() {
        String repositoryDir = getClass().getResource("/unit-test-repository").getFile();



        repositoryFile = new File(repositoryDir);
    }

    @Test
    public void testSearch() {
        RepositoryFinder finder = new FileSystemRepositoryFinder(repositoryFile);

        List<PackageInfo> packages = finder.allPackages();

        assertNotNull("The returned package list is null", packages);
        assertTrue("The returned package list is empty", packages.size() > 0);

        PackageInfo packageInfo = packages.get(0);



        assertEquals("Invalid repository name", "unit-test-repository",
                packageInfo.getRepository());

        assertEquals("Invalid group id", "net.orpiske.test",
                packageInfo.getGroupId());
    }
}
