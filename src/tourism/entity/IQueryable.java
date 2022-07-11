package tourism.entity;

import java.io.IOException;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Model;

public interface IQueryable {
	public String queryString();
	
	public IQueryable getObjectFromQuery(QuerySolution qs) throws IOException;
	
	public Model getModelInstance(IQueryable queryableEntity);
	
	public void queryToFile() throws IOException;
}
