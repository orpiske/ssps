package net.orpiske.ssps.common.version;

import net.orpiske.ssps.common.version.slot.SlotComparator;
import net.orpiske.ssps.common.version.slot.SlotComparatorFactory;

public class Version implements Comparable<Version>{
	
	private String value;
	private SlotComparator slotComparator;
	
	public Version() {
		slotComparator = SlotComparatorFactory.create(SlotComparatorFactory.DEFAULT_SLOT);
	}
	
	public Version(final String slotMask) {
		slotComparator = SlotComparatorFactory.create(slotMask);
	}
	
	
	public Version(final SlotComparator slotComparator) {
		this.slotComparator = slotComparator;  
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}

	
	public boolean fits(Version other) {
		return slotComparator.fits(getValue(), other.getValue());
	}

	@Override
	public int compareTo(Version other) {
		VersionComparator versionComparator = new DefaultVersionComparator();
		
		return versionComparator.compare(getValue(), other.getValue());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
 		
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		
		Version other = (Version) obj;
		if (getValue() == null) {
			if (other.getValue() == null) { 
				return true;
			}
			
			return false;
		}
		
		return getValue().equals(other.getValue()); 
	}
	
	
	public static Version toVersion(final String value) {
		Version version = new Version();
		version.setValue(value);
		
		return version;
	}
	
	
	

}
