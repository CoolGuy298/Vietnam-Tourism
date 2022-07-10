package tourism.entity;

public abstract class Entity {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Entity() {
		
	}

	public Entity(String name) {
		super();
		this.name = name;
	}
	
}
