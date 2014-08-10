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
package net.orpiske.ssps.common.repository.utils;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import net.orpiske.ssps.common.groovy.GroovyClasspathHelper;
import net.orpiske.ssps.common.packages.annotations.Helper;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.exception.PackageInfoException;

import net.orpiske.ssps.common.version.Version;
import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.metaclass.MissingPropertyExceptionNoStack;

/**
 * The Groovy script engine
 *
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class PackageDataUtils {

	private static Logger logger = Logger.getLogger(PackageDataUtils.class);

	private PackageDataUtils() {

	}

    private static boolean isHelper(Class<?> groovyClass) {
        Helper helper = groovyClass.getAnnotation(Helper.class);

        if (helper != null) {
            return true;
        }

        return false;
    }

    private static Class<?> getGroovyClass(final File file) throws PackageInfoException {
        GroovyClasspathHelper classpathHelper = GroovyClasspathHelper.getInstance();
        GroovyClassLoader loader = classpathHelper.getLoader();

        Class<?> groovyClass;
        try {
            if (!file.exists()) {
                throw new IOException("File not found: " + file.getPath());
            }

            if (!file.canRead()) {
                throw new IOException("Permission denied: " + file.getPath());
            }

            loader.addClasspath(file.getParent() + File.separator + "resources");

            groovyClass = loader.parseClass(file);
        } catch (CompilationFailedException e) {
            throw new PackageInfoException("The script has errors: " + e.getMessage(),
                    e);
        } catch (IOException e) {
            throw new PackageInfoException("Input/output error: " + e.getMessage(),
                    e);
        }

        return groovyClass;
    }

    private static GroovyObject getObject(Class<?> groovyClass, final PackageInfo packageInfo)
            throws PackageInfoException
    {
		GroovyObject groovyObject;

		try {
			groovyObject = (GroovyObject) groovyClass.newInstance();
		} catch (InstantiationException e) {
			throw new PackageInfoException("Unable to instantiate object: "
					+ e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new PackageInfoException("Illegal access: " + e.getMessage(),
					e);
		}

		return groovyObject;
	}

    private static void readPropertiesFromClass(final Class<?> groovyClass, final PackageInfo packageInfo) {
        packageInfo.setGroupId(groovyClass.getPackage().getName());
        packageInfo.setName(groovyClass.getClass().getSimpleName());
    }


    private static void readPropertiesFromObject(final GroovyObject groovyObject, final PackageInfo packageInfo) {
        try {
            String url = groovyObject.getProperty("url").toString();
            packageInfo.setUrl(url);
        }
        catch (MissingPropertyExceptionNoStack e) {
            logger.info("Property URL undefined for " + packageInfo.getName());
        }

        try {
            String version  = groovyObject.getProperty("version").toString();

            if (version != null) {
                packageInfo.setVersion(Version.toVersion(version));
            }
        }
        catch (MissingPropertyExceptionNoStack e) {
            logger.info("Property version undefined for " + packageInfo.getName());
        }


        try {
            Object o = groovyObject.getProperty("dependencies");

            if (o instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, String> dependencies =
                        (LinkedHashMap<String, String>) o;

                packageInfo.setDependencies(dependencies);
            }
        }
        catch (MissingPropertyExceptionNoStack e) {
            packageInfo.setDependencies(new LinkedHashMap<String, String>());
            logger.debug("Property " + e.getProperty() + " undefined for " + packageInfo.getName());
        }
    }


	public static PackageInfo read(final File file, final PackageInfo packageInfo) throws PackageInfoException {
        Class<?> groovyClass = getGroovyClass(file);

		if (isHelper(groovyClass)) {
			return null;
		}

        readPropertiesFromClass(groovyClass, packageInfo);

		GroovyObject groovyObject = getObject(groovyClass, packageInfo);

        readPropertiesFromObject(groovyObject, packageInfo);

        return packageInfo;
	}
}
