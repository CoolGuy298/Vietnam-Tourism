package tourism.entity;

import org.apache.jena.rdf.model.Model;

public abstract class Entity {
	private String name;
	private Model model;

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

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	
}
