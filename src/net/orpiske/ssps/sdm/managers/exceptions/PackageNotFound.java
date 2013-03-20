package net.orpiske.ssps.sdm.managers.exceptions;

import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.repository.PackageInfo;

@SuppressWarnings("serial")
public class PackageNotFound extends SspsException {
	public PackageNotFound(String name, Throwable t) {
		super("Package not found: " + name, t);
	}

	public PackageNotFound(String name) {
		super("Package not found: " + name);
	}
	
	
	public PackageNotFound(PackageInfo packageInfo) {
		super("Package not found: " + packageInfo == null? "null" : packageInfo.getName());
	}
}
