package tourism.entity;

public abstract class Entity {
	private String name;
	private String hasDescription;
	
	public Entity() {
		
	}

	public Entity(String name, String hasDescription) {
		super();
		this.name = name;
		this.setHasDescription(hasDescription);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getHasDescription() {
		return hasDescription;
	}

	public void setHasDescription(String hasDescription) {
		this.hasDescription = hasDescription;
	}
	
}
