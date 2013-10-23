SSPS: Software Package Module
============

This is source code repository for the SPM Package Module (SPM).

SPM is a SSPS utility that processes template files and create SDM packages out of it. It 
can be used to evaluate a file name and create a simple package out of it - provided you have 
a template.

Compiling
--------------------------------

You need [Gradle](http://www.gradle.org). Once setup, run:

```
gradle build
```

The jar file will be located in the build/libs directory within the project directory.


Running
--------------------------------

```
java -jar spm-0.3.0-SNAPSHOT.jar <action>
```


Usage
--------------------------------

SPM works by processing templates and replacing the variables in the file with input parameters
from command-line, a properties file or both

Suppose a template file similar to this:

```groovy
import my.organization.deployment.*

class $packageName extends BinaryPackage {
	def version = "$packageVersion"
	def name = "$packageName"
	def url = "$packageFile"
	def dependencies = [];

	$packageName() {
		super();
		
		someMethod(version, name, url);
	}
}

```groovy

You can give the path of a file and SPM will evaluate its name in order to replace the 3 
default variables (see below) with the information taken from the file name and path. This
is how you could process the aforementioned template:

```
java -jar spm-0.3.0-SNAPSHOT.jar eval --source-file=/path/to/file-0.2.1.tar.bz -t $HOME/tmp/sample/sample.groovy.template
```

This should give you the following output:

```groovy
import my.organization.deployment.*

class file extends BinaryPackage {
	def version = "0.2.1"
	def name = "file"
	def url = "file:/path/to/file-0.2.1.tar.bz"
	def dependencies = [];

	file() {
		super();
		
		someMethod(version, name, url);
	}
}
```groovy


If you have pre-set variables, you may use a template file to generate the package script. 
To do so, you need to have a properties that maps the keys to the values. For example: 

```
packageName=getprlinfo
packageVersion=0.2.1
packageFile=http://myserver.org/\$name/\$version/\$name-\$version.tar.gz
```

Obs.: note that the the variables that are meant to be used by SDM, should be properly escaped 
with \$.

You'd run it as such:

```
java -jar spm-0.3.0-SNAPSHOT.jar templateFile -t $HOME/tmp/sample/sample.groovy.template -p $HOME/tmp/sample/template.properties
```

The output would be equal to the previous one.

You can also define your own variables on the properties file and reference it on the 
template. Any property added to the properties file can be referenced in the template.


Finally, you may also want to pass some of the variables via command-line or you may want to 
set some of the variables via command-line. The variables set via command-line take precedence
over the ones in the properties file, hence you can use them to override the file settings.

This is how you can run it in this scenario:

```
java -DpackageVersion=0.5.6 -jar spm-0.3.0-SNAPSHOT.jar templateFile -t $HOME/tmp/sample/sample.groovy.template -p $HOME/tmp/sample/template.properties
```

Here's what would happen in this scenario:

```groovy
import my.organization.deployment.*

class getprlinfo extends BinaryPackage {
	def version = "0.5.6"
	def name = "getprlinfo"
	def url = "http://myserver.org/$name/$version/$name-$version.tar.gz"
	def dependencies = [];

	getprlinfo() {
		super();
		
		someMethod(version, name, url);
	}
}
```groovy

The 3 default variables
--------------------------------

When running in eval mode, SPM will automagically set the values of these 3 variables:

* packageName (to the file name: ex spm if the input is spm-0.3.0.tar.gz)
* $packageFile (to an URI to the file path: file:/path/to/spm-0.3.0.tar.gz if the input 
is /path/to/spm-0.3.0.tar.gz)
* packageVersion (to the version in the file name: ex 0.3.0 if the input is spm-0.3.0.tar.gz)


Usage
--------------------------------

References
----

* [Main Site](http://orpiske.net/ssps)
