package tourism.entity;

import java.io.IOException;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Model;

public interface IQueryable {
	// Obtain class-specific query string
	public String queryString();
	
	// Get results and write to file
	public void queryToFile() throws IOException;
	
	// Get instance of an object for each result
	public IQueryable getObjectFromQuery(QuerySolution qs) throws IOException;
	
	// Create and return a model containing a triple from obtained object
	public Model getModelInstance(IQueryable queryableEntity);
}
