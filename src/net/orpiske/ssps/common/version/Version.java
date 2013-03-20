package net.orpiske.ssps.common.version;


public class Version implements Comparable<Version>{
	
	private String value;
	
	
	public Version() {
		
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
