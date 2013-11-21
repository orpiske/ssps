/**
 Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.sdm.plugins.builders

import net.orpiske.sdm.lib.Executable
import net.orpiske.ssps.common.configuration.ConfigurationWrapper
import org.apache.commons.configuration.PropertiesConfiguration

/**
 * Maven project builder
 */
class MavenBuilder implements ProjectBuilder {
    private String executable;
    private String projectDirectory
    private String profile;



    public MavenBuilder(String projectDirectory) {
        this.projectDirectory = projectDirectory;

        PropertiesConfiguration config = ConfigurationWrapper.getConfig();
        executable = config.getString("maven.executable");

		if (executable == null || executable.isEmpty()) {
			executable = "mvn"
		}
    }


    public MavenBuilder(File projectDirectory) {
        this.projectDirectory = projectDirectory.getPath();

        PropertiesConfiguration config = ConfigurationWrapper.getConfig();
        executable = config.getString("maven.executable");

		if (executable == null || executable.isEmpty()) {
			executable = "mvn"
		}
    }


    String getProfile() {
        return profile
    }

    void setProfile(String profile) {
        this.profile = profile
    }


    private int execPhase(String phase) {
        String arguments = "-f ${projectDirectory}/pom.xml"

        if (profile != null && !profile.isEmpty()) {
            arguments = arguments + " -P ${profile}"
        }

        arguments = arguments + " ${phase}"

        //System.setProperty("JAVA_HOME", )
        int ret = Executable.exec(executable, arguments)

        if (ret != 0) {
            println "The command line did not execute successfully"
        }

        return ret
    }

    @Override
    int clean() {
        return execPhase("clean");
    }

    @Override
    int compile() {
        return execPhase("compile");
    }

    @Override
    int createPackage() {
        return execPhase("package");
    }

    @Override
    int install() {
        return execPhase("install");
    }
}
