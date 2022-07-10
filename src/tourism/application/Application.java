package tourism.application;

import java.io.IOException;

import tourism.entity.site.Building;

public class Application {

	public static void main(String[] args) throws IOException{
		Building b = new Building();
		b.query();
	}

}
