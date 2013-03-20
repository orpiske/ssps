package net.orpiske.ssps.sdm.managers.exceptions;

import java.util.List;

import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;

@SuppressWarnings("serial")
public class MultipleInstalledPackages extends SspsException {
	
	private List<SoftwareInventoryDto> softwareList;

	public MultipleInstalledPackages(String name) {
		super("Multiple installed packages found: " + name);
	}

	public MultipleInstalledPackages(String name, List<SoftwareInventoryDto> softwareList) {
		super("Multiple installed packages found: " + name);
		
		this.softwareList = softwareList;
	}

	public List<SoftwareInventoryDto> getSoftwareList() {
		return softwareList;
	}
}
