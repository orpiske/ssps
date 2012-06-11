package org.ssps.frontend.utils;

import java.io.File;

public final class Constants {
    /**
     * This property is used to set the configuration directory
     */
    public static final String HOME_PROPERTY = "org.ssps.frontend.home";

    public static final String FRONTEND_CONFIG_DIR;

    static {
	FRONTEND_CONFIG_DIR = System.getProperty(HOME_PROPERTY)
		+ File.separator + "conf";
    }

    /**
     * This constant holds the configuration file name for the backend
     */
    public static final String CONFIG_FILE_NAME = "frontend.properties";
}
