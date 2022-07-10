package tourism.entity.site;

import tourism.entity.Entity;

public class TouristSite extends Entity{
	private String hasAdministrativeDivision;

	public String getHasAdministrativeDivision() {
		return hasAdministrativeDivision;
	}

	public void setHasAdministrativeDivision(String hasAdministrativeDivision) {
		this.hasAdministrativeDivision = hasAdministrativeDivision;
	}
	
	public TouristSite() {
		
	}

	public TouristSite(String name, String label, String hasDescription, String hasAdministrativeDivision) {
		super(name, label, hasDescription);
		this.hasAdministrativeDivision = hasAdministrativeDivision;
	}
	
}
