package tourism.entity.event;

import java.io.IOException;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;
import tourism.vocabulary.VNTOURISM;
import org.apache.jena.vocabulary.RDFS;

public class Festival extends Event implements IQueryable {
	private static final String TARGETFILE = "festivals.txt";

	public Festival() {
		
	}

	public Festival(String name, String label, String hasDescription, String hasTimeHappen) {
		super(name, label, hasDescription, hasTimeHappen);
	}
	
	public String queryString() {
		return "prefix yago: <http://dbpedia.org/class/yago/>\r\n"
				+ "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "\r\n"
				+ "SELECT DISTINCT ?festival ?label ?hasDescription (str(?begin) AS ?hasTimeHappen)\r\n"
				+ "WHERE {\r\n"
				+ "     {?festival rdf:type yago:WikicatFestivalsInVietnam.}\r\n"
				+ "     ?festival rdfs:label ?label.\r\n"
				+ "     ?festival rdfs:comment ?hasDescription.\r\n"
				+ "     OPTIONAL {?festival dbp:date ?begin.}\r\n"
				+ "     FILTER (lang(?label) = 'en')\r\n"
				+ "     FILTER (lang(?hasDescription) = 'en')\r\n"
				+ "}";
	}

	public void queryToFile() throws IOException {
		String queryString = this.queryString();
		
		QueryExecution qexec = DataProcessor.getQueryConnection(queryString);
		ResultSet results = qexec.execSelect();
		Model m = ModelFactory.createDefaultModel();
		m.setNsPrefixes(VNTOURISM.PREFIXMAP);
		
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			Festival f = (Festival) this.getObjectFromQuery(qs);
			Model temp = this.getModelInstance(f);
			m = m.union(temp);
		}
		
		DataProcessor.writeToFile(m, Festival.TARGETFILE);
		qexec.close();
	}
	
	public IQueryable getObjectFromQuery(QuerySolution qs) throws IOException {
		String name = DataProcessor.processImpl(qs, "festival");
		String label = qs.getLiteral("label").toString();
		String hasDescription = qs.getLiteral("hasDescription").toString();
		Literal hasTimeHappenLiteral = qs.getLiteral("hasTimeHappen");
		String hasTimeHappen = "";
		if (hasTimeHappenLiteral != null)
			hasTimeHappen = hasTimeHappenLiteral.toString();

		return new Festival(name, label, hasDescription, hasTimeHappen);
	}
	
	public Model getModelInstance(IQueryable queryableEntity) {
		Festival f = (Festival) queryableEntity;
		Model localModel = ModelFactory.createDefaultModel();
		localModel.createResource(VNTOURISM.URI + f.getName(), VNTOURISM.Festival)
		    .addLiteral(RDFS.label, f.getLabel())
			.addLiteral(VNTOURISM.hasDescription, f.getHasDescription())
			.addLiteral(VNTOURISM.hasTimeHappen, f.getHasTimeHappen());
		
		return localModel;
	}

	
}
