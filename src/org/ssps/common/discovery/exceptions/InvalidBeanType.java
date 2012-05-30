package org.ssps.common.discovery.exceptions;

import org.ssps.common.exceptions.SspsException;

public class InvalidBeanType extends SspsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InvalidBeanType(final String message) {
	super(message);
    }
    
       
    public InvalidBeanType(final String message, final Throwable t) {
	super(message, t);
    }    

}
