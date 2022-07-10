package tourism.entity.person;

import tourism.entity.Entity;

public class Person extends Entity{
	private String hasBorn;
	private String hasBornAt;
	private String hasDied;
	
	public String getHasBorn() {
		return hasBorn;
	}
	public void setHasBorn(String hasBorn) {
		this.hasBorn = hasBorn;	
	}
	public String getHasBornAt() {
		return hasBornAt;
	}
	public void setHasBornAt(String hasBornAt) {
		this.hasBornAt = hasBornAt;
	}
	public String getHasDied() {
		return hasDied;
	}
	public void setHasDied(String hasDied) {
		this.hasDied = hasDied;
	}
	
	public Person() {
		super();
	}
	
	public Person(String name, String label, String hasDescription, String hasBorn, String hasBornAt,
			String hasDied) {
		super(name, label, hasDescription);
		this.hasBorn = hasBorn;
		this.hasBornAt = hasBornAt;
		this.hasDied = hasDied;
	}

}
