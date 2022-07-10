package tourism.application;

import java.io.IOException;

import tourism.entity.person.King;

public class Application {

	public static void main(String[] args) throws IOException{
		King k = new King();
		k.query();
	}

}
