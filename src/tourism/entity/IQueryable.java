package tourism.entity;

import java.io.IOException;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Model;

public interface IQueryable {
	public void queryToFile() throws IOException;
	public Model processQuery(QuerySolution qs) throws IOException;
}
