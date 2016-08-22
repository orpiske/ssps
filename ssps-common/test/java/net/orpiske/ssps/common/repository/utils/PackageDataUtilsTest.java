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
package net.orpiske.ssps.common.repository.utils;

import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.exception.PackageInfoException;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;

public class PackageDataUtilsTest {

    @Test(expected = PackageInfoException.class)
    public void testFileNotFound() throws PackageInfoException {
        String file = getClass().getResource("/").getFile();

        String voidPackagePath = file + File.separator + "net"
                + File.separator + "orpiske"
                + File.separator + "test"
                + File.separator + "VoidPackage.groovy";


        File voidPackageFile = new File(voidPackagePath);


        PackageInfo packageInfo = new PackageInfo();
        PackageDataUtils.read(voidPackageFile, packageInfo);
    }


    @Test
    public void testReadPackageInfo() throws PackageInfoException {
        String file = getClass().getResource("/unit-test-repository/packages").getFile();

        String voidPackagePath = file + File.separator + "net"
                + File.separator + "orpiske"
                + File.separator + "test"
                + File.separator + "VoidPackage.groovy";


        File voidPackageFile = new File(voidPackagePath);


        PackageInfo packageInfo = new PackageInfo();
        PackageDataUtils.read(voidPackageFile, packageInfo);

        assertEquals("The URL does not match", "https://www.doesnotexist.br", packageInfo.getUrl());
    }

}
