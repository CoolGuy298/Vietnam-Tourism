package tourism.entity.event;

import tourism.entity.Entity;

public class Event extends Entity {
	private String hasTimeHappen;
	
	public Event() {
		
	}

	public Event(String name, String label, String hasDescription, String hasTimeHappen) {
		super(name, label, hasDescription);
		this.hasTimeHappen = hasTimeHappen;
	}

	public String getHasTimeHappen() {
		return hasTimeHappen;
	}

	public void setHasTimeHappen(String hasTimeHappen) {
		this.hasTimeHappen = hasTimeHappen;
	}
}
