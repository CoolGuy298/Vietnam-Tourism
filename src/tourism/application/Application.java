package tourism.application;

import java.io.IOException;
import java.util.ArrayList;

import tourism.entity.person.*;
import tourism.entity.IQueryable;
import tourism.entity.event.*;
import tourism.entity.site.*;

public class Application {

	public static void main(String[] args) throws IOException{
		// Container for registering query handlers
		ArrayList<IQueryable> handlerList = new ArrayList<IQueryable>();
		handlerList.add(new King());
		handlerList.add(new Festival());
		handlerList.add(new Building());
		handlerList.add(new NaturalSite());
		
		try {
			for (IQueryable handler : handlerList) {
				handler.queryToFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
