
                          SSPS:SDM

  What is it?
  -----------

  SDM is a simple, multi-platform, package manager. It helps developers and configuration management personnel to
  automate the jobs related to install software packages. It allows you to perform the same procedures no matter what
  OS you are running.
  SDM is focused on custom software, in-house or enterprise applications that may not be suitable for distribution using
  RPM, APT, nix, portage and many other package managers.
  SDM aims to be simple: as simple as possible to deploy custom software, without requiring elevated privileges (root,
  Administrator, etc).


  Documentation
  -------------

  The most up-to-date documentation can be found at http://orpiske.net/ssps/.

  Release Notes
  -------------

  The full list of changes can be found at http://www.orpiske.net/ssps/release-notes.html.

  System Requirements
  -------------------

  JDK:
    1.6 or above.

  Memory:
    The minimum recommend memory for running SDM is 32Mb.

  Disk:
    No minimum requirement.

  Operating System:
    No minimum requirement. SDM should run on any modern operating system capable or running Java, including but not
    limited to: Linux, OS X, Unices in general (Solaris, HP-UX) and Windows.

  Installing SDM
  ----------------

  1) Unpack the archive where you would like to store the binaries. For example: :

    Unix-based Operating Systems (Linux, Solaris and Mac OS X)
      tar zxvf sdm-0.3.x-bin.tar.bz2
    Windows 2000/XP
      unzip sdm-0.3.x-bin.tar.zip

  2) A directory called "sdm-0.3.x" will be created.

  3) Add the bin directory to your PATH, eg:

    Unix-based Operating Systems (Linux, Solaris and Mac OS X)
      export PATH=/usr/local/sdm-0.3.x/bin:$PATH
    Windows 2000/XP/7
      set PATH="c:\tools\sdm-0.3.x\bin";%PATH%

  4) Run "mvn --version" to verify that it is correctly installed.


  Licensing
  ---------

  Please see the file called LICENSE.TXT

  SDM URLS
  ----------

  Home Page:          http://orpiske.net/ssps/
  Downloads:          http://orpiske.net/ssps/get.html
  Source Code:        http://github.com/orpiske/sdm
  Issue Tracking:     https://github.com/orpiske/sdm/issues
