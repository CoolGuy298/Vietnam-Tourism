package tourism.application;

import java.io.IOException;
import java.util.ArrayList;

import tourism.entity.person.*;
import tourism.entity.IQueryable;
import tourism.entity.event.*;
import tourism.entity.site.*;

public class Application {
	// Container for registering query handlers
	private static ArrayList<IQueryable> handlerList = new ArrayList<IQueryable>();

	public static void main(String[] args){
		Application.handlerList.add(new King());
		Application.handlerList.add(new Festival());
		Application.handlerList.add(new Building());
		Application.handlerList.add(new NaturalSite());
		
		try {
			for (IQueryable handler : Application.handlerList) {
				handler.queryToFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
