package tourism.entity;

public abstract class Entity {
	private String name;
	private String label;
	private String hasDescription;
	
	public Entity() {
		
	}

	public Entity(String name, String label, String hasDescription) {
		super();
		this.name = name;
		this.label = label;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
