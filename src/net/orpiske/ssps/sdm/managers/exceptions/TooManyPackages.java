package net.orpiske.ssps.sdm.managers.exceptions;

import java.util.List;

import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.repository.PackageInfo;

@SuppressWarnings("serial")
public class TooManyPackages extends SspsException {
	private List<PackageInfo> packages;

	public TooManyPackages(String name) {
		super("Too many packages found: " + name);
	}

	public TooManyPackages(String name, List<PackageInfo> packages) {
		super("Too many packages found: " + name);
		
		this.packages = packages;
	}

	public List<PackageInfo> getPackages() {
		return packages;
	}
}
