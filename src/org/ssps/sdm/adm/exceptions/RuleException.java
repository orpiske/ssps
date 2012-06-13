package org.ssps.sdm.adm.exceptions;

import org.ssps.common.exceptions.SspsException;

@SuppressWarnings("serial")
public class RuleException extends SspsException {

	public RuleException(String message) {
		super(message);
	}

	public RuleException(String message, Throwable t) {
		super(message, t);
	}

}
